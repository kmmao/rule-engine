package cn.ruleengine.core.rule.strategy;

import cn.ruleengine.core.rule.RuleSetStrategyType;
import org.springframework.lang.NonNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/29
 * @since 1.0.0
 */
public class RuleSetStrategyFactory {

    public static RuleSetStrategy getInstance(@NonNull RuleSetStrategyType strategy) {
        switch (strategy) {
            case ALL_RULE:
                break;
            case WHEN_A_RULE_IS_HIT:
                break;
            case THE_FIRST_RULE:
                break;
            case WHEN_A_RULE_EXECUTE_FAILS:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + strategy);
        }
        return null;
    }

}
