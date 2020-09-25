package com.engine.web.function;

import com.engine.core.annotation.Executor;
import com.engine.core.annotation.Function;
import com.engine.core.annotation.Param;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

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
public class SumFunction {

    @Executor
    public BigDecimal executor(@Param(value = "list") List<BigDecimal> list) {
        return list.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
