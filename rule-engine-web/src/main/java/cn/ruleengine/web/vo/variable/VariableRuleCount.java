package cn.ruleengine.web.vo.variable;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/11/14
 * @since 1.0.0
 */
@Data
public class VariableRuleCount {

    public static final Integer PUBLISH_TABLE = 2;

    private Integer id;

    private String code;

    /**
     * type:2 已发布表中
     */
    private Integer type;

}
