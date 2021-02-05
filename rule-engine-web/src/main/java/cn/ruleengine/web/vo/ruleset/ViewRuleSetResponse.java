package cn.ruleengine.web.vo.ruleset;

import cn.ruleengine.web.vo.common.Parameter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/15
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ViewRuleSetResponse extends GetRuleSetResponse {

    /**
     * 规则入参
     */
    private Set<Parameter> parameters;

}
