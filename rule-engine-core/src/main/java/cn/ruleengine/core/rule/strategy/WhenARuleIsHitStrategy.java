package cn.ruleengine.core.rule.strategy;

import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.rule.Rule;

import java.util.Collections;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/29
 * @since 1.0.0
 */
public class WhenARuleIsHitStrategy implements RuleSetStrategy {

    private static WhenARuleIsHitStrategy whenARuleIsHitStrategy = new WhenARuleIsHitStrategy();

    private WhenARuleIsHitStrategy() {
    }

    public static WhenARuleIsHitStrategy getInstance() {
        return whenARuleIsHitStrategy;
    }


    @Override
    public List<Object> compute(List<Rule> rules, Input input, RuleEngineConfiguration configuration) {
        for (Rule rule : rules) {
            Object action = rule.execute(input, configuration);
            if (action != null) {
                return Collections.singletonList(action);
            }
        }
        return null;
    }

}
