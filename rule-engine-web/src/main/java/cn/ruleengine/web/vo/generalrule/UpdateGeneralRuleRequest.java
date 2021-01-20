package cn.ruleengine.web.vo.generalrule;

import cn.ruleengine.web.vo.condition.ConditionGroupConfig;
import cn.ruleengine.web.vo.condition.ConfigValue;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
public class UpdateGeneralRuleRequest {

    @NotNull
    private Integer id;

    private List<ConditionGroupConfig> conditionGroup;

    private ConfigValue action;

    private DefaultAction defaultAction;

}
