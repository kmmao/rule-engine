package com.engine.web.function;

import cn.hutool.core.lang.Validator;
import com.engine.core.annotation.*;
import lombok.extern.slf4j.Slf4j;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/4/7
 * @since 1.0.0
 */
@Slf4j
@Function
public class IsEmailFunction {

    /**
     * 函数主方法，如果不存在抛出异常
     *
     * @param value 参数绑定，函数入参
     * @return true/false
     */
    @Executor
    public Boolean executor(@Param(value = "value") String value) {
        return Validator.isEmail(value);
    }

    /**
     * 此函数失败默认返回false；
     *
     * @param value 参数绑定，函数入参
     * @return false
     */
    @FailureStrategy
    public Boolean failureStrategy(@Param(value = "value") String value) {
        log.info("我是函数失败策略方法：" + value);
        return false;
    }
}
