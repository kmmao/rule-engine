package cn.ruleengine.core.rule.strategy;

import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.rule.Rule;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 当有一个规则执行不成立时，终止执行
 *
 * @author 丁乾文
 * @create 2020/12/29
 * @since 1.0.0
 */
@Slf4j
public class WhenARuleExecuteFailsStrategy implements RuleSetStrategy {


    private static final WhenARuleExecuteFailsStrategy INSTANCE = new WhenARuleExecuteFailsStrategy();

    private WhenARuleExecuteFailsStrategy() {
    }

    public static WhenARuleExecuteFailsStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Object> compute(List<Rule> rules, Input input, RuleEngineConfiguration configuration) {
        List<Object> actions = null;
        for (Rule rule : rules) {
            log.debug("执行规则：" + rule.getName());
            Object action = rule.execute(input, configuration);
            if (action != null) {
                if (log.isDebugEnabled()) {
                    log.debug("规则：{} 命中结果：{}", rule.getName(), action);
                }
                // 如果匹配到结果，初始化一个arraylist
                if (actions == null) {
                    actions = new ArrayList<>();
                }
                actions.add(action);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("存在不成立规则：" + rule.getName());
                }
                // 当有一个规则执行不成立时，终止执行，如果没有匹配到任何结果时 直接命中默认结果
                return actions;
            }
        }
        return actions;
    }

}
