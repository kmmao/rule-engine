package cn.ruleengine.core.decisiontable;

import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 决策表执行策略
 *
 * @author dingqianwen
 * @date 2020/12/19
 * @since 1.0.0
 */
public enum StrategyType {

    /**
     * 返回所有优先级命中的结果
     */
    ALL_PRIORITY(1),
    /**
     * 返回命中的最高优先级第一个结果
     */
    HIGHEST_PRIORITY_SINGLE(2),
    /**
     * 返回命中的最高优先级所有结果
     */
    HIGHEST_PRIORITY_ALL(3);

    @Getter
    private Integer value;

    StrategyType(Integer value) {
        this.value = value;
    }

}
