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
package com.engine.core;

import com.engine.core.annotation.Executor;
import com.engine.core.annotation.FailureStrategy;
import com.engine.core.exception.FunctionException;

import java.lang.reflect.Method;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/11/21
 * @since 1.0.0
 */
public class FunctionProcess {

    /**
     * 获取函数中带有@Executor注解的方法
     *
     * @param abstractFunction 函数
     * @return Method
     */
    public Method getExecutorMethod(Object abstractFunction) {
        Method executor = null;
        Method[] methods = abstractFunction.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Executor.class)) {
                //如果已经存在，则抛出异常
                if (executor != null) {
                    throw new FunctionException("函数中存在多个@Executor");
                }
                executor = method;
            }
        }
        String functionName = abstractFunction.getClass().getSimpleName();
        if (executor == null) {
            throw new FunctionException("{}中没有找到可执行函数方法", functionName);
        }
        return executor;
    }

    /**
     * 获取函数中带有@FailureStrategy注解的方法
     *
     * @param abstractFunction 函数
     * @return Method
     */
    public Method getFailureStrategyMethod(Object abstractFunction) {
        Method failureStrategy = null;
        Method[] methods = abstractFunction.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(FailureStrategy.class)) {
                //如果已经存在，则抛出异常
                if (failureStrategy != null) {
                    throw new FunctionException("函数中存在多个@FailureStrategy");
                }
                failureStrategy = method;
            }
        }
        return failureStrategy;
    }

}
