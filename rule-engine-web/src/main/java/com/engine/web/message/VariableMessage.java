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
package com.engine.web.message;

import com.engine.core.DefaultEngine;
import com.engine.core.EngineVariable;
import com.engine.web.service.VariableResolveService;
import com.engine.web.vo.variable.VariableMessageVo;
import com.engine.web.config.rabbit.RabbitTopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
@Slf4j
@Component
public class VariableMessage {

    @Resource
    private DefaultEngine defaultEngine;
    @Resource
    private VariableResolveService variableResolveService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(),
            exchange = @Exchange(value = RabbitTopicConfig.VAR_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitTopicConfig.VAR_TOPIC_ROUTING_KEY)
    )
    public void message(VariableMessageVo variableMessageVo) {
        log.info("变量消息：{}", variableMessageVo);
        EngineVariable engineVariable = defaultEngine.getEngineVariable();
        Integer id = variableMessageVo.getId();
        switch (variableMessageVo.getType()) {
            case REMOVE:
                log.info("开始移除变量：{}", id);
                engineVariable.removeVariable(id);
                break;
            case UPDATE:
                log.info("开始更新变量：{}", id);
                engineVariable.addVariable(id, variableResolveService.getVarById(id));
                break;
            case LOAD:
                log.info("开始加载变量：{}", id);
                engineVariable.addVariable(id, variableResolveService.getVarById(id));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + variableMessageVo.getType());
        }
    }
}
