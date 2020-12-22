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
package cn.ruleengine.client.param;


import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/9
 * @since 1.0.0
 */
@Data
public class BatchParam implements Serializable{

    private static final long serialVersionUID = 4617274211982307682L;

    private String workspaceCode;

    private String accessKeyId;

    private String accessKeySecret;

    /**
     * 指定一个线程处理多少规则
     */
    private Integer threadSegNumber = 100;

    /**
     * 执行超时时间，-1永不超时
     */
    private Long timeout = -1L;

    /**
     * 规则执行信息，规则code以及规则入参
     */
    private List<ExecuteInfo> executeInfos;

    @Data
    public static class ExecuteInfo implements Serializable {

        private static final long serialVersionUID = 3341164067497852607L;

        /**
         * 标记规则使用，防止传入规则与规则输出结果顺序错误时
         * 通过此标记区分
         */
        private String symbol;

        private String ruleCode;

        private Map<String, Object> param = new HashMap<>();

    }

}
