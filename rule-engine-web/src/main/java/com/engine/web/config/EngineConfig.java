package com.engine.web.config;

import com.engine.core.*;
import com.engine.core.cache.DefaultFunctionCache;
import com.engine.core.rule.Rule;
import com.engine.core.rule.RuleListener;
import com.engine.web.enums.ErrorLevelEnum;
import com.engine.web.message.ExceptionMessage;
import com.engine.web.service.RulePublishService;
import com.engine.web.service.VariableResolveService;
import com.engine.web.store.manager.RuleEngineRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/4/7
 * @since 1.0.0
 */
@Slf4j
@Component
public class EngineConfig {

    @Resource
    private VariableResolveService variableResolveService;
    @Resource
    private RuleEngineRuleListener ruleEngineRuleListener;
    @Resource
    private RulePublishService rulePublishService;

    /**
     * 默认的引擎
     *
     * @return com.engine
     */
    @Primary
    @Bean
    public Engine defaultEngine() {
        log.info("开始初始化规则引擎");
        DefaultEngine defaultEngine = new DefaultEngine();
        defaultEngine.addMultipleRule(rulePublishService.getAllPublishRule());
        defaultEngine.getEngineVariable().addMultipleVariable(variableResolveService.getAllVariable());
        defaultEngine.setRuleListener(ruleEngineRuleListener);
        defaultEngine.setFunctionCache(new DefaultFunctionCache(1000));
        log.info("规则引擎初始化完毕");
        return defaultEngine;
    }

    @Primary
    @Configuration
    public static class RuleEngineRuleListener implements RuleListener {

        @Resource
        private RedissonClient redissonClient;
        @Resource
        private ExceptionMessage exceptionMessage;
        @Resource
        private RuleEngineRuleManager ruleEngineRuleManager;

        @Override
        public void before(Rule rule, Input input) {

        }

        @Override
        public void onException(Rule rule, Input input, Exception exception) {
            log.error("规则运行发生了异常", exception);
            Integer ruleId = rule.getId();
            // 获取此规则异常报警邮件接收人
            Rule.AbnormalAlarm abnormalAlarm = rule.getAbnormalAlarm();
            if (abnormalAlarm.getEnable()) {
                this.exceptionMessage.send(exception, ErrorLevelEnum.RUN_RULE, abnormalAlarm.getEmail());
            }
        }

        @Override
        public void after(Rule rule, Input input, OutPut outPut) {

        }
    }

}
