package cn.ruleengine.web.service.decisiontable.impl;

import cn.ruleengine.core.condition.Operator;
import cn.ruleengine.core.decisiontable.*;
import cn.ruleengine.core.rule.AbnormalAlarm;
import cn.ruleengine.core.value.Value;
import cn.ruleengine.web.enums.EnableEnum;
import cn.ruleengine.web.service.ValueResolve;
import cn.ruleengine.web.service.decisiontable.DecisionTableResolveService;
import cn.ruleengine.web.store.entity.RuleEngineDecisionTable;
import cn.ruleengine.web.store.manager.RuleEngineDecisionTableManager;
import cn.ruleengine.web.vo.condition.ConfigValue;
import cn.ruleengine.web.vo.decisiontable.*;
import cn.ruleengine.web.vo.generalrule.DefaultAction;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
@Service
public class DecisionTableResolveServiceImpl implements DecisionTableResolveService {

    @Resource
    private RuleEngineDecisionTableManager ruleEngineDecisionTableManager;
    @Resource
    private ValueResolve valueResolve;


    /**
     * 根据决策表id查询解析一个决策表
     *
     * @param id 决策表id
     * @return rule
     */
    @Override
    public DecisionTable getDecisionTableById(Integer id) {
        RuleEngineDecisionTable ruleEngineDecisionTable = this.ruleEngineDecisionTableManager.getById(id);
        return this.decisionTableProcess(ruleEngineDecisionTable);
    }


    /**
     * 处理引擎决策表
     *
     * @param ruleEngineDecisionTable 规则引擎决策表
     * @return 决策表
     */
    @Override
    public DecisionTable decisionTableProcess(RuleEngineDecisionTable ruleEngineDecisionTable) {
        TableData tableData = JSON.parseObject(ruleEngineDecisionTable.getTableData(), TableData.class);
        DecisionTable decisionTable = new DecisionTable();
        decisionTable.setId(ruleEngineDecisionTable.getId());
        decisionTable.setCode(ruleEngineDecisionTable.getCode());
        decisionTable.setName(ruleEngineDecisionTable.getName());
        decisionTable.setDescription(ruleEngineDecisionTable.getDescription());
        decisionTable.setWorkspaceId(ruleEngineDecisionTable.getWorkspaceId());
        decisionTable.setWorkspaceCode(ruleEngineDecisionTable.getWorkspaceCode());
        decisionTable.setAbnormalAlarm(JSON.parseObject(ruleEngineDecisionTable.getAbnormalAlarm(), AbnormalAlarm.class));
        decisionTable.setStrategyType(DecisionTableStrategyType.getByValue(ruleEngineDecisionTable.getStrategyType()));
        List<CollConditionHeads> collConditionHeads = tableData.getCollConditionHeads();
        for (CollConditionHeads collConditionHead : collConditionHeads) {
            CollHead collHead = new CollHead();
            ConfigValue leftValue = collConditionHead.getLeftValue();
            collHead.setName(collConditionHead.getName());
            collHead.setLeftValue(this.valueResolve.getValue(leftValue.getType(), leftValue.getValueType(), leftValue.getValue()));
            collHead.setOperator(Operator.getByName(collConditionHead.getSymbol()));
            decisionTable.addCollHead(collHead);
        }
        List<Rows> rows = tableData.getRows();
        for (int i = 0; i < rows.size(); i++) {
            Rows row = rows.get(i);
            Row decisionTableRow = new Row();
            decisionTableRow.setOrder(i);
            decisionTableRow.setPriority(row.getPriority());
            List<ConfigValue> conditions = row.getConditions();
            for (ConfigValue condition : conditions) {
                if (condition.getType() == null) {
                    // 空的单元格
                    decisionTableRow.addColl(new Coll());
                } else {
                    Value value = this.valueResolve.getValue(condition.getType(), condition.getValueType(), condition.getValue());
                    decisionTableRow.addColl(new Coll(value));
                }
            }
            ConfigValue result = row.getResult();
            Value value = this.valueResolve.getValue(result.getType(), result.getValueType(), result.getValue());
            decisionTableRow.setAction(value);
            decisionTable.addRow(decisionTableRow);
        }
        DefaultAction defaultAction = tableData.getCollResultHead().getDefaultAction();
        if (EnableEnum.ENABLE.getStatus().equals(defaultAction.getEnableDefaultAction())) {
            decisionTable.setDefaultActionValue(this.valueResolve.getValue(defaultAction.getType(), defaultAction.getValueType(), defaultAction.getValue()));
        }
        return decisionTable;
    }

}
