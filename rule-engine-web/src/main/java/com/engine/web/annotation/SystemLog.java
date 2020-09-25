package com.engine.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 〈一句话功能简述〉<br>
 * 〈有此注解的请求方法,会持久化日志〉
 *
 * @author 丁乾文
 * @create 2019/11/1
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLog {
    /**
     * 日志说明
     *
     * @return String
     */
    String description() default "";
}
