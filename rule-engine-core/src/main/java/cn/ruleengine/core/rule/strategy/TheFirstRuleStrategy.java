package cn.ruleengine.core.rule.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.rule.Rule;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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
            log.info("规则集为空");
            return Collections.emptyList();
        }
        Rule rule = rules.iterator().next();
        log.info("执行规则：{}", rule.getName());
        Object action = rule.execute(input, configuration);
        if (action != null) {
            log.info("规则：{} 命中结果：{}", rule.getName(), action);
        }
        return Collections.singletonList(action);
    }

}
