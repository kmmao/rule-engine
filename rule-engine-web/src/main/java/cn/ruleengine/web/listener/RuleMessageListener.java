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

import cn.ruleengine.web.config.rabbit.RabbitTopicConfig;
import cn.ruleengine.core.DefaultEngine;
import cn.ruleengine.web.listener.body.RuleMessageBody;
import cn.ruleengine.web.service.RulePublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
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
public class RuleMessageListener {

    @Resource
    private DefaultEngine defaultEngine;
    @Resource
    private RulePublishService rulePublishService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(),
            exchange = @Exchange(value = RabbitTopicConfig.RULE_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitTopicConfig.RULE_TOPIC_ROUTING_KEY)
    )
    public void message(RuleMessageBody ruleMessageBody) {
        log.info("规则消息：{}", ruleMessageBody);
        String workspaceCode = ruleMessageBody.getWorkspaceCode();
        String ruleCode = ruleMessageBody.getRuleCode();
        switch (ruleMessageBody.getType()) {
            case UPDATE:
                log.info("开始更新规则：{}", ruleMessageBody.getRuleCode());
                this.defaultEngine.addRule(rulePublishService.getPublishRule(workspaceCode, ruleCode));
                break;
            case LOAD:
                log.info("开始加载规则：{}", ruleMessageBody.getRuleCode());
                this.defaultEngine.addRule(rulePublishService.getPublishRule(workspaceCode, ruleCode));
                break;
            case REMOVE:
                log.info("开始移除规则：{}", ruleMessageBody.getRuleCode());
                this.defaultEngine.removeRule(workspaceCode, ruleCode);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + ruleMessageBody.getType());
        }
    }
}
