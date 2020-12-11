package cn.ruleengine.web.vo.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Data
public class ConfigBean {

    @Valid
    private Value leftValue;

    @NotBlank
    private String symbol;

    @Valid
    private Value rightValue;

    @NoArgsConstructor
    @Data
    public static class Value {

        @NotNull
        private Integer type;

        @NotBlank
        private String value;

        private String valueName;

        /**
         * 固定值变量 值
         * value为变量的id
         */
        private String variableValue;

        @NotBlank
        private String valueType;

    }

}
