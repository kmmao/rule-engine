package com.engine.web.annotation;

import com.engine.web.enums.RateLimitEnum;
import org.redisson.api.RateIntervalUnit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 〈一句话功能简述〉<br>
 * 〈接口限流〉
 *
 * @author 丁乾文
 * @create 2019/9/22
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 每个周期内请求次数,默认60秒内一个这个ip地址只能请求一次此接口
     *
     * @return int
     */
    long limit() default 1L;

    /**
     * 周期时间内触发
     *
     * @return int
     */
    long refreshInterval() default 60L;

    /**
     * 限流类型,默认根据ip限制
     *
     * @return RateLimitEnum
     */
    RateLimitEnum type() default RateLimitEnum.IP;

    /**
     * 时间单位
     *
     * @return RateIntervalUnit
     */
    RateIntervalUnit rateIntervalUnit() default RateIntervalUnit.SECONDS;
}
