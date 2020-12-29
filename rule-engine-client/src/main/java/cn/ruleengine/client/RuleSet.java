package cn.ruleengine.client;

import cn.ruleengine.client.fegin.RuleSetInterface;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020-12-29 16:35
 * @since 1.0.0
 */
public class RuleSet extends Executor {

    public RuleSet(RuleEngineProperties ruleEngineProperties, RuleSetInterface ruleSetInterface) {
        super(ruleEngineProperties, ruleSetInterface);
    }

}
