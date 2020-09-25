package com.engine.web.vo.rule;

import lombok.Data;
import lombok.EqualsAndHashCode;


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

    private RuleInterfaceDescriptionResponse ruleInterfaceDescription;

}
