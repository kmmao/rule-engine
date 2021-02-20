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
package cn.ruleengine.core.value;

import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.Input;
import lombok.Getter;
import lombok.ToString;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/3/2
 * @since 1.0.0
 */
@ToString
public class Element implements Value {

    @Getter
    private Integer elementId;
    @Getter
    private String elementCode;

    /**
     * 值类型
     * <p>
     * 注意，如果为日期类型元素，入参只能为时间戳，如果入参想为【yyyy-MM-dd HH:mm:ss】格式后期可以提相关需求！
     * 但是一般调用方可以自己处理后再请求 new Date().getTime()
     */
    private ValueType valueType;

    /**
     * json反序列化使用
     */
    Element() {
    }

    public Element(Integer elementId, String elementCode, ValueType valueType) {
        this.elementId = elementId;
        this.elementCode = elementCode;
        this.valueType = valueType;
    }

    @Override
    public Object getValue(Input input, RuleEngineConfiguration configuration) {
        Object value = input.get(this.getElementCode());
        return this.dataConversion(value, this.getValueType());
    }

    @Override
    public ValueType getValueType() {
        return this.valueType;
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Element)) {
            return false;
        }
        Element element = (Element) other;
        return element.getElementId().equals(this.getElementId());
    }
}
