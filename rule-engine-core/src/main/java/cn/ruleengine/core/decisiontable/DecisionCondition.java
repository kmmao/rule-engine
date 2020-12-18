/**
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
