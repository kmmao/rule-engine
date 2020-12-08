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
package com.engine.client;


import lombok.Data;

import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/11/7
 * @since 1.0.0
 */
@Data
public class OutPut {

    private static final int SUCCESS_CODE = 200;

    /**
     * 接口返回码
     */
    private Integer code;
    /**
     * 返回数据
     */
    private Data data;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 是否请求成功
     *
     * @return true
     */
    public boolean isSuccess() {
        return Objects.equals(this.code, SUCCESS_CODE);
    }

    public static class Data {

        private Object value;
        private String dataType;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }
    }

}
