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
package cn.ruleengine.core.listener;

import cn.ruleengine.core.Input;
import cn.ruleengine.core.OutPut;
import cn.ruleengine.core.decisiontable.DecisionTable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/16
 * @since 1.0.0
 */
public class DefaultDecisionTableExecuteListener implements DecisionTableExecuteListener {

    @Override
    public void before(DecisionTable rule, Input input) {

    }

    @Override
    public void onException(DecisionTable rule, Input input, Exception exception) {
        log.error("决策表：{}执行异常：{}", rule.getCode(), exception);
    }

    @Override
    public void after(DecisionTable rule, Input input, OutPut outPut) {
        log.info("决策表：{}执行完毕，返回结果值：{},类型：{}", rule.getCode(), outPut.getValue(), outPut.getClassType());
    }

}
