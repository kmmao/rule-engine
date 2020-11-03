package com.engine.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.JSON;
import com.engine.core.value.VariableType;
import com.engine.web.store.entity.*;
import com.engine.web.store.manager.*;
import com.engine.web.store.mapper.RuleEngineConditionMapper;
import com.engine.web.store.mapper.RuleEngineRuleMapper;
import com.engine.web.store.mapper.RuleEngineVariableMapper;
import com.engine.web.vo.rule.RuleCountInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/27
 * @since 1.0.0
 */
@Slf4j
@Service
public class RuleCountInfoService {

    @Resource
    private RuleEngineRuleMapper ruleEngineRuleMapper;
    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineFunctionValueManager ruleEngineFunctionValueManager;
    @Resource
    private RuleEngineVariableMapper ruleEngineVariableMapper;
    @Resource
    private RuleEngineConditionMapper ruleEngineConditionMapper;
    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;

    /**
     * 异步更新引用此变量的规则统计信息
     *
     * @param varId 变量id
     */
    @Async
    public void refreshByVarId(Integer varId) {
        log.info("开始更新引用此变量的规则统计信息,{}", varId);
        List<RuleEngineRule> ruleEngineRules = ruleEngineVariableMapper.countRule(varId);
        this.refresh(ruleEngineRules);
    }

    /**
     * 异步更新引用此条件的规则统计信息
     *
     * @param conditionId 条件id
     */
    @Async
    public void refreshByConditionId(Integer conditionId) {
        log.info("开始更新引用此条件的规则统计信息,{}", conditionId);
        List<RuleEngineRule> ruleEngineRules = ruleEngineConditionMapper.countRule(conditionId);
        this.refresh(ruleEngineRules);
    }

    /**
     * 异步刷新引用此规则引用的基础组建数量
     *
     * @param ruleEngineRules rule
     */
    @Async
    public void refreshAsync(List<RuleEngineRule> ruleEngineRules) {
        this.refresh(ruleEngineRules);
    }

    /**
     * 刷新引用此规则引用的基础组建数量
     *
     * @param ruleEngineRules rule
     */
    public void refresh(List<RuleEngineRule> ruleEngineRules) {
        if (CollUtil.isEmpty(ruleEngineRules)) {
            log.info("没有查询到任何规则引用此数据");
            return;
        }
        List<RuleEngineRule> ruleEngineRuleList = ruleEngineRules.stream().map(m -> {
            RuleEngineRule ruleEngineRule = new RuleEngineRule();
            // 复制id 以及结果默认结果引用的组建信息
            BeanUtil.copyProperties(m, ruleEngineRule);
            RuleCountInfo ruleCountInfo = this.countRuleInfo(ruleEngineRule);
            ruleEngineRule.setCountInfo(JSON.toJSONString(ruleCountInfo));
            return ruleEngineRule;
        }).collect(Collectors.toList());
        this.ruleEngineRuleManager.updateBatchById(ruleEngineRuleList);
    }

    /**
     * 统计引用此规则的所有的元素变量
     *
     * @param ruleEngineRule rule
     * @return RuleCountInfo
     */
    public RuleCountInfo countRuleInfo(RuleEngineRule ruleEngineRule) {
        RuleCountInfo countInfo = new RuleCountInfo();
        countInfo.setRuleId(ruleEngineRule.getId());
        // 查询规则引用的所有的条件
        List<RuleEngineCondition> engineConditions = ruleEngineRuleMapper.countCondition(ruleEngineRule.getId());
        Set<Integer> conditionIds = engineConditions.stream().map(RuleEngineCondition::getId).collect(Collectors.toSet());
        countInfo.setConditionIds(conditionIds);
        // 遍历条件中的变量/元素
        for (RuleEngineCondition engineCondition : engineConditions) {
            if (engineCondition.getRightType().equals(VariableType.ELEMENT.getType())) {
                countInfo.addElementId(Integer.valueOf(engineCondition.getRightValue()));
            }
            if (engineCondition.getLeftType().equals(VariableType.ELEMENT.getType())) {
                countInfo.addElementId(Integer.valueOf(engineCondition.getLeftValue()));
            }
            if (engineCondition.getLeftType().equals(VariableType.VARIABLE.getType())) {
                RuleEngineVariable ruleEngineVariable = this.ruleEngineVariableManager.getById(engineCondition.getLeftValue());
                this.countVariableElement(countInfo, ruleEngineVariable);
            }
            if (engineCondition.getRightType().equals(VariableType.VARIABLE.getType())) {
                RuleEngineVariable ruleEngineVariable = this.ruleEngineVariableManager.getById(engineCondition.getRightValue());
                this.countVariableElement(countInfo, ruleEngineVariable);
            }
        }
        this.countResult(countInfo, ruleEngineRule);
        return countInfo;
    }

    /**
     * 统计函数参数中引用的元素/变量信息
     *
     * @param countInfo          countInfo
     * @param ruleEngineVariable var
     */
    private void countVariableElement(RuleCountInfo countInfo, RuleEngineVariable ruleEngineVariable) {
        countInfo.addVariableId(ruleEngineVariable.getId());
        if (ruleEngineVariable.getType().equals(VariableType.FUNCTION.getType())) {
            List<RuleEngineFunctionValue> engineFunctionValues = this.ruleEngineFunctionValueManager.lambdaQuery()
                    .eq(RuleEngineFunctionValue::getVariableId, ruleEngineVariable.getId()).list();
            for (RuleEngineFunctionValue engineFunctionValue : engineFunctionValues) {
                if (engineFunctionValue.getType().equals(VariableType.ELEMENT.getType())) {
                    countInfo.addElementId(Integer.valueOf(engineFunctionValue.getValue()));
                } else if (engineFunctionValue.getType().equals(VariableType.VARIABLE.getType())) {
                    this.countVariableElement(countInfo, this.ruleEngineVariableManager.getById(engineFunctionValue.getValue()));
                }
            }
        }
    }

    /**
     * 统计规则结果中引用的元素变量
     *
     * @param countInfo      countInfo
     * @param ruleEngineRule rule
     */
    private void countResult(RuleCountInfo countInfo, RuleEngineRule ruleEngineRule) {
        Integer actionType = ruleEngineRule.getActionType();
        if (actionType == null) {
            return;
        }
        if (actionType.equals(VariableType.ELEMENT.getType())) {
            countInfo.addElementId(Integer.valueOf(ruleEngineRule.getActionValue()));
        } else if (actionType.equals(VariableType.VARIABLE.getType())) {
            RuleEngineVariable ruleEngineVariable = this.ruleEngineVariableManager.getById(ruleEngineRule.getActionValue());
            this.countVariableElement(countInfo, ruleEngineVariable);
        }
        Integer defaultActionType = ruleEngineRule.getDefaultActionType();
        // 如果存在默认结果
        if (Validator.isNotEmpty(defaultActionType)) {
            if (defaultActionType.equals(VariableType.ELEMENT.getType())) {
                countInfo.addElementId(Integer.valueOf(ruleEngineRule.getDefaultActionValue()));
            } else if (defaultActionType.equals(VariableType.VARIABLE.getType())) {
                RuleEngineVariable ruleEngineVariable = this.ruleEngineVariableManager.getById(ruleEngineRule.getDefaultActionValue());
                this.countVariableElement(countInfo, ruleEngineVariable);
            }
        }
    }
}
