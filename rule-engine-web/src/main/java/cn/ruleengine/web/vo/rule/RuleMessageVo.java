package cn.ruleengine.web.vo.rule;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
@Data
public class RuleMessageVo {

    private RuleMessageVo.Type type;

    private String workspaceCode;

    private Integer workspaceId;

    private String ruleCode;

    public enum Type {
        /**
         * 规则加载，以及移除
         */
        LOAD, UPDATE, REMOVE
    }
}
