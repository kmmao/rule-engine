package com.engine.web.message;

import com.engine.web.config.rabbit.RabbitQueueConfig;
import com.engine.web.enums.ErrorLevelEnum;
import com.engine.web.store.entity.RuleEngineSystemLog;
import com.engine.web.store.manager.RuleEngineSystemLogManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/11/1
 * @since 1.0.0
 */
@Slf4j
@Component
public class SystemLogMessage {

    @Resource
    private RuleEngineSystemLogManager ruleEngineSystemLogManager;

    /**
     * 接收日志消息
     *
     * @param ruleEngineSystemLog 日志内容
     */
    @RabbitListener(queues = RabbitQueueConfig.SYSTEM_LOG_QUEUE)
    public void execute(RuleEngineSystemLog ruleEngineSystemLog) {
        log.info("接收到日志消息,准备存入数据库!");
        this.ruleEngineSystemLogManager.save(ruleEngineSystemLog);
    }
}
