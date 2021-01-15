package cn.ruleengine.web.service.ruleset.impl;

import cn.ruleengine.core.rule.RuleSet;
import cn.ruleengine.web.service.ruleset.RuleSetResolveService;
import cn.ruleengine.web.store.entity.RuleEngineRuleSet;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/15
 * @since 1.0.0
 */
@Service
public class RuleSetResolveServiceImpl implements RuleSetResolveService {

    /**
     * 根据规则集id查询解析一个规则集
     *
     * @param id 规则集id
     * @return rule
     */
    @Override
    public RuleSet getRuleSetById(Integer id) {
        return null;
    }

    /**
     * 处理引擎规则集
     *
     * @param ruleEngineRuleSet 规则引擎规则集
     * @return 规则集
     */
    @Override
    public RuleSet ruleSetProcess(RuleEngineRuleSet ruleEngineRuleSet) {
        return null;
    }
}
