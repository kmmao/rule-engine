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

import cn.ruleengine.core.condition.Compare;
import cn.ruleengine.core.condition.ConditionCompareFactory;
import cn.ruleengine.core.value.Value;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/12/19
 * @since 1.0.0
 */
public class CollHeadCompare extends CollHead {

    private Object lValue;

    public void setValue(Object lValue) {
        this.lValue = lValue;
    }

    public Object getValue() {
        return this.lValue;
    }

    /**
     * 表头比较器
     *
     * @param rValue 单元格数据
     * @return true 条件成立
     */
    public boolean compare(Object rValue) {
        Value leftValue = super.getLeftValue();
        Compare compare = ConditionCompareFactory.getCompare(leftValue.getValueType());
        return compare.compare(lValue, super.getOperator(), rValue);
    }

}
