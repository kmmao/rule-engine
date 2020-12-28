package cn.ruleengine.core.rule;

import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020-12-28 15:43
 * @since 1.0.0
 */
public enum RuleSetStrategyType {

    /**
     * 执行所有规则，然后返回结果，如果没有结果返回默认规则结果，如果没有默认规则返回null
     */
    ALL_RULE(1),
    /**
     * 当有一个规则执行结果被命中，则终止执行，返回这个结果
     */
    WHEN_A_RULE_IS_HIT(2),
    /**
     * 只执行第一个规则，然后返回结果，如果没有结果返回默认规则结果，如果没有默认规则返回null
     */
    THE_FIRST_RULE(3),
    /**
     * 当有一个规则执行不成立时，终止执行，返回结果，如果没有结果返回默认规则结果，如果没有默认规则返回null
     */
    WHEN_A_RULE_EXECUTE_FAILS(4);

    @Getter
    private Integer value;

    RuleSetStrategyType(Integer value) {
        this.value = value;
    }

}
