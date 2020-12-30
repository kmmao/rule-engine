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
package cn.ruleengine.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.exception.FunctionException;
import cn.ruleengine.core.annotation.Param;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.core.exception.ValueException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.validation.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/19
 * @since 1.0.0
 */
@Slf4j
public class FunctionExecutor {


    private FunctionExecuteMethodParamsParser functionExecuteMethodParamsParser = new FunctionExecuteMethodParamsParser();

    /**
     * 函数执行器
     *
     * @param abstractFunction 执行的函数
     * @param paramMap         函数入参
     * @return 函数执行结果
     */
    public Object executor(Object abstractFunction, Method executor, Method failureStrategy, Map<String, Object> paramMap) {
        log.info("开始解析并执行函数：{}，函数入参：{}", abstractFunction, paramMap);
        Executor executorAnnotation = executor.getAnnotation(Executor.class);
        Object[] executorMethodArgs = this.functionExecuteMethodParamsParser.getBindArgs(executor.getParameters(), paramMap);
        try {
            int maxAttempts = executorAnnotation.maxAttempts();
            int i = 0;
            do {
                try {
                    return executor.invoke(abstractFunction, executorMethodArgs);
                } catch (IllegalAccessException e) {
                    throw new FunctionException("主函数方法非法访问异常{}", e.getMessage());
                } catch (Exception e) {
                    //当重试全部用完后，还是失败，则抛出异常
                    if (i == maxAttempts || maxAttempts < 0) {
                        throw e;
                    }
                    log.warn("执行函数主方法异常，{}ms后重试调用，异常原因：{}", executorAnnotation.delay(), e);
                    ThreadUtil.sleep(executorAnnotation.delay());
                }
                i++;
            } while (i <= maxAttempts);
            String functionName = abstractFunction.getClass().getSimpleName();
            throw new FunctionException("{} Function execution failed", functionName);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            log.warn("函数主方法执行失败", targetException);
            // 如果存在失败策略方法
            if (failureStrategy != null) {
                Class<? extends Throwable>[] noFailureFor = executorAnnotation.noFailureFor();
                //如果遇到这种类型的异常，都是直接抛出的
                for (Class<? extends Throwable> aClass : noFailureFor) {
                    if (aClass.isAssignableFrom(targetException.getClass())) {
                        throw new FunctionException(targetException);
                    }
                }
                //再判断是否满足失败函数触发的异常
                Class<? extends Throwable>[] failureFor = executorAnnotation.failureFor();
                for (Class<? extends Throwable> aClass : failureFor) {
                    if (aClass.isAssignableFrom(targetException.getClass())) {
                        log.info("开始执行函数失败策略方法");
                        try {
                            // 如果失败策略方法参数与执行方法参数类型相同时
                            if (Arrays.equals(failureStrategy.getParameters(), executor.getParameters())) {
                                return failureStrategy.invoke(abstractFunction, executorMethodArgs);
                            }
                            Object[] failureStrategyMethodArgs = this.functionExecuteMethodParamsParser.getBindArgs(failureStrategy.getParameters(), paramMap);
                            return failureStrategy.invoke(abstractFunction, failureStrategyMethodArgs);
                        } catch (IllegalAccessException ex) {
                            throw new FunctionException("失败策略方法非法访问异常{}", ex.getMessage());
                        } catch (InvocationTargetException ex) {
                            log.error("失败策略方法执行失败", ex.getTargetException());
                            throw new FunctionException(ex.getTargetException());
                        }
                    }
                }
                log.warn("失败策略方法异常未命中执行");
            }
            throw new FunctionException(targetException);
        }

    }

    private static class FunctionExecuteMethodParamsParser {

        /**
         * 参数绑定用，基本数据类型绑定解析
         */
        private static final Set<Class<?>> BASIC_TYPE = new HashSet<Class<?>>() {

            private static final long serialVersionUID = 5728375214844388515L;

            {
                add(String.class);
                add(Integer.class);
                add(BigDecimal.class);
                add(Boolean.class);
            }

        };

        /**
         * 获取绑定参数
         *
         * @param parameters 方法参数列表
         * @param paramMap   执行入参
         * @return 绑定后的参数列表
         */
        @SneakyThrows
        private Object[] getBindArgs(Parameter[] parameters, Map<String, Object> paramMap) {
            if (ArrayUtil.isEmpty(parameters)) {
                return new Object[]{};
            }
            Object[] args = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                Class<?> parameterType = parameter.getType();
                if (Map.class.isAssignableFrom(parameterType)) {
                    args[i] = this.paramConvertMap(parameter, paramMap);
                } else if (BASIC_TYPE.contains(parameterType)) {
                    args[i] = this.paramConvertBasicType(parameter, paramMap);
                } else if (List.class.isAssignableFrom(parameterType) || Set.class.isAssignableFrom(parameterType)) {
                    args[i] = this.paramConvertCollection(parameter, paramMap);
                } else {
                    args[i] = this.paramConvertBean(parameter, paramMap);
                }
            }
            return args;
        }

