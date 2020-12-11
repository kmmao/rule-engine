package cn.ruleengine.web.vo.variable;

import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/25
 * @since 1.0.0
 */
@Data
public class GetVariableResponse {

    private Integer id;

    private String name;

    private String description;

    private Integer type;

    private String valueType;

    private String value;

    private Function function;

    @Data
    public static class Function {

        private Integer id;
        /**
         * 函数名称
         */
        private String name;
        /**
         * 函数返回值
         */
        private String returnValueType;

        /**
         * 函数中所有的参数
         */
        private List<ParamValue> paramValues;
    }
}
