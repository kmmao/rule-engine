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
package com.engine.web.config;

import com.engine.core.DefaultEngine;
import com.engine.core.Engine;
import com.engine.core.Input;
import com.engine.core.OutPut;
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
        defaultEngine.addMultipleRule(this.rulePublishService.getAllPublishRule());
        defaultEngine.getEngineVariable().addMultipleVariable(this.variableResolveService.getAllVariable());
        defaultEngine.setRuleListener(this.ruleEngineRuleListener);
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
