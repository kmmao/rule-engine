package cn.ruleengine.core.decisiontable;

import cn.ruleengine.core.Configuration;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.condition.*;
import cn.ruleengine.core.value.Value;
import cn.ruleengine.core.value.ValueType;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/12/18
 * @since 1.0.0
 */
@Data
public class DecisionCondition implements ConditionCompare {

    /**
     * 条件左值
     */
    private Object leftValue;
    /**
     * 左值类型
     */
    private ValueType leftValueType;
    /**
     * 运算符
     */
    private Operator operator;
    /**
     * 为固定值
     */
    private Value rightValue;

    public DecisionCondition(Object leftValue, ValueType leftValueType, Operator operator, Value rightValue) {
        this.leftValue = leftValue;
        this.leftValueType = leftValueType;
        this.operator = operator;
        this.rightValue = rightValue;
    }

    @Override
    public boolean compare(Input input, Configuration configuration) {
        Compare compare = ConditionCompareFactory.getCompare(this.leftValueType);
        Object rValue = this.rightValue.getValue(input, configuration);
        return compare.compare(leftValue, this.operator, rValue);
    }

}
