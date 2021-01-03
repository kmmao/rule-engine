package cn.ruleengine.web.service.generalrule.impl;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.rule.AbnormalAlarm;
import cn.ruleengine.core.rule.GeneralRule;
import cn.ruleengine.web.enums.EnableEnum;
import cn.ruleengine.web.service.generalrule.GeneralRuleResolveService;
import cn.ruleengine.web.service.ValueResolve;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import com.alibaba.fastjson.JSONObject;
import cn.ruleengine.core.condition.Condition;
import cn.ruleengine.core.condition.ConditionGroup;
import cn.ruleengine.core.condition.ConditionSet;
import cn.ruleengine.core.condition.Operator;
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
public class GeneralRuleResolveServiceImpl implements GeneralRuleResolveService {

    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private RuleEngineGeneralRuleManager ruleEngineGeneralRuleManager;
    @Resource
    private ValueResolve valueResolve;
    @Resource
    private RuleEngineConditionGroupManager ruleEngineConditionGroupManager;
    @Resource
    private RuleEngineConditionGroupConditionManager ruleEngineConditionGroupConditionManager;
    @Resource
    private RuleEngineConditionManager ruleEngineConditionManager;


    /**
     * 根据规则id查询解析一个规则
     *
     * @param id 规则id
     * @return rule
     */
    @Override
    public GeneralRule getGeneralRuleById(Integer id) {
        log.info("开始加载规则：{}", id);
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        return ruleProcess(ruleEngineGeneralRule);
    }

    /**
     * 解析RuleEngineRule为Rule
     *
     * @param ruleEngineGeneralRule ruleEngineGeneralRule
     * @return Rule
     */
    @Override
    public GeneralRule ruleProcess(RuleEngineGeneralRule ruleEngineGeneralRule) {
        GeneralRule rule = new GeneralRule();
        rule.setId(ruleEngineGeneralRule.getId());
        rule.setCode(ruleEngineGeneralRule.getCode());
        rule.setName(ruleEngineGeneralRule.getName());
        rule.setWorkspaceId(ruleEngineGeneralRule.getWorkspaceId());
        rule.setWorkspaceCode(ruleEngineGeneralRule.getWorkspaceCode());
        rule.setDescription(ruleEngineGeneralRule.getDescription());
        rule.setConditionSet(this.loadConditionSet(ruleEngineGeneralRule.getRuleId()));
        RuleEngineRule ruleEngineRule = this.ruleEngineRuleManager.getById(ruleEngineGeneralRule.getRuleId());
        rule.setActionValue(this.valueResolve.getValue(ruleEngineRule.getActionType(), ruleEngineRule.getActionValueType(), ruleEngineRule.getActionValue()));
        rule.setAbnormalAlarm(JSONObject.parseObject(ruleEngineGeneralRule.getAbnormalAlarm(), AbnormalAlarm.class));
        // 如果启用了默认结果
        if (EnableEnum.ENABLE.getStatus().equals(ruleEngineGeneralRule.getEnableDefaultAction())) {
            rule.setDefaultActionValue(this.valueResolve.getValue(ruleEngineGeneralRule.getDefaultActionType(), ruleEngineGeneralRule.getDefaultActionValueType(), ruleEngineGeneralRule.getDefaultActionValue()));
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
        if (CollUtil.isEmpty(conditionIds)) {
            return new ConditionSet();
        }
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
