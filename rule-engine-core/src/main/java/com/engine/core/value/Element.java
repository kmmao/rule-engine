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
import lombok.Setter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/3/2
 * @since 1.0.0
 */
@Data
public class Element implements Value {

    private Integer elementId;

    private String elementName;

    private String elementCode;

    /**
     * 值类型
     */
    private DataType dataType;

    public Element(Integer elementId, String elementName, String elementCode, DataType dataType) {
        this.elementId = elementId;
        this.elementName = elementName;
        this.elementCode = elementCode;
        this.dataType = dataType;
    }

    @Override
    public Object getValue(Input input, Configuration configuration) {
        Object value = input.get(elementCode);
        return dataConversion(value, getDataType());
    }

    @Override
    public String getValueType() {
        return this.getClass().getTypeName();
    }

    @Override
    public DataType getDataType() {
        return dataType;
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
        if (this.getDataType() != element.getDataType()) {
            return false;
        }
        return element.getElementCode().equals(this.getElementCode());
    }
}
