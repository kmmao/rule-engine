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

import cn.ruleengine.core.decisiontable.DecisionTable;
import cn.ruleengine.core.exception.EngineException;
import cn.ruleengine.core.listener.ExecuteListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/27
 * @since 1.0.0
 */
@Slf4j
public class DecisionTableEngine extends Engine<DecisionTable> {


    /**
     * 可传入配置信息，包括规则监听器，决策表、规则变量...
     *
     * @param configuration 决策表引擎运行所需配置参数
     */
    public DecisionTableEngine(@NonNull RuleEngineConfiguration configuration) {
        super(configuration);
    }

    /**
     * 执行决策表
     *
     * @param input             输入参数
     * @param workspaceCode     工作空间code
     * @param decisionTableCode 决策表code
     * @return Output
     */
    @Override
    public Output execute(@NonNull Input input, @NonNull String workspaceCode, @NonNull String decisionTableCode) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(workspaceCode);
        Objects.requireNonNull(decisionTableCode);
        DecisionTable decisionTable = super.get(workspaceCode, decisionTableCode);
        if (decisionTable == null) {
            throw new EngineException("no decision table:{}", decisionTableCode);
        }
        if (log.isDebugEnabled()) {
            log.debug("开始执行决策表:" + decisionTable.getCode());
        }
        ExecuteListener<DecisionTable> listener = this.getConfiguration().getDecisionTableExecuteListener();
        listener.before(decisionTable, input);
        try {
            List<Object> actions = decisionTable.execute(input, this.getConfiguration());
            if (log.isDebugEnabled()) {
                log.debug("规则集执行完毕:{},{}", decisionTable.getCode(), actions);
            }
            DefaultOutput output = new DefaultOutput(actions);
            listener.after(decisionTable, input, output);
            return output;
        } catch (Exception exception) {
            listener.onException(decisionTable, input, exception);
            throw exception;
        }
    }

    /**
     * 销毁决策表引擎
     */
    @Override
    public void close() {
        super.close();
        log.info("The decision table engine has been destroyed");
    }


}
