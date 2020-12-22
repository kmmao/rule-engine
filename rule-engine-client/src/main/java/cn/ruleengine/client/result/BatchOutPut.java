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
package cn.ruleengine.client.result;


import lombok.Data;
import org.springframework.lang.Nullable;

import java.io.Serializable;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/11/7
 * @since 1.0.0
 */
@Data
public class BatchOutPut implements Serializable {

    private static final long serialVersionUID = -6098570767368919540L;

    /**
     * 规则执行状态，是否执行成功，或者遇到了异常
     */
    private Boolean isDone = true;
    /**
     * isDone=false规则执行错误消息
     */
    private String message;

    /**
     * 标记规则使用，防止传入规则与规则输出结果顺序错误时
     * 通过此标记区分
     */
    @Nullable
    private String symbol;

    /**
     * 规则执行结果
     */
    private OutPut outPut;

}
