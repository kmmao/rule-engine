/**
 * Copyright @2020 dingqianwen
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
package com.engine.core;


import com.engine.web.BaseTest;
import com.engine.web.config.rabbit.RabbitTopicConfig;
import com.engine.web.vo.rule.RuleMessageVo;
import com.engine.web.vo.variable.VariableMessageVo;
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
        RuleMessageVo ruleMessageVo = new RuleMessageVo();
        ruleMessageVo.setType(RuleMessageVo.Type.LOAD);
        ruleMessageVo.setRuleCode("test");
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.RULE_EXCHANGE, RabbitTopicConfig.RULE_TOPIC_ROUTING_KEY, ruleMessageVo);
    }

    @Test
    public void reloadVar() {
        VariableMessageVo variableMessageVo = new VariableMessageVo();
        variableMessageVo.setType(VariableMessageVo.Type.LOAD);
        variableMessageVo.setId(1);
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.VAR_EXCHANGE, RabbitTopicConfig.VAR_TOPIC_ROUTING_KEY, variableMessageVo);
    }
}
