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


import org.springframework.lang.NonNull;

import java.io.Closeable;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/2/29
 * @since 1.0.0
 */
public interface Engine extends Closeable {

    /**
     * 获取规则引擎运行所需的参数
     *
     * @return RuleEngineConfiguration
     */
    RuleEngineConfiguration getConfiguration();

    /**
     * 根据入参来执行引擎，并返回结果
     *
     * @param input         输入参数
     * @param workspaceCode 工作空间code
     * @param code          规则/决策表Code
     * @return 规则引擎计算的结果
     */
    OutPut execute(@NonNull Input input, @NonNull String workspaceCode, @NonNull String code);


    /**
     * 获取规则引擎变量
     *
     * @return 规则引擎变量
     */
    default EngineVariable getEngineVariable() {
        return this.getConfiguration().getEngineVariable();
    }

    /**
     * 是否存在某规则/决策表
     *
     * @param workspaceCode 工作空间code
     * @param code          规则/决策表code
     * @return true存在
     */
    boolean isExists(String workspaceCode, String code);

    /**
     * 从规则引擎删除一个规则...
     *
     * @param workspaceCode 工作空间code
     * @param code          规则/决策表code
     */
    void remove(String workspaceCode, @NonNull String code);

}
