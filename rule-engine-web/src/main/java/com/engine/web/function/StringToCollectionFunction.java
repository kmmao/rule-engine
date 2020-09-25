package com.engine.web.function;

import cn.hutool.core.collection.CollUtil;
import com.engine.core.annotation.Executor;
import com.engine.core.annotation.Function;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 字符串转为集合
 *
 * @author dingqianwen
 * @date 2020/9/1
 * @since 1.0.0
 */
@Slf4j
@Function
public class StringToCollectionFunction {

    @Executor
    public List<?> executor(@Valid Params params) {
        String regex = params.getRegex();
        return Arrays.asList(params.getValue().split(regex));
    }

    @Data
    public static class Params {

        @NotBlank(message = "字符串不能为空")
        private String value;

        /**
         * 正则分隔符
         */
        @NotNull(message = "正则分隔符不能为空")
        private String regex;

    }
}
