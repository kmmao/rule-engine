package cn.ruleengine.web.message.impl;

import cn.ruleengine.web.config.rabbit.RabbitQueueConfig;
import cn.ruleengine.web.message.ISender;
import cn.ruleengine.web.message.SenderEnum;
import cn.ruleengine.web.store.entity.RuleEngineSystemLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName RabbitMQSender
 * @Description
 * @Author AaronPiUC
 * @Date 2021/1/19 19:09
 */
@Service
public class RabbitMQSender implements ISender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(RuleEngineSystemLog log) {
        rabbitTemplate.convertAndSend(RabbitQueueConfig.SYSTEM_LOG_QUEUE, log);
    }

    @Override
    public boolean verify(String condition) {
        return SenderEnum.RABBITMQ.getSenderType().equals(condition);
    }

    /**
     * 排序，数字越小，优先级越高
     *
     * @return
     */
    @Override
    public int order() {
        return 0;
    }
}
