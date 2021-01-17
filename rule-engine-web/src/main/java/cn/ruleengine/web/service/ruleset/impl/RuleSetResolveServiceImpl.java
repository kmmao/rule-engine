package cn.ruleengine.web.service.ruleset.impl;

import cn.ruleengine.core.rule.*;
import cn.ruleengine.web.enums.EnableEnum;
import cn.ruleengine.web.service.ConditionSetService;
import cn.ruleengine.web.service.ValueResolve;
import cn.ruleengine.web.service.ruleset.RuleSetResolveService;
import cn.ruleengine.web.store.entity.RuleEngineRule;
import cn.ruleengine.web.store.entity.RuleEngineRuleSet;
import cn.ruleengine.web.store.entity.RuleEngineRuleSetRule;
import cn.ruleengine.web.store.manager.RuleEngineRuleManager;
import cn.ruleengine.web.store.manager.RuleEngineRuleSetManager;
import cn.ruleengine.web.store.manager.RuleEngineRuleSetRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/15
 * @since 1.0.0
 */
@Slf4j
@Service
public class RuleSetResolveServiceImpl implements RuleSetResolveService {

    @Resource
    private RuleEngineRuleSetManager ruleEngineRuleSetManager;
    @Resource
    private ValueResolve valueResolve;
    @Resource
    private RuleEngineRuleSetRuleManager ruleEngineRuleSetRuleManager;
    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private ConditionSetService conditionSetService;

    /**
     * 根据规则集id查询解析一个规则集
     *
     * @param id 规则集id
     * @return rule
     */
    @Override
    public RuleSet getRuleSetById(Integer id) {
        log.info("开始加载规则集：{}", id);
        RuleEngineRuleSet ruleEngineRuleSet = this.ruleEngineRuleSetManager.getById(id);
        return ruleSetProcess(ruleEngineRuleSet);
    }

    /**
     * 处理引擎规则集
     *
     * @param ruleEngineRuleSet 规则引擎规则集
     * @return 规则集
     */
    @Override
    public RuleSet ruleSetProcess(RuleEngineRuleSet ruleEngineRuleSet) {
        RuleSet ruleSet = new RuleSet();
        ruleSet.setId(ruleEngineRuleSet.getId());
        ruleSet.setCode(ruleEngineRuleSet.getCode());
        ruleSet.setName(ruleEngineRuleSet.getName());
        ruleSet.setWorkspaceId(ruleEngineRuleSet.getWorkspaceId());
        ruleSet.setWorkspaceCode(ruleEngineRuleSet.getWorkspaceCode());
        ruleSet.setDescription(ruleEngineRuleSet.getDescription());
        ruleSet.setStrategyType(RuleSetStrategyType.getByValue(ruleEngineRuleSet.getStrategyType()));
        List<RuleEngineRuleSetRule> ruleEngineRuleSetRules = this.ruleEngineRuleSetRuleManager.lambdaQuery().eq(RuleEngineRuleSetRule::getRuleSetId, ruleEngineRuleSet.getId()).list();
        List<Integer> ruleIds = ruleEngineRuleSetRules.stream().map(RuleEngineRuleSetRule::getRuleId)
                .collect(Collectors.toList());
        // 排序，有序执行的
        ruleEngineRuleSetRules.sort(Comparator.comparing(RuleEngineRuleSetRule::getOrderNo));
        ruleIds.add(ruleEngineRuleSet.getDefaultRuleId());
        Map<Integer, RuleEngineRule> ruleEngineRuleMap = this.ruleEngineRuleManager.lambdaQuery().in(RuleEngineRule::getId, ruleIds).list()
                .stream().collect(Collectors.toMap(RuleEngineRule::getId, Function.identity()));
        for (RuleEngineRuleSetRule ruleEngineRuleSetRule : ruleEngineRuleSetRules) {
            RuleEngineRule ruleEngineRule = ruleEngineRuleMap.get(ruleEngineRuleSetRule.getRuleId());
            ruleSet.addRule(this.getRule(ruleEngineRule));
        }
        // 如果启用了默认结果
        if (EnableEnum.ENABLE.getStatus().equals(ruleEngineRuleSet.getEnableDefaultRule())) {
            RuleEngineRule ruleEngineRule = ruleEngineRuleMap.get(ruleEngineRuleSet.getDefaultRuleId());
            ruleSet.setDefaultRule(this.getRule(ruleEngineRule));
        }
        return ruleSet;
    }

    private Rule getRule(RuleEngineRule ruleEngineRule) {
        Rule rule = new Rule();
        rule.setId(ruleEngineRule.getId());
        rule.setCode(ruleEngineRule.getCode());
        rule.setName(ruleEngineRule.getName());
        rule.setDescription(ruleEngineRule.getDescription());
        rule.setConditionSet(this.conditionSetService.loadConditionSet(ruleEngineRule.getId()));
        rule.setActionValue(this.valueResolve.getValue(ruleEngineRule.getActionType(), ruleEngineRule.getActionValueType(), ruleEngineRule.getActionValue()));
        return rule;
    }

}
