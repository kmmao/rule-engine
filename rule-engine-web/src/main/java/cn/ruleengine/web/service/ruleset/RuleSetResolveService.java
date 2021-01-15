package cn.ruleengine.web.service.ruleset;

import cn.ruleengine.core.rule.RuleSet;
import cn.ruleengine.web.store.entity.RuleEngineRuleSet;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
public interface RuleSetResolveService {

    /**
     * 根据规则集id查询解析一个规则集
     *
     * @param id 规则集id
     * @return rule
     */
    RuleSet getRuleSetById(Integer id);

    /**
     * 处理引擎规则集
     *
     * @param ruleEngineRuleSet 规则引擎规则集
     * @return 规则集
     */
    RuleSet ruleSetProcess(RuleEngineRuleSet ruleEngineRuleSet);

}
