/**
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.engine.core.value;

import com.engine.core.Configuration;
import com.engine.core.annotation.Executor;
import com.engine.core.annotation.FailureStrategy;
import com.engine.core.annotation.FunctionCacheable;
import com.engine.core.cache.FunctionCache;
import com.engine.core.cache.KeyGenerator;
import com.engine.core.exception.FunctionException;
import com.engine.core.Input;
import com.engine.core.FunctionExecutor;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
@Slf4j
@Data
public class Function implements Value {

    private Integer id;

    private String name;


    private DataType dataType;

    /**
     * 需要执行的函数
     */
    private Object abstractFunction;

    /**
     * 函数执行主方法
     */
    private Method executor;
    /**
     * 函数失败策略方法
     */
    private Method failureStrategy;

    /**
     * 函数缓存key生成
     */
    private KeyGenerator keyGenerator;
    /**
     * 是否启用缓存
     */
    private Boolean enableCache = false;

    /**
     * 缓存的生存时间，单位：ms
     */
    private long liveOutTime;

    /**
     * 函数执行器
     */
    private FunctionExecutor functionExecutor = new FunctionExecutor();

    @Setter
    private Map<String, Value> param;

    public Function(Integer id, String name, Object abstractFunction, DataType dataType, Map<String, Value> param) {
        this.id = id;
        this.name = name;
        this.dataType = dataType;
        this.param = param;
        this.abstractFunction = abstractFunction;
        // 预解析函数中的方法
        this.initExecutorMethod();
        this.initFailureStrategyMethod();
        this.initKeyGenerator();
    }

    /**
     * 初始化函数缓存key生成
     */
    private void initKeyGenerator() {
        Class<?> abstractFunctionClass = this.abstractFunction.getClass();
        if (!abstractFunctionClass.isAnnotationPresent(FunctionCacheable.class)) {
            return;
        }
        FunctionCacheable functionCacheable = abstractFunctionClass.getAnnotation(FunctionCacheable.class);
        if (functionCacheable.enable()) {
            this.enableCache = true;
            this.liveOutTime = functionCacheable.liveOutTime();
            Class<? extends KeyGenerator> keyGeneratorClass = functionCacheable.keyGenerator();
            try {
                Constructor<? extends KeyGenerator> constructor = keyGeneratorClass.getConstructor();
                if (!Modifier.isPublic(constructor.getModifiers())) {
                    constructor.setAccessible(true);
                }
                this.keyGenerator = constructor.newInstance();
            } catch (Exception e) {
                throw new FunctionException("Function failed to generate cache key");
            }
        }
    }

    public Function(Object abstractFunction) {
        this(null, null, abstractFunction, null, null);
    }

    @Override
    public Object getValue(Input input, Configuration configuration) {
        //处理函数入参
        Map<String, Object> paramMap = new HashMap<>(this.param.size());
        for (Map.Entry<String, Value> entry : this.param.entrySet()) {
            paramMap.put(entry.getKey(), entry.getValue().getValue(input, configuration));
        }
        //获取缓存实现类
        FunctionCache functionCache = configuration.getFunctionCache();
        Class<?> abstractFunctionClass = this.abstractFunction.getClass();
        Object value;
        if (this.enableCache) {
            String key = this.keyGenerator.generate(this.abstractFunction, paramMap);
            value = functionCache.get(key);
            if (value != null) {
                log.debug("{}函数存在缓存", abstractFunctionClass.getSimpleName());
                return value;
            } else {
                log.debug("{}函数不存在缓存,开始执行函数", abstractFunctionClass.getSimpleName());
                value = this.executor(paramMap);
                functionCache.put(key, value, this.liveOutTime);
            }
        } else {
            value = this.executor(paramMap);
        }
        return Optional.ofNullable(value).map(m -> {
            if (!dataType.getClassType().isAssignableFrom(m.getClass())) {
                throw new FunctionException("The return type of the function does not match the set type");
            }
            return m;
        }).orElse(null);
    }

    /**
     * 执行函数
     *
     * @param paramValue 函数值
     * @return 函数返回结果
     */
    public Object executor(Map<String, Object> paramValue) {
        return this.functionExecutor.executor(this.abstractFunction, this.executor, this.failureStrategy, paramValue);
    }

    /**
     * 获取函数中带有@Executor注解的方法
     **/
    private void initExecutorMethod() {
        Method[] methods = this.abstractFunction.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Executor.class)) {
                //如果已经存在，则抛出异常
                if (this.executor != null) {
                    throw new FunctionException("函数中存在多个@Executor");
                }
                this.executor = method;
            }
        }
        String functionName = this.abstractFunction.getClass().getSimpleName();
        if (this.executor == null) {
            throw new FunctionException("{}中没有找到可执行函数方法", functionName);
        }
        if (!Modifier.isPublic(this.executor.getModifiers())) {
            this.executor.setAccessible(true);
        }
    }

    /**
     * 获取函数中带有@FailureStrategy注解的方法
     */
    private void initFailureStrategyMethod() {
        Method[] methods = this.abstractFunction.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(FailureStrategy.class)) {
                //如果已经存在，则抛出异常
                if (this.failureStrategy != null) {
                    throw new FunctionException("函数中存在多个@FailureStrategy");
                }
                this.failureStrategy = method;
            }
        }
        if (this.failureStrategy == null) {
            return;
        }
        if (!Modifier.isPublic(this.failureStrategy.getModifiers())) {
            this.failureStrategy.setAccessible(true);
        }
    }

    @Override
    public String getValueType() {
        return this.getClass().getTypeName();
    }

    @Override
    public DataType getDataType() {
        return this.dataType;
    }
}
