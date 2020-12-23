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
package cn.ruleengine.web.config;


import cn.ruleengine.web.BaseTest;
import cn.ruleengine.web.config.rabbit.RabbitTopicConfig;
import cn.ruleengine.web.listener.body.RuleMessageBody;
import cn.ruleengine.web.listener.body.VariableMessageBody;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
public class RabbitMqTest extends BaseTest {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void reloadAll() {
        log.info("开始发送消息");
        RuleMessageBody ruleMessageBody = new RuleMessageBody();
        ruleMessageBody.setType(RuleMessageBody.Type.LOAD);
        ruleMessageBody.setRuleCode("test");
        ruleMessageBody.setWorkspaceCode("default");
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.RULE_EXCHANGE, RabbitTopicConfig.RULE_TOPIC_ROUTING_KEY, ruleMessageBody);
    }

    @Test
    public void reloadVar() {
        VariableMessageBody variableMessageBody = new VariableMessageBody();
        variableMessageBody.setType(VariableMessageBody.Type.LOAD);
        variableMessageBody.setId(1);
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.VAR_EXCHANGE, RabbitTopicConfig.VAR_TOPIC_ROUTING_KEY, variableMessageBody);
    }
}
