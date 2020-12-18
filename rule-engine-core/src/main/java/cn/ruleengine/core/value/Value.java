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

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import cn.ruleengine.core.Configuration;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.exception.ValueException;
import cn.ruleengine.core.condition.compare.BooleanCompare;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/2/29
 * @since 1.0.0
 */
public interface Value {

    /**
     * 获取value
     *
     * @param input         上下文
     * @param configuration 规则配置信息
     * @return value
     */
    Object getValue(Input input, Configuration configuration);


    /**
     * value 的类型，元素，固定值，变量
     *
     * @return type
     */
    ValueType getValueType();

    /**
     * 数据转换
     *
     * @param value     转换的数据
     * @param valueType 数据类型
     * @return 转换后的数据
     */
    default Object dataConversion(Object value, ValueType valueType) {
        if (Objects.isNull(value)) {
            return null;
        }
        String valueStr = String.valueOf(value);
        switch (valueType) {
            case COLLECTION:
                if (Validator.isEmpty(value)) {
                    return Collections.emptyList();
                }
                if (value instanceof Collection) {
                    return value;
                } else {
                    return Arrays.asList(valueStr.split(","));
                }
            case NUMBER:
                if (Validator.isEmpty(value)) {
                    return null;
                }
                if (value instanceof BigDecimal) {
                    return value;
                }
                if (NumberUtil.isNumber(valueStr)) {
                    return new BigDecimal(valueStr);
                }
                throw new ValueException("{}只能是Number类型", value);
            case STRING:
                return valueStr;
            case BOOLEAN:
                if (Validator.isEmpty(value)) {
                    return null;
                }
                if (value instanceof Boolean) {
                    return value;
                } else if (Objects.equals(valueStr, BooleanCompare.TRUE)) {
                    return true;
                } else if (Objects.equals(valueStr, BooleanCompare.FALSE)) {
                    return false;
                }
                throw new ValueException("{}只能是Boolean类型", value);
            default:
                throw new ValueException("不支持的数据类型{}", valueType);
        }

    }
}
