package com.engine.web.service.impl;

import com.engine.core.rule.Rule;
import com.engine.web.enums.RuleStatus;
import com.engine.web.service.RulePublishService;
import com.engine.web.store.entity.RuleEngineRulePublish;
import com.engine.web.store.manager.RuleEngineRulePublishManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/4
 * @since 1.0.0
 */
@Service
public class RulePublishServiceImpl implements RulePublishService {

    @Resource
    private RuleEngineRulePublishManager ruleEngineRulePublishManager;

    /**
     * 根据规则code，查询发布规则
     *
     * @param ruleCode 规则code
     * @return 规则
     */
    @Override
    public Rule getPublishRuleByCode(String ruleCode) {
        RuleEngineRulePublish rulePublish = this.ruleEngineRulePublishManager.lambdaQuery()
                .eq(RuleEngineRulePublish::getStatus,RuleStatus.PUBLISHED.getStatus())
                .eq(RuleEngineRulePublish::getRuleCode, ruleCode)
                .one();
        Rule rule = new Rule();
        rule.fromJson(rulePublish.getData());
        return rule;
    }

    /**
     * 获取所有的已发布规则
     *
     * @return rule
     */
    @Override
    public List<Rule> getAllPublishRule() {
        List<RuleEngineRulePublish> rulePublishList = this.ruleEngineRulePublishManager.lambdaQuery()
                .eq(RuleEngineRulePublish::getStatus, RuleStatus.PUBLISHED.getStatus())
                .list();
        List<Rule> rules = new ArrayList<>();
        for (RuleEngineRulePublish publish : rulePublishList) {
            Rule rule = new Rule();
            rule.fromJson(publish.getData());
            rules.add(rule);
        }
        return rules;
    }

}
