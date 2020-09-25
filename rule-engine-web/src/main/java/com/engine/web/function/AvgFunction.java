package com.engine.web.function;

import com.engine.core.annotation.Executor;
import com.engine.core.annotation.Function;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 求平均值
 *
 * @author dingqianwen
 * @date 2020/9/1
 * @since 1.0.0
 */
@Slf4j
@Function
public class AvgFunction {

    @Executor
    public BigDecimal executor(@Valid Params params) {
        List<BigDecimal> list = params.getList();
        Integer scale = params.getScale();
        return list.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(list.size()), scale, BigDecimal.ROUND_HALF_UP);
    }

    @Data
    public static class Params {

        @NotNull
        private List<BigDecimal> list;

        /**
         * 几位小数
         */
        @NotNull
        private Integer scale;

    }
}
