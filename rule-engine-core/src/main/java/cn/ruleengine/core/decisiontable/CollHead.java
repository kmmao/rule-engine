package cn.ruleengine.core.decisiontable;

import cn.ruleengine.core.Configuration;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.condition.Operator;
import cn.ruleengine.core.value.Value;
import lombok.Data;
import org.springframework.lang.NonNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/12/18
 * @since 1.0.0
 */
@Data
public class CollHead {

    /**
     * 条件左值
     */
    private Value leftValue;

    private Object lValue;

    /**
     * 运算符
     */
    private Operator operator;

    public CollHead(Value leftValue, Operator operator) {
        this.leftValue = leftValue;
        this.operator = operator;
    }

    public Object getLeftValue(@NonNull Input input, @NonNull Configuration configuration) {
        if (this.lValue != null) {
            return this.lValue;
        }
        return this.lValue = this.leftValue.getValue(input, configuration);
    }

}
