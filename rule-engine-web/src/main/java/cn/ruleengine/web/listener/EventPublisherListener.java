package cn.ruleengine.web.listener;

import cn.ruleengine.web.config.rabbit.RabbitTopicConfig;
import cn.ruleengine.web.listener.body.RuleMessageBody;
import cn.ruleengine.web.listener.body.VariableMessageBody;
import cn.ruleengine.web.listener.event.RuleEvent;
import cn.ruleengine.web.listener.event.VariableEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020-12-23 17:50
 * @since 1.0.0
 */
@Slf4j
@Component
public class EventPublisherListener {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = RuleEvent.class)
    public void ruleEvent(RuleEvent ruleEvent) {
        RuleMessageBody ruleMessageBody = ruleEvent.getRuleMessageBody();
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.RULE_EXCHANGE, RabbitTopicConfig.RULE_TOPIC_ROUTING_KEY, ruleMessageBody);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = VariableEvent.class)
    public void variableEvent(VariableEvent variableEvent) {
        VariableMessageBody variableMessageBody = variableEvent.getVariableMessageBody();
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.VAR_EXCHANGE, RabbitTopicConfig.VAR_TOPIC_ROUTING_KEY, variableMessageBody);
    }

}
