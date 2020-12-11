package cn.ruleengine.web.vo.condition;

import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/28
 * @since 1.0.0
 */
@Data
public class ConditionGroupConfig {
    private Integer id;
    private String name;
    private Integer orderNo;
    /**
     * 条件组与条件关系
     */
    private List<ConditionGroupCondition> conditionGroupCondition;
}
