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
package cn.ruleengine.client.fegin;

import cn.ruleengine.client.param.BatchParam;
import cn.ruleengine.client.param.ExecuteParam;
import cn.ruleengine.client.param.IsExistsParam;
import cn.ruleengine.client.result.BatchExecuteRuleResult;
import cn.ruleengine.client.result.BatchOutPut;
import cn.ruleengine.client.result.ExecuteRuleResult;
import cn.ruleengine.client.result.IsExistsResult;
import feign.Headers;
import feign.RequestLine;

import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/10
 * @since 1.0.0
 */
@Headers({"Content-Type: application/json", "Accept: application/json"})
public interface RuleInterface {

    /**
     * 调用规则引擎中的规则
     *
     * @param executeParam 入参
     * @return ExecuteRuleResult
     */
    @RequestLine("POST /ruleEngine/execute")
    ExecuteRuleResult execute(ExecuteParam executeParam);

    /**
     * 引擎中是否存在此规则
     *
     * @param existsParam 入参
     * @return IsExistsResult
     */
    @RequestLine("POST /ruleEngine/isExists")
    IsExistsResult isExists(IsExistsParam existsParam);


    /**
     * 批量执行规则
     *
     * @param batchParam 批量参数
     * @return list
     */
    @RequestLine("POST /ruleEngine/batchExecute")
    BatchExecuteRuleResult batchExecute(BatchParam batchParam);

}
