package cn.ruleengine.web.vo.rule;

import cn.ruleengine.core.rule.Rule;
import cn.ruleengine.web.vo.condition.ConditionGroupConfig;
import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/24
 * @since 1.0.0
 */
@Data
public class GetRuleResponse {

    private Integer id;

    private String name;

    private String code;

    private String description;

    private Integer workspaceId;

    private String workspaceCode;

    private List<ConditionGroupConfig> conditionGroup;

    private Action action;

    private DefaultAction defaultAction;

    private Rule.AbnormalAlarm abnormalAlarm;

}
