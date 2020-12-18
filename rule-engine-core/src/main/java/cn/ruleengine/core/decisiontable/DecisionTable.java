package cn.ruleengine.core.decisiontable;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/12/18
 * @since 1.0.0
 */
@Data
public class DecisionTable {

    /**
     * 规则id
     */
    private Integer id;

    /**
     * 规则Code
     */
    private String code;
    /**
     * 规则名称
     */
    private String name;

    private String description;

    /**
     * 工作空间
     */
    private Integer workspaceId;
    /**
     * 工作空间code
     */
    private String workspaceCode;


}
