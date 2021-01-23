package cn.ruleengine.core.rule.strategy;

import cn.hutool.core.collection.CollUtil;
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
public class TheFirstRuleStrategy implements RuleSetStrategy {

    private static TheFirstRuleStrategy theFirstRuleStrategy = new TheFirstRuleStrategy();

    private TheFirstRuleStrategy() {
    }

    public static TheFirstRuleStrategy getInstance() {
        return theFirstRuleStrategy;
    }

    @Override
    public List<Object> compute(List<Rule> rules, Input input, RuleEngineConfiguration configuration) {
        // 当没有任何规则时
        if (CollUtil.isEmpty(rules)) {
            return Collections.emptyList();
        }
        Object action = rules.iterator().next().execute(input, configuration);
        return Collections.singletonList(action);
    }

}
