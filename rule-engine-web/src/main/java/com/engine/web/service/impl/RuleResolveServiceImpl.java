package com.engine.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.engine.core.condition.Condition;
import com.engine.core.condition.ConditionGroup;
import com.engine.core.condition.ConditionSet;
import com.engine.core.condition.Operator;
import com.engine.core.rule.Rule;
import com.engine.web.enums.EnableEnum;
import com.engine.web.service.RuleResolveService;
import com.engine.web.store.entity.*;
import com.engine.web.store.manager.*;
import com.engine.web.service.ValueResolve;
import com.engine.web.vo.common.DataCacheMap;
import com.engine.web.vo.rule.RuleCountInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
@Slf4j
@Service
public class RuleResolveServiceImpl implements RuleResolveService {

    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private ValueResolve valueResolve;
    @Resource
    private RuleEngineConditionGroupManager ruleEngineConditionGroupManager;
    @Resource
    private RuleEngineConditionGroupConditionManager ruleEngineConditionGroupConditionManager;
    @Resource
    private RuleEngineConditionManager ruleEngineConditionManager;


    /**
     * 根据规则code查询解析一个规则
     *
     * @param ruleCode 规则code
     * @return rule
     */
    @Override
    public Rule getRuleByCode(String ruleCode) {
        log.info("开始加载规则：{}", ruleCode);
        RuleEngineRule ruleEngineRule = this.ruleEngineRuleManager.lambdaQuery().eq(RuleEngineRule::getCode, ruleCode).one();
        return ruleProcess(ruleEngineRule);
    }

    /**
     * 根据规则id查询解析一个规则
     *
     * @param id 规则id
     * @return rule
     */
    @Override
    public Rule getRuleById(Integer id) {
        log.info("开始加载规则：{}", id);
        RuleEngineRule ruleEngineRule = this.ruleEngineRuleManager.getById(id);
        return ruleProcess(ruleEngineRule);
    }

    /**
     * 解析RuleEngineRule为Rule
     *
     * @param ruleEngineRule ruleEngineRule
     * @return Rule
     */
    private Rule ruleProcess(RuleEngineRule ruleEngineRule) {
        Rule rule = new Rule();
        rule.setId(ruleEngineRule.getId());
        rule.setCode(ruleEngineRule.getCode());
        rule.setName(ruleEngineRule.getName());
        rule.setDescription(ruleEngineRule.getDescription());
        rule.setConditionSet(this.loadConditionSet(ruleEngineRule.getId()));
        rule.setActionValue(valueResolve.getValue(ruleEngineRule.getActionType(), ruleEngineRule.getActionValueType(), ruleEngineRule.getActionValue()));
        rule.setAbnormalAlarm(JSONObject.parseObject(ruleEngineRule.getAbnormalAlarm(), Rule.AbnormalAlarm.class));
        // 如果启用了默认结果
        if (EnableEnum.ENABLE.getStatus().equals(ruleEngineRule.getEnableDefaultAction())) {
            rule.setDefaultActionValue(valueResolve.getValue(ruleEngineRule.getDefaultActionType(), ruleEngineRule.getDefaultActionValueType(), ruleEngineRule.getDefaultActionValue()));
        }
        return rule;
    }


    /**
     * 获取规则配置条件集，懒得写的，待优化
     *
     * @param ruleId 规则id
     * @return 条件集
     */
    private ConditionSet loadConditionSet(Integer ruleId) {
        List<RuleEngineConditionGroup> conditionGroups = this.ruleEngineConditionGroupManager.lambdaQuery()
                .eq(RuleEngineConditionGroup::getRuleId, ruleId)
                .orderByAsc(RuleEngineConditionGroup::getOrderNo)
                .list();
        if (CollUtil.isEmpty(conditionGroups)) {
            return new ConditionSet();
        }
        // 加载所有的用到的条件组条件
        Set<Integer> conditionGroupIds = conditionGroups.stream().map(RuleEngineConditionGroup::getId).collect(Collectors.toSet());
        Map<Integer, List<RuleEngineConditionGroupCondition>> conditionGroupConditionMaps = this.ruleEngineConditionGroupConditionManager.lambdaQuery()
                .in(RuleEngineConditionGroupCondition::getConditionGroupId, conditionGroupIds)
                .orderByAsc(RuleEngineConditionGroupCondition::getOrderNo)
                .list()
                .stream().collect(Collectors.groupingBy(RuleEngineConditionGroupCondition::getConditionGroupId));
        Set<Integer> conditionIds = conditionGroupConditionMaps.values().stream().flatMap(Collection::stream).map(RuleEngineConditionGroupCondition::getConditionId).collect(Collectors.toSet());
        List<RuleEngineCondition> ruleEngineConditions = ruleEngineConditionManager.lambdaQuery().in(RuleEngineCondition::getId, conditionIds).list();
        Map<Integer, RuleEngineCondition> conditionMap = ruleEngineConditions.stream().collect(Collectors.toMap(RuleEngineCondition::getId, Function.identity()));
        ConditionSet conditionSet = new ConditionSet();
        for (RuleEngineConditionGroup group : conditionGroups) {
            ConditionGroup conditionGroup = new ConditionGroup();
            conditionGroup.setId(group.getId());
            conditionGroup.setOrderNo(group.getOrderNo());
            List<RuleEngineConditionGroupCondition> groupConditions = conditionGroupConditionMaps.get(group.getId());
            if (CollUtil.isNotEmpty(groupConditions)) {
                for (RuleEngineConditionGroupCondition groupCondition : groupConditions) {
                    RuleEngineCondition engineCondition = conditionMap.get(groupCondition.getConditionId());
                    Condition condition = new Condition();
                    condition.setId(engineCondition.getId());
                    condition.setName(engineCondition.getName());
                    condition.setOrderNo(groupCondition.getOrderNo());
                    condition.setLeftValue(valueResolve.getValue(engineCondition.getLeftType(), engineCondition.getLeftValueType(), engineCondition.getLeftValue()));
                    condition.setOperator(Operator.getByName(engineCondition.getSymbol()));
                    condition.setRightValue(valueResolve.getValue(engineCondition.getRightType(), engineCondition.getRightValueType(), engineCondition.getRightValue()));
                    conditionGroup.addCondition(condition);
                }
                conditionSet.addConditionGroup(conditionGroup);
            }
        }
        return conditionSet;
    }
}
