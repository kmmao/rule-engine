package cn.ruleengine.client;

import cn.ruleengine.client.fegin.GeneralRuleInterface;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020-12-29 16:15
 * @since 1.0.0
 */
public class GeneralRule extends Executor {

    public GeneralRule(RuleEngineProperties ruleEngineProperties, GeneralRuleInterface generalRuleInterface) {
        super(ruleEngineProperties, generalRuleInterface);
    }

}
