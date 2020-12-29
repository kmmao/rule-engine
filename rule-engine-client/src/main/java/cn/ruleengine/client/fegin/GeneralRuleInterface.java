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
import cn.ruleengine.client.result.BatchExecuteResult;
import cn.ruleengine.client.result.ExecuteResult;
import cn.ruleengine.client.result.IsExistsResult;
import feign.RequestLine;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/10
 * @since 1.0.0
 */
public interface GeneralRuleInterface extends BaseInterface {

    /**
     * 调用规则引擎中的规则
     *
     * @param executeParam 入参
     * @return ExecuteRuleResult
     */
    @Override
    @RequestLine("POST /ruleEngine/generalRule/execute")
    ExecuteResult execute(ExecuteParam executeParam);

    /**
     * 引擎中是否存在此规则
     *
     * @param existsParam 入参
     * @return IsExistsResult
     */
    @Override
    @RequestLine("POST /ruleEngine/generalRule/isExists")
    IsExistsResult isExists(IsExistsParam existsParam);


    /**
     * 批量执行规则
     *
     * @param batchParam 批量参数
     * @return list
     */
    @Override
    @RequestLine("POST /ruleEngine/generalRule/batchExecute")
    BatchExecuteResult batchExecute(BatchParam batchParam);

}
