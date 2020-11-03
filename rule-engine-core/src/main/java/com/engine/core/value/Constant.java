/**
 * Copyright @2020 dingqianwen
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
package com.engine.core.value;

import com.engine.core.Configuration;
import com.engine.core.Input;
import com.engine.core.EngineVariable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/3/2
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class Constant implements Value {

    /**
     * 常量值
     */
    @Setter
    private Object value;

    /**
     * 值类型
     */
    private DataType dataType;

    public Constant(Object value, DataType dataType) {
        this.value = value;
        this.dataType = dataType;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Constant)) {
            return false;
        }
        Constant constant = (Constant) other;
        if (this.getDataType() != constant.getDataType()) {
            return false;
        }
        Object curValue = dataConversion(this.value, getDataType());
        Object constantValue = dataConversion(constant.getValue(), getDataType());
        switch (constant.getDataType()) {
            case NUMBER:
                if (((Number) curValue).longValue() != ((Number) constantValue).longValue()) {
                    return false;
                }
                break;
            case COLLECTION:
                Collection<?> curValueColl = (Collection<?>) curValue;
                Collection<?> constantValueColl = (Collection<?>) constantValue;
                if (curValueColl.size() != constantValueColl.size()) {
                    return false;
                }
                if (!curValueColl.containsAll(constantValueColl)) {
                    return false;
                }
                break;
            case STRING:
            case BOOLEAN:
                if (!Objects.equals(this.value, constant.getValue())) {
                    return false;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + constant.getDataType());
        }
        return true;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    public String getValueType() {
        return this.getClass().getTypeName();
    }

    @Override
    public Object getValue(Input input, Configuration configuration) {
        return dataConversion(value, dataType);
    }
}
