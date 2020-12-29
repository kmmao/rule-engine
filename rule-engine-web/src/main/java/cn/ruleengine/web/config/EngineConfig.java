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

import cn.ruleengine.core.*;
import cn.ruleengine.web.service.decisiontable.DecisionTablePublishService;
import cn.ruleengine.core.cache.DefaultFunctionCache;

import cn.ruleengine.web.service.simplerule.SimpleRulePublishService;
import cn.ruleengine.web.service.VariableResolveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;

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
    private SimpleRulePublishService rulePublishService;
    @Resource
    private DecisionTablePublishService decisionTablePublishService;

    /**
     * 规则引擎配置
     *
     * @return RuleEngineConfiguration
     */
    @Bean(destroyMethod = "close")
    public RuleEngineConfiguration ruleEngineConfiguration() {
        RuleEngineConfiguration configuration = new RuleEngineConfiguration();
        configuration.getEngineVariable().addMultipleVariable(this.variableResolveService.getAllVariable());
        configuration.setFunctionCache(new DefaultFunctionCache(1000));
        return configuration;
    }

    /**
     * 规则引擎
     *
     * @return engine
     */
    @Primary
    @Bean(destroyMethod = "close")
    public Engine ruleEngine(RuleEngineConfiguration ruleEngineConfiguration) {
        log.info("开始初始化规则引擎");
        SimpleRuleEngine ruleEngine = new SimpleRuleEngine(ruleEngineConfiguration);
        ruleEngine.addMultipleSimpleRule(this.rulePublishService.getAllPublishSimpleRule());
        log.info("规则引擎初始化完毕");
        return ruleEngine;
    }

    /**
     * 决策表引擎
     *
     * @return engine
     */
    @Bean(destroyMethod = "close")
    public Engine decisionTableEngine(RuleEngineConfiguration ruleEngineConfiguration) {
        log.info("开始初始化决策表引擎");
        DecisionTableEngine ruleEngine = new DecisionTableEngine(ruleEngineConfiguration);
        ruleEngine.addMultipleDecisionTable(this.decisionTablePublishService.getAllPublishDecisionTable());
        log.info("决策表引擎初始化完毕");
        return ruleEngine;
    }

    /**
     * 规则集引擎
     *
     * @return engine
     */
    @Bean(destroyMethod = "close")
    public Engine ruleSetEngine(RuleEngineConfiguration ruleEngineConfiguration) {
        log.info("开始初始化规则集引擎");
        RuleSetEngine ruleSetEngine = new RuleSetEngine(ruleEngineConfiguration);
        ruleSetEngine.addMultipleRuleSet(new ArrayList<>());
        log.info("规则集引擎初始化完毕");
        return ruleSetEngine;
    }

}
