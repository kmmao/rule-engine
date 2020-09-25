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
    private RuleEngineElementManager ruleEngineElementManager;
    @Resource
    private RuleEngineConditionGroupConditionManager ruleEngineConditionGroupConditionManager;


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
        DataCacheMap cacheMap = valueResolve.getCacheMap(JSONObject.parseObject(ruleEngineRule.getCountInfo(), RuleCountInfo.class));
        Rule rule = new Rule();
        rule.setId(ruleEngineRule.getId());
        rule.setCode(ruleEngineRule.getCode());
        rule.setName(ruleEngineRule.getName());
        rule.setDescription(ruleEngineRule.getDescription());
        rule.setConditionSet(this.loadConditionSet(ruleEngineRule, cacheMap));
        rule.setActionValue(valueResolve.getValue(ruleEngineRule.getActionType(), ruleEngineRule.getActionValueType(), ruleEngineRule.getActionValue(), cacheMap));
        rule.setAbnormalAlarm(JSONObject.parseObject(ruleEngineRule.getAbnormalAlarm(), Rule.AbnormalAlarm.class));
        // 如果启用了默认结果
        if (ruleEngineRule.getEnableDefaultAction().equals(EnableEnum.ENABLE.getStatus())) {
            rule.setDefaultActionValue(valueResolve.getValue(ruleEngineRule.getDefaultActionType(), ruleEngineRule.getDefaultActionValueType(), ruleEngineRule.getDefaultActionValue(), cacheMap));
        }
        // 规则入参
        rule.setParameters(this.getRuleParameter(ruleEngineRule.getCountInfo()));
        return rule;
    }

    public Set<Rule.Parameter> getRuleParameter(String countInfo) {
        RuleCountInfo ruleCountInfo = JSON.parseObject(countInfo, RuleCountInfo.class);
        Set<Rule.Parameter> parameters = new HashSet<>();
        if (CollUtil.isNotEmpty(ruleCountInfo.getElementIds())) {
            List<RuleEngineElement> engineElements = this.ruleEngineElementManager.lambdaQuery()
                    .in(RuleEngineElement::getId, ruleCountInfo.getElementIds()).list();
            for (RuleEngineElement engineElement : engineElements) {
                Rule.Parameter parameter = new Rule.Parameter();
                parameter.setName(engineElement.getName());
                parameter.setCode(engineElement.getCode());
                parameter.setValueType(engineElement.getValueType());
                parameters.add(parameter);
            }
        }
        return parameters;
    }

    /**
     * 获取规则配置条件集，懒得写的，待优化
     *
     * @param ruleEngineRule 规则
     * @return 条件集
     */
    private ConditionSet loadConditionSet(RuleEngineRule ruleEngineRule, DataCacheMap cacheMap) {
        List<RuleEngineConditionGroup> conditionGroups = this.ruleEngineConditionGroupManager.lambdaQuery()
                .eq(RuleEngineConditionGroup::getRuleId, ruleEngineRule.getId())
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
        ConditionSet conditionSet = new ConditionSet();
        for (RuleEngineConditionGroup group : conditionGroups) {
            ConditionGroup conditionGroup = new ConditionGroup();
            conditionGroup.setId(group.getId());
            conditionGroup.setOrderNo(group.getOrderNo());
            List<RuleEngineConditionGroupCondition> groupConditions = conditionGroupConditionMaps.get(group.getId());
            if (CollUtil.isNotEmpty(groupConditions)) {
                for (RuleEngineConditionGroupCondition groupCondition : groupConditions) {
                    RuleEngineCondition engineCondition = cacheMap.getConditionMap().get(groupCondition.getConditionId());
                    Condition condition = new Condition();
                    condition.setId(engineCondition.getId());
                    condition.setName(engineCondition.getName());
                    condition.setOrderNo(groupCondition.getOrderNo());
                    condition.setLeftValue(valueResolve.getValue(engineCondition.getLeftType(), engineCondition.getLeftValueType(), engineCondition.getLeftValue(), cacheMap));
                    condition.setOperator(Operator.getByName(engineCondition.getSymbol()));
                    condition.setRightValue(valueResolve.getValue(engineCondition.getRightType(), engineCondition.getRightValueType(), engineCondition.getRightValue(), cacheMap));
                    conditionGroup.addCondition(condition);
                }
                conditionSet.addConditionGroup(conditionGroup);
            }
        }
        return conditionSet;
    }
}
