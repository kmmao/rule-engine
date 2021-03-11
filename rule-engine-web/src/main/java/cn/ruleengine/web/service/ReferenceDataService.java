package cn.ruleengine.web.service;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.web.vo.common.ReferenceData;
import cn.ruleengine.web.vo.condition.*;
import cn.ruleengine.web.vo.decisiontable.CollConditionHeads;
import cn.ruleengine.web.vo.decisiontable.CollResultHead;
import cn.ruleengine.web.vo.decisiontable.Rows;
import cn.ruleengine.web.vo.decisiontable.TableData;
import cn.ruleengine.web.vo.generalrule.DefaultAction;
import cn.ruleengine.web.vo.generalrule.GeneralRuleBody;
import cn.ruleengine.web.vo.ruleset.RuleBody;
import cn.ruleengine.web.vo.ruleset.RuleSetBody;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2021/1/23
 * @since 1.0.0
 */
@Service
public class ReferenceDataService {

    /**
     * 统计决策表引用的变量以及元素id
     *
     * @param tableData 决策表
     * @return ReferenceData
     */
    public ReferenceData countReferenceData(TableData tableData) {
        ReferenceData referenceData = new ReferenceData();
        List<CollConditionHeads> collConditionHeads = tableData.getCollConditionHeads();
        if (CollUtil.isNotEmpty(collConditionHeads)) {
            for (CollConditionHeads collConditionHead : collConditionHeads) {
                referenceData.resolve(collConditionHead.getLeftValue());
            }
        }
        CollResultHead collResultHead = tableData.getCollResultHead();
        DefaultAction defaultAction = collResultHead.getDefaultAction();
        referenceData.resolve(defaultAction);
        List<Rows> rows = tableData.getRows();
        for (Rows row : rows) {
            List<ConfigValue> conditions = row.getConditions();
            for (ConfigValue condition : conditions) {
                referenceData.resolve(condition);
            }
            ConfigValue result = row.getResult();
            referenceData.resolve(result);
        }
        return referenceData;
    }

    /**
     * 统计规则集引用的变量以及元素id
     *
     * @param ruleSetBody 规则集
     * @return ReferenceData
     */
    public ReferenceData countReferenceData(RuleSetBody ruleSetBody) {
        ReferenceData referenceData = new ReferenceData();
        RuleBody defaultRule = ruleSetBody.getDefaultRule();
        referenceData.resolve(defaultRule.getAction());
        this.countReferenceData(referenceData, defaultRule.getConditionGroup());
        List<RuleBody> ruleSet = ruleSetBody.getRuleSet();
        for (RuleBody ruleBody : ruleSet) {
            referenceData.resolve(ruleBody.getAction());
            this.countReferenceData(referenceData, ruleBody.getConditionGroup());
        }
        return referenceData;
    }

    /**
     * 条件组引用的变量以及元素
     *
     * @param referenceData  referenceData
     * @param conditionGroup 条件组
     */
    private void countReferenceData(ReferenceData referenceData, List<ConditionGroupConfig> conditionGroup) {
        for (ConditionGroupConfig conditionGroupConfig : conditionGroup) {
            List<ConditionGroupCondition> conditionGroupCondition = conditionGroupConfig.getConditionGroupCondition();
            // 已知bug修复
            if (CollUtil.isEmpty(conditionGroupCondition)) {
                continue;
            }
            for (ConditionGroupCondition groupCondition : conditionGroupCondition) {
                ConditionBody condition = groupCondition.getCondition();
                ConfigBean config = condition.getConfig();
                referenceData.resolve(config.getLeftValue());
                referenceData.resolve(config.getRightValue());
                // 引用的条件id
                referenceData.addConditionId(condition.getId());
            }
        }
    }

    /**
     * 统计普通规则引用的变量以及元素id
     *
     * @param generalRuleBody 普通规则
     * @return ReferenceData
     */
    public ReferenceData countReferenceData(GeneralRuleBody generalRuleBody) {
        ReferenceData referenceData = new ReferenceData();
        referenceData.resolve(generalRuleBody.getAction());
        DefaultAction defaultAction = generalRuleBody.getDefaultAction();
        referenceData.resolve(defaultAction);
        this.countReferenceData(referenceData, generalRuleBody.getConditionGroup());
        return referenceData;
    }

}
