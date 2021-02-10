package cn.ruleengine.web.listener;

import cn.ruleengine.web.config.rabbit.RabbitQueueConfig;
import cn.ruleengine.web.config.rabbit.RabbitTopicConfig;
import cn.ruleengine.web.listener.body.DecisionTableMessageBody;
import cn.ruleengine.web.listener.body.GeneralRuleMessageBody;
import cn.ruleengine.web.listener.body.RuleSetMessageBody;
import cn.ruleengine.web.listener.body.VariableMessageBody;
import cn.ruleengine.web.listener.event.*;
import cn.ruleengine.web.store.entity.RuleEngineSystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 默认使用rabbit，前提是配置了，没有的话不会通知集群其他规则重新加载
 * <p>
 * 集群时，规则发布之类的没必要集成kafka，只要能保证消息不丢都可以
 * <p>
 * 如果只启动一台服务，没必要配置rabbit
 * <p>
 * 此处可以使用SpringCloud Stream
 *
 * @author 丁乾文
 * @create 2020-12-23 17:50
 * @since 1.0.0
 */
@Slf4j
@Component
public class EventPublisherListener {


    @Resource
    private ApplicationContext applicationContext;

    private RabbitTemplate rabbitTemplate;

    @Autowired(required = false)
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 事物结束后，发送mq消息通知需要加载或者移出的规则
     *
     * @param generalRuleEvent 规则事件
     * @see GeneralRuleMessageListener
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = GeneralRuleEvent.class)
    public void generalRuleEvent(GeneralRuleEvent generalRuleEvent) {
        GeneralRuleMessageBody ruleMessageBody = generalRuleEvent.getRuleMessageBody();
        if (this.rabbitTemplate != null) {
            this.rabbitTemplate.convertAndSend(RabbitTopicConfig.RULE_EXCHANGE, RabbitTopicConfig.RULE_TOPIC_ROUTING_KEY, ruleMessageBody);
        } else {
            GeneralRuleMessageListener messageListener = this.applicationContext.getBean(GeneralRuleMessageListener.class);
            messageListener.message(ruleMessageBody);
        }
    }

    /**
     * 事物结束后，对规则集的操作
     *
     * @param ruleSetEvent 规则集事件
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = RuleSetEvent.class)
    public void ruleSetEvent(RuleSetEvent ruleSetEvent) {
        RuleSetMessageBody ruleSetMessageBody = ruleSetEvent.getRuleSetMessageBody();
        if (this.rabbitTemplate != null) {
            this.rabbitTemplate.convertAndSend(RabbitTopicConfig.RULE_SET_EXCHANGE, RabbitTopicConfig.RULE_SET_TOPIC_ROUTING_KEY, ruleSetMessageBody);
        } else {
            RuleSetMessageListener messageListener = this.applicationContext.getBean(RuleSetMessageListener.class);
            messageListener.message(ruleSetMessageBody);
        }
    }

    /**
     * 事物结束后，对决策表的操作
     *
     * @param decisionTableEvent 决策表事件
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = DecisionTableEvent.class)
    public void decisionTableEvent(DecisionTableEvent decisionTableEvent) {
        DecisionTableMessageBody decisionTableMessageBody = decisionTableEvent.getDecisionTableMessageBody();
        if (this.rabbitTemplate != null) {
            this.rabbitTemplate.convertAndSend(RabbitTopicConfig.DECISION_TABLE_EXCHANGE, RabbitTopicConfig.DECISION_TABLE_TOPIC_ROUTING_KEY, decisionTableMessageBody);
        } else {
            DecisionTableMessageListener messageListener = this.applicationContext.getBean(DecisionTableMessageListener.class);
            messageListener.message(decisionTableMessageBody);
        }
    }

    /**
     * 事物结束后，对变量的操作
     *
     * @param variableEvent 变量事件
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = VariableEvent.class)
    public void variableEvent(VariableEvent variableEvent) {
        VariableMessageBody variableMessageBody = variableEvent.getVariableMessageBody();
        if (this.rabbitTemplate != null) {
            this.rabbitTemplate.convertAndSend(RabbitTopicConfig.VAR_EXCHANGE, RabbitTopicConfig.VAR_TOPIC_ROUTING_KEY, variableMessageBody);
        } else {
            VariableMessageListener messageListener = this.applicationContext.getBean(VariableMessageListener.class);
            messageListener.message(variableMessageBody);
        }
    }

    /**
     * 如果配置了rabbit 则通过rabbit发送，否则直接异步处理
     *
     * @param systemLogEvent 日志消息
     */
    @Async
    @EventListener(SystemLogEvent.class)
    public void systemLogEvent(SystemLogEvent systemLogEvent) {
        RuleEngineSystemLog ruleEngineSystemLog = systemLogEvent.getRuleEngineSystemLog();
        if (this.rabbitTemplate != null) {
            this.rabbitTemplate.convertAndSend(RabbitQueueConfig.SYSTEM_LOG_QUEUE, ruleEngineSystemLog);
        } else {
            SystemLogMessageListener messageListener = this.applicationContext.getBean(SystemLogMessageListener.class);
            messageListener.message(ruleEngineSystemLog);
        }
    }

}
