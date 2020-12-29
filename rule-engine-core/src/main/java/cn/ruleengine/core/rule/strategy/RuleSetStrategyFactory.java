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

    /**
     * 获取规则引擎执行策略
     *
     * @param strategy 策略类型
     * @return RuleSetStrategy
     */
    public static RuleSetStrategy getInstance(@NonNull RuleSetStrategyType strategy) {
        switch (strategy) {
            case ALL_RULE:
                return AllRuleStrategy.getInstance();
            case WHEN_A_RULE_IS_HIT:
                return WhenARuleIsHitStrategy.getInstance();
            case THE_FIRST_RULE:
                return TheFirstRuleStrategy.getInstance();
            case WHEN_A_RULE_EXECUTE_FAILS:
                return WhenARuleExecuteFailsStrategy.getInstance();
            default:
                throw new IllegalStateException("Unexpected value: " + strategy);
        }
    }

}
