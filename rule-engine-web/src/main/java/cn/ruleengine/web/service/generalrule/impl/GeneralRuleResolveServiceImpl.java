package cn.ruleengine.web.service.generalrule.impl;

import cn.ruleengine.core.rule.GeneralRule;
import cn.ruleengine.web.enums.EnableEnum;
import cn.ruleengine.web.service.ConditionSetService;
import cn.ruleengine.web.service.generalrule.GeneralRuleResolveService;
import cn.ruleengine.web.service.ValueResolve;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    private ConditionSetService conditionSetService;

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
        rule.setConditionSet(this.conditionSetService.loadConditionSet(ruleEngineGeneralRule.getRuleId()));
        RuleEngineRule ruleEngineRule = this.ruleEngineRuleManager.getById(ruleEngineGeneralRule.getRuleId());
        rule.setActionValue(this.valueResolve.getValue(ruleEngineRule.getActionType(), ruleEngineRule.getActionValueType(), ruleEngineRule.getActionValue()));
        // 如果启用了默认结果
        if (EnableEnum.ENABLE.getStatus().equals(ruleEngineGeneralRule.getEnableDefaultAction())) {
            rule.setDefaultActionValue(this.valueResolve.getValue(ruleEngineGeneralRule.getDefaultActionType(), ruleEngineGeneralRule.getDefaultActionValueType(), ruleEngineGeneralRule.getDefaultActionValue()));
        }
        return rule;
    }

}



