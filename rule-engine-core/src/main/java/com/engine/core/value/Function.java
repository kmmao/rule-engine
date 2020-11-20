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

    private Object abstractFunction;

    private DataType dataType;

    private String name;

    /**
     * 函数执行器
     */
    private FunctionExecutor processor = new FunctionExecutor();

    @Setter
    private Map<String, Value> param;

    public Function(Integer id, String name, Object abstractFunction, DataType dataType, Map<String, Value> param) {
        this.id = id;
        this.name = name;
        this.abstractFunction = abstractFunction;
        this.dataType = dataType;
        this.param = param;
    }

    @Override
    public Object getValue(Input input, Configuration configuration) {
        //处理函数入参
        Map<String, Object> paramMap = new HashMap<>(param.size());
        for (Map.Entry<String, Value> entry : param.entrySet()) {
            paramMap.put(entry.getKey(), entry.getValue().getValue(input, configuration));
        }
        //获取缓存实现类
        FunctionCache functionCache = configuration.getFunctionCache();
        Class<?> abstractFunctionClass = this.abstractFunction.getClass();
        Object value;
        FunctionCacheable enableFunctionCache = abstractFunctionClass.getAnnotation(FunctionCacheable.class);
        if (enableFunctionCache != null && enableFunctionCache.enable()) {
            Class<? extends KeyGenerator> keyGeneratorClass = enableFunctionCache.keyGenerator();
            KeyGenerator keyGenerator;
            try {
                Constructor<? extends KeyGenerator> constructor = keyGeneratorClass.getConstructor();
                if (!Modifier.isPublic(constructor.getModifiers())) {
                    constructor.setAccessible(true);
                }
                keyGenerator = constructor.newInstance();
            } catch (Exception e) {
                throw new FunctionException("Function failed to generate cache key");
            }
            String key = keyGenerator.generate(abstractFunction, paramMap);
            value = functionCache.get(key);
            if (value != null) {
                log.debug("{}函数存在缓存", abstractFunctionClass.getSimpleName());
                return value;
            } else {
                log.debug("{}函数不存在缓存,开始执行函数", abstractFunctionClass.getSimpleName());
                value = this.processor.executor(abstractFunction, paramMap);
                functionCache.put(key, value, enableFunctionCache.liveOutTime());
            }
        } else {
            value = this.processor.executor(abstractFunction, paramMap);
        }
        return Optional.ofNullable(value).map(m -> {
            if (!dataType.getClassType().isAssignableFrom(m.getClass())) {
                throw new FunctionException("The return type of the function does not match the set type");
            }
            return m;
        }).orElse(null);
    }


    @Override
    public String getValueType() {
        return this.getClass().getTypeName();
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }
}
