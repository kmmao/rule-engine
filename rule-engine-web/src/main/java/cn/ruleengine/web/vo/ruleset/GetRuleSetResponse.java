package cn.ruleengine.web.vo.ruleset;

import cn.ruleengine.core.rule.AbnormalAlarm;
import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/15
 * @since 1.0.0
 */
@Data
public class GetRuleSetResponse {

    private Integer id;

    private String name;

    private String code;

    private String description;

    private Integer workspaceId;

    private String workspaceCode;

    private List<RuleBody> ruleSet;

    private Integer strategyType;

    private Integer enableDefaultRule;

    private RuleBody defaultRule;

    private AbnormalAlarm abnormalAlarm;

}
