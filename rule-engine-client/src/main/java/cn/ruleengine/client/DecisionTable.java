package cn.ruleengine.client;

import cn.ruleengine.client.fegin.BaseInterface;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020-12-29 15:51
 * @since 1.0.0
 */
public class DecisionTable extends Executor {

    public DecisionTable(RuleEngineProperties ruleEngineProperties, BaseInterface decisionTableInterface) {
        super(ruleEngineProperties, decisionTableInterface);
    }

}