        private Map<String, Object> paramConvertMap(Parameter parameter, Map<String, Object> paramMap) {
            Type parameterParameterizedType = parameter.getParameterizedType();
            if (parameterParameterizedType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) parameterParameterizedType;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (!(actualTypeArguments[0].equals(String.class) && actualTypeArguments[1].equals(Object.class))) {
                    throw new ValidException("仅支持范型为<String,Object>类型Map");
                }
            }
            return paramMap;
        }

        @SneakyThrows
        private Object paramConvertBasicType(Parameter parameter, Map<String, Object> paramMap) {
            Class<?> parameterType = parameter.getType();
            Object value = paramMap.get(getParameterName(parameter));
            this.paramValid(parameter, value);
            Object instance = parameterType.getConstructor(String.class).newInstance(String.valueOf(value));
            return Optional.ofNullable(value).map(m -> instance).orElse(null);
        }

        private Collection<?> paramConvertCollection(Parameter parameter, Map<String, Object> paramMap) {
            Class<?> parameterType = parameter.getType();
            Type parameterParameterizedType = parameter.getParameterizedType();
            Collection<?> value = (Collection<?>) paramMap.get(getParameterName(parameter));
            // 校验集合参数
            this.paramValid(parameter, value);
            // bug 修复，空集合参数导致空指针问题
            if (value != null) {
                Stream<?> stream = value.stream();
                if (parameterParameterizedType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) parameterParameterizedType;
                    Type typeArgument = parameterizedType.getActualTypeArguments()[0];
                    // 判断方法集合类型
                    if (typeArgument.equals(BigDecimal.class)) {
                        stream = stream.map(String::valueOf).map(BigDecimal::new);
                    } else if (typeArgument.equals(Integer.class)) {
                        stream = stream.map(String::valueOf).map(Integer::valueOf);
                    }
                }
                if (Set.class.isAssignableFrom(parameterType)) {
                    return stream.collect(Collectors.toSet());
                } else {
                    return stream.collect(Collectors.toList());
                }
            }
            return null;
        }

        @SneakyThrows
        private Object paramConvertBean(Parameter parameter, Map<String, Object> paramMap) {
            Class<?> parameterType = parameter.getType();
            Constructor<?> constructor = parameterType.getConstructor();
            if (!Modifier.isPublic(constructor.getModifiers())) {
                constructor.setAccessible(true);
            }
            Object newInstance = constructor.newInstance();
            BeanUtil.copyProperties(paramMap, newInstance);
            //如果参数前面有Valid注解
            if (parameter.isAnnotationPresent(Valid.class)) {
                //参数校验,带有注解的属性校验
                Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                //验证某个对象,，其实也可以只验证其中的某一个属性的
                Set<ConstraintViolation<Object>> constraintViolations = validator.validate(newInstance);
                Iterator<ConstraintViolation<Object>> iter = constraintViolations.iterator();
                if (iter.hasNext()) {
                    ConstraintViolation<Object> next = iter.next();
                    String messageTemplate = next.getMessageTemplate();
                    if (messageTemplate == null) {
                        throw new ValueException(next.getMessage());
                    }
                    if (messageTemplate.startsWith(StrUtil.DELIM_START) && messageTemplate.endsWith(StrUtil.DELIM_END)) {
                        throw new ValueException(next.getPropertyPath().toString() + next.getMessage());
                    }
                }
            }
            return newInstance;
        }

        /**
         * 获取方法参数名称，如果存在Param此注解并且注解value不为空，则返回注解value,否则直接返回参数的name
         *
         * @param parameter 参数信息
         * @return 参数名称
         */
        private String getParameterName(Parameter parameter) {
            if (parameter.isAnnotationPresent(Param.class)) {
                Param param = parameter.getAnnotation(Param.class);
                return StrUtil.isBlank(param.value()) ? parameter.getName() : param.value();
            }
            return parameter.getName();
        }

        /**
         * 校验普通参数
         *
         * @param parameter 参数信息
         * @param value     参数值
         */
        private void paramValid(Parameter parameter, Object value) {
            String name = getParameterName(parameter);
            if (parameter.isAnnotationPresent(Param.class)) {
                Param param = parameter.getAnnotation(Param.class);
                if (!param.required()) {
                    return;
                }
                if (Objects.isNull(value)) {
                    throw new ValidException("{} can not be null", name);
                }
            }
        }
    }


}
