package cn.ruleengine.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.web.enums.RuleStatus;
import cn.ruleengine.web.service.RulePublishService;
import cn.ruleengine.web.store.entity.RuleEngineRulePublish;
import cn.ruleengine.web.store.manager.RuleEngineRulePublishManager;
import cn.ruleengine.core.rule.Rule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/4
 * @since 1.0.0
 */
@Slf4j
@Service
public class RulePublishServiceImpl implements RulePublishService {

    @Resource
    private RuleEngineRulePublishManager ruleEngineRulePublishManager;

    /**
     * 根据规则code，查询发布规则
     *
     * @param workspaceCode 工作空间code
     * @param ruleCode      规则code
     * @return 规则
     */
    @Override
    public Rule getPublishRule(String workspaceCode, String ruleCode) {
        RuleEngineRulePublish rulePublish = this.ruleEngineRulePublishManager.lambdaQuery()
                .eq(RuleEngineRulePublish::getStatus, RuleStatus.PUBLISHED.getStatus())
                .eq(RuleEngineRulePublish::getRuleCode, ruleCode)
                .eq(RuleEngineRulePublish::getWorkspaceCode, workspaceCode)
                .one();
        return  Rule.buildRule(rulePublish.getData());
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
        if (CollUtil.isEmpty(rulePublishList)) {
            return Collections.emptyList();
        }
        List<Rule> rules = new ArrayList<>(rulePublishList.size());
        for (RuleEngineRulePublish publish : rulePublishList) {
            try {
                log.info("parse rule for workspace code: {} rule code: {}", publish.getWorkspaceCode(), publish.getRuleCode());
                Rule rule = Rule.buildRule(publish.getData());
                rules.add(rule);
            } catch (Exception e) {
                log.error("parse rule error ", e);
            }
        }
        return rules;
    }

}
