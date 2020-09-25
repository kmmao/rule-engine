package com.engine.web.message;

import com.engine.core.DefaultEngine;
import com.engine.core.rule.Rule;
import com.engine.web.service.RulePublishService;
import com.engine.web.service.RuleResolveService;
import com.engine.web.config.rabbit.RabbitTopicConfig;
import com.engine.web.store.entity.RuleEngineRulePublish;
import com.engine.web.store.manager.RuleEngineRulePublishManager;
import com.engine.web.vo.rule.RuleMessageVo;
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
public class RuleMessage {

    @Resource
    private DefaultEngine defaultEngine;
    @Resource
    private RulePublishService rulePublishService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(),
            exchange = @Exchange(value = RabbitTopicConfig.RULE_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitTopicConfig.RULE_TOPIC_ROUTING_KEY)
    )
    public void message(RuleMessageVo ruleMessageVo) {
        log.info("规则消息：{}", ruleMessageVo);
        switch (ruleMessageVo.getType()) {
            case UPDATE:
                log.info("开始更新规则：{}", ruleMessageVo.getRuleCode());
                defaultEngine.addRule(rulePublishService.getPublishRuleByCode(ruleMessageVo.getRuleCode()));
                break;
            case LOAD:
                log.info("开始加载规则：{}", ruleMessageVo.getRuleCode());
                defaultEngine.addRule(rulePublishService.getPublishRuleByCode(ruleMessageVo.getRuleCode()));
                break;
            case REMOVE:
                log.info("开始移除规则：{}", ruleMessageVo.getRuleCode());
                defaultEngine.removeRule(ruleMessageVo.getRuleCode());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + ruleMessageVo.getType());
        }
    }
}
