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
 * 只执行第一个规则，实际使用中没有太大用处，可以直接用普通规则代替此规则集，只是无聊时开发的
 * <p>
 * 如果第一个没有命中结果，则直接处理默认结果
 *
 * @author 丁乾文
 * @create 2020/12/29
 * @since 1.0.0
 */
@Slf4j
public class TheFirstRuleStrategy implements RuleSetStrategy {

    private static final TheFirstRuleStrategy INSTANCE = new TheFirstRuleStrategy();

    private TheFirstRuleStrategy() {
    }

    public static TheFirstRuleStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Object> compute(List<Rule> rules, Input input, RuleEngineConfiguration configuration) {
        // 当没有任何规则时
        if (CollUtil.isEmpty(rules)) {
            if (log.isDebugEnabled()) {
                log.debug("规则集为空");
            }
            // 没有配置任何规则，走默认结果
            return null;
        }
        Rule rule = rules.iterator().next();
        if (log.isDebugEnabled()) {
            log.debug("执行规则：" + rule.getName());
        }
        Object action = rule.execute(input, configuration);
        // 如果结果为空
        if (action == null) {
            if (log.isDebugEnabled()) {
                log.debug("规则：{} 未命中命中结果", rule.getName());
            }
            return null;
        }
        if (log.isDebugEnabled()) {
            log.debug("规则：{} 命中结果：{}", rule.getName(), action);
        }
        return Collections.singletonList(action);
    }

}
