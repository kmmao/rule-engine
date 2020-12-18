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
package cn.ruleengine.core;

import cn.ruleengine.core.value.ValueType;
import org.springframework.lang.Nullable;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/10
 * @since 1.0.0
 */
public class DefaultOutPut implements OutPut {


    private Object value;
    private ValueType valueType;

    public DefaultOutPut(@Nullable Object value, @Nullable ValueType valueType) {
        this.value = value;
        this.valueType = valueType;
    }

    /**
     * 输出的参数值
     *
     * @return 输出值
     */
    @Override
    public Object getValue() {
        return this.value;
    }

    /**
     * value 的数据类型，string，number，boolean，collection
     *
     * @return 数据类型
     */
    @Override
    public ValueType getValueType() {
        return this.valueType;
    }

}
