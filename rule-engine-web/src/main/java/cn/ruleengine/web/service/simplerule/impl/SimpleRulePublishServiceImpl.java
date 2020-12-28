package cn.ruleengine.web.service.simplerule.impl;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.rule.SimpleRule;
import cn.ruleengine.web.enums.DataStatus;
import cn.ruleengine.web.service.simplerule.SimpleRulePublishService;
import cn.ruleengine.web.store.entity.RuleEngineSimpleRulePublish;
import cn.ruleengine.web.store.manager.RuleEngineSimpleRulePublishManager;
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
public class SimpleRulePublishServiceImpl implements SimpleRulePublishService {

    @Resource
    private RuleEngineSimpleRulePublishManager ruleEngineSimpleRulePublishManager;

    /**
     * 根据规则code，查询发布规则
     *
     * @param workspaceCode 工作空间code
     * @param ruleCode      规则code
     * @return 规则
     */
    @Override
    public SimpleRule getPublishSimpleRule(String workspaceCode, String ruleCode) {
        RuleEngineSimpleRulePublish rulePublish = this.ruleEngineSimpleRulePublishManager.lambdaQuery()
                .eq(RuleEngineSimpleRulePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineSimpleRulePublish::getSimpleRuleCode, ruleCode)
                .eq(RuleEngineSimpleRulePublish::getWorkspaceCode, workspaceCode)
                .one();
        return SimpleRule.buildRule(rulePublish.getData());
    }

    /**
     * 获取所有的已发布规则
     *
     * @return rule
     */
    @Override
    public List<SimpleRule> getAllPublishSimpleRule() {
        List<RuleEngineSimpleRulePublish> rulePublishList = this.ruleEngineSimpleRulePublishManager.lambdaQuery()
                .eq(RuleEngineSimpleRulePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .list();
        if (CollUtil.isEmpty(rulePublishList)) {
            return Collections.emptyList();
        }
        List<SimpleRule> rules = new ArrayList<>(rulePublishList.size());
        for (RuleEngineSimpleRulePublish publish : rulePublishList) {
            try {
                log.info("parse rule for workspace code: {} rule code: {}", publish.getWorkspaceCode(), publish.getSimpleRuleCode());
                SimpleRule rule = SimpleRule.buildRule(publish.getData());
                rules.add(rule);
            } catch (Exception e) {
                log.error("parse rule error ", e);
            }
        }
        return rules;
    }

}
