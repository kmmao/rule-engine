package cn.ruleengine.web.service.decisiontable;

import cn.ruleengine.core.decisiontable.DecisionTable;
import cn.ruleengine.web.store.entity.RuleEngineDecisionTable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
public interface DecisionTableResolveService {

    /**
     * 根据决策表id查询解析一个决策表
     *
     * @param id 决策表id
     * @return rule
     */
    DecisionTable getDecisionTableById(Integer id);

    /**
     * 处理引擎决策表
     *
     * @param ruleEngineDecisionTable 规则引擎决策表
     * @return 决策表
     */
    DecisionTable decisionTableProcess(RuleEngineDecisionTable ruleEngineDecisionTable);

}
