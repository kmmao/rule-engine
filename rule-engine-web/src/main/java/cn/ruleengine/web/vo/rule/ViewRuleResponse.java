package cn.ruleengine.web.vo.rule;

import cn.ruleengine.core.rule.Rule;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/24
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ViewRuleResponse extends GetRuleResponse {

    /**
     * 规则入参
     */
    private Set<Rule.Parameter> parameters;

}
