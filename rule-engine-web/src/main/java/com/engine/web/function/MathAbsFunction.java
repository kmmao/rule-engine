package com.engine.web.function;

import com.engine.core.annotation.Executor;
import com.engine.core.annotation.Function;
import com.engine.core.annotation.Param;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/4/7
 * @since 1.0.0
 */
@Function
public class MathAbsFunction {

    @Executor
    public Integer executor(@Param("value") Integer value) {
        return Math.abs(value);
    }

}
