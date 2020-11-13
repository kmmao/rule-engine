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
package com.engine.core.value;

import com.engine.core.Configuration;
import com.engine.core.Input;
import com.engine.core.EngineVariable;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/3/2
 * @since 1.0.0
 */
@Data
public class Variable implements Value {

    private Integer id;

    /**
     * 变量名称
     */
    private String name;

    /**
     * 变量值类型
     */
    private DataType dataType;


    public Variable(Integer id, String name, DataType dataType) {
        this.id = id;
        this.name = name;
        this.dataType = dataType;
    }

    @Override
    public Object getValue(Input input, Configuration configuration) {
        Object value = configuration.getEngineVariable().getVariable(id);
        if (value instanceof Constant) {
            Constant constantVal = (Constant) value;
            return dataConversion(constantVal.getValue(), getDataType());
        }
        Function functionValue = (Function) value;
        return functionValue.getValue(input, configuration);
    }

    @Override
    public String getValueType() {
        return this.getClass().getTypeName();
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }
}
