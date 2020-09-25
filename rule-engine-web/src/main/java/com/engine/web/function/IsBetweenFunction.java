package com.engine.web.function;

import cn.hutool.core.lang.Validator;
import com.engine.core.annotation.Executor;
import com.engine.core.annotation.Function;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/30
 * @since 1.0.0
 */
@Slf4j
@Function
public class IsBetweenFunction {

    /**
     * 函数主方法，如果不存在抛出异常
     *
     * @param params 参数绑定，函数入参
     * @return true/false
     */
    @Executor
    public Boolean executor(@Valid Params params) {
        return Validator.isBetween(params.getValue(), params.getMin(), params.getMax());
    }


    @Data
    public static class Params {
        @NotNull
        private Integer value;
        @NotNull
        private Integer min;
        @NotNull
        private Integer max;
    }

}
