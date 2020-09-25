package com.engine.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/8/13
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReSubmitLock {
    /**
     * 拿到锁后默认10秒解锁
     *
     * @return long
     */
    long timeOut() default 10 * 1000;

    /**
     * 是否让参数加为key
     *
     * @return boolean
     */
    boolean lockKeyArgs() default false;
}
