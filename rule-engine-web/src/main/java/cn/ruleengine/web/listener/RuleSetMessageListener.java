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

import cn.ruleengine.core.RuleSetEngine;
import cn.ruleengine.web.config.rabbit.RabbitTopicConfig;
import cn.ruleengine.web.listener.body.RuleSetMessageBody;
import cn.ruleengine.web.service.ruleset.RuleSetPublishService;
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
public class RuleSetMessageListener {

    @Resource
    private RuleSetEngine ruleSetEngine;
    @Resource
    private RuleSetPublishService ruleSetPublishService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(),
            exchange = @Exchange(value = RabbitTopicConfig.RULE_SET_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitTopicConfig.RULE_SET_TOPIC_ROUTING_KEY)
    )
    public void message(RuleSetMessageBody ruleSetMessageBody) {
        log.info("规则集消息：{}", ruleSetMessageBody);
        String workspaceCode = ruleSetMessageBody.getWorkspaceCode();
        String ruleCode = ruleSetMessageBody.getRuleSetCode();
        switch (ruleSetMessageBody.getType()) {
            case UPDATE:
                log.info("开始更新规则集：{}", ruleCode);
                this.ruleSetEngine.addRuleSet(ruleSetPublishService.getPublishRuleSet(workspaceCode, ruleCode));
                break;
            case LOAD:
                log.info("开始加载规则集：{}", ruleCode);
                this.ruleSetEngine.addRuleSet(ruleSetPublishService.getPublishRuleSet(workspaceCode, ruleCode));
                break;
            case REMOVE:
                log.info("开始移除规则集：{}", ruleCode);
                this.ruleSetEngine.remove(workspaceCode, ruleCode);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + ruleSetMessageBody.getType());
        }
    }
}
