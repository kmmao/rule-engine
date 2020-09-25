package com.engine.web.function;

import com.engine.core.annotation.Executor;
import com.engine.core.annotation.Function;
import com.engine.core.annotation.Param;
import lombok.extern.slf4j.Slf4j;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/1
 * @since 1.0.0
 */
@Slf4j
@Function
public class StringLengthFunction {

    @Executor
    public Integer executor(@Param(value = "value", required = false) String value) {
        if (value == null) {
            return 0;
        }
        return value.length();
    }

}
