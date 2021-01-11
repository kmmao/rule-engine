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
package cn.ruleengine.web.listener;

import cn.ruleengine.core.DecisionTableEngine;
import cn.ruleengine.web.config.rabbit.RabbitTopicConfig;
import cn.ruleengine.web.listener.body.DecisionTableMessageBody;
import cn.ruleengine.web.service.decisiontable.DecisionTablePublishService;
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
public class DecisionTableMessageListener {

    @Resource
    private DecisionTableEngine decisionTableEngine;
    @Resource
    private DecisionTablePublishService decisionTablePublishService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(),
            exchange = @Exchange(value = RabbitTopicConfig.DECISION_TABLE_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitTopicConfig.DECISION_TABLE_TOPIC_ROUTING_KEY)
    )
    public void message(DecisionTableMessageBody decisionTableMessageBody) {
        log.info("决策表消息：{}", decisionTableMessageBody);
        String workspaceCode = decisionTableMessageBody.getWorkspaceCode();
        String decisionTableCode = decisionTableMessageBody.getDecisionTableCode();
        switch (decisionTableMessageBody.getType()) {
            case UPDATE:
                log.info("开始更新决策表：{}", decisionTableCode);
                this.decisionTableEngine.addDecisionTable(decisionTablePublishService.getPublishDecisionTable(workspaceCode, decisionTableCode));
                break;
            case LOAD:
                log.info("开始加载决策表：{}", decisionTableCode);
                this.decisionTableEngine.addDecisionTable(decisionTablePublishService.getPublishDecisionTable(workspaceCode, decisionTableCode));
                break;
            case REMOVE:
                log.info("开始移除决策表：{}", decisionTableCode);
                this.decisionTableEngine.removeDecisionTable(workspaceCode, decisionTableCode);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + decisionTableMessageBody.getType());
        }
    }

}
