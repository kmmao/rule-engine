package cn.ruleengine.web.listener;

import cn.ruleengine.web.config.rabbit.RabbitTopicConfig;
import cn.ruleengine.web.listener.body.DecisionTableMessageBody;
import cn.ruleengine.web.listener.body.GeneralRuleMessageBody;
import cn.ruleengine.web.listener.body.VariableMessageBody;
import cn.ruleengine.web.listener.event.DecisionTableEvent;
import cn.ruleengine.web.listener.event.GeneralRuleEvent;
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

    /**
     * 事物结束后，发送mq消息通知需要加载或者移出的规则
     * <p>
     * 如果是单服务，可以把mq去掉GeneralRuleMessageListener类方法复制到此方法内
     *
     * @param generalRuleEvent 规则事件
     * @see GeneralRuleMessageListener
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = GeneralRuleEvent.class)
    public void generalRuleEvent(GeneralRuleEvent generalRuleEvent) {
        GeneralRuleMessageBody ruleMessageBody = generalRuleEvent.getRuleMessageBody();
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.RULE_EXCHANGE, RabbitTopicConfig.RULE_TOPIC_ROUTING_KEY, ruleMessageBody);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = DecisionTableEvent.class)
    public void decisionTableEvent(DecisionTableEvent decisionTableEvent) {
        DecisionTableMessageBody decisionTableMessageBody = decisionTableEvent.getDecisionTableMessageBody();
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.DECISION_TABLE_EXCHANGE, RabbitTopicConfig.DECISION_TABLE_TOPIC_ROUTING_KEY, decisionTableMessageBody);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = VariableEvent.class)
    public void variableEvent(VariableEvent variableEvent) {
        VariableMessageBody variableMessageBody = variableEvent.getVariableMessageBody();
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.VAR_EXCHANGE, RabbitTopicConfig.VAR_TOPIC_ROUTING_KEY, variableMessageBody);
    }

}
