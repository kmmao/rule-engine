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


import cn.ruleengine.core.exception.EngineException;
import cn.ruleengine.core.listener.ExecuteListener;
import cn.ruleengine.core.rule.RuleSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 把规则引擎执行流程反抽象为写程序
 * 程序设计到变量，以及等等数据方法
 *
 * @author dingqianwen
 * @date 2020/3/2
 * @since 1.0.0
 */
@Slf4j
public class RuleSetEngine extends Engine<RuleSet> {


    /**
     * 可传入配置信息，包括规则监听器，规则变量...
     *
     * @param configuration 规则引擎运行所需配置参数
     */
    public RuleSetEngine(@NonNull RuleEngineConfiguration configuration) {
        super(configuration);
    }


    /**
     * 根据入参来执行引擎，并返回结果
     *
     * @param input         输入参数
     * @param workspaceCode 工作空间code
     * @param code          规则/决策表Code
     * @return 规则引擎计算的结果
     */
    @Override
    public Output execute(@NonNull Input input, @NonNull String workspaceCode, @NonNull String code) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(workspaceCode);
        Objects.requireNonNull(code);
        RuleSet ruleSet = super.get(workspaceCode, code);
        if (ruleSet == null) {
            throw new EngineException("no rule set:{}", code);
        }
        if (log.isDebugEnabled()) {
            log.debug("开始执行规则集:" + ruleSet.getCode());
        }
        ExecuteListener<RuleSet> listener = this.getConfiguration().getRuleSetListener();
        listener.before(ruleSet, input);
        try {
            Object action = ruleSet.execute(input, this.getConfiguration());
            if (log.isDebugEnabled()) {
                log.debug("规则集执行完毕:{},{}", ruleSet.getCode(), action);
            }
            DefaultOutput output = new DefaultOutput(action);
            listener.after(ruleSet, input, output);
            return output;
        } catch (Exception exception) {
            listener.onException(ruleSet, input, exception);
            throw exception;
        }
    }

    /**
     * 销毁规则引擎
     */
    @Override
    public void close() {
        super.close();
        log.info("The rule set engine has been destroyed");
    }


}
