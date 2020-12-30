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
package cn.ruleengine.client;

import cn.ruleengine.client.fegin.DecisionTableInterface;
import cn.ruleengine.client.fegin.GeneralRuleInterface;
import cn.ruleengine.client.fegin.RuleSetInterface;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;


import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/6
 * @since 1.0.0
 */
public class RuleEngineAutoConfiguration {

    @Resource
    private RuleEngineProperties ruleEngineProperties;

    @Bean
    public RuleEngineClient ruleEngineClient() {
        return new RuleEngineClient();
    }


    private <T> T baseInterface(Class<T> clazz) {
        String baseUrl = this.ruleEngineProperties.getBaseUrl();
        RuleEngineProperties.FeignConfig feignConfig = ruleEngineProperties.getFeignConfig();
        RuleEngineProperties.FeignConfig.Request request = feignConfig.getRequest();
        RuleEngineProperties.FeignConfig.Retryer retryer = feignConfig.getRetryer();
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(request.getConnectTimeoutMillis(), request.getReadTimeoutMillis()))
                .retryer(new Retryer.Default(retryer.getPeriod(), retryer.getMaxPeriod(), retryer.getMaxAttempts()))
                .target(clazz, baseUrl);
    }

    @Primary
    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public GeneralRuleInterface generalRuleInterface() {
        return this.baseInterface(GeneralRuleInterface.class);
    }

    @Lazy
    @Bean
    @ConditionalOnMissingBean
    public DecisionTableInterface decisionTableInterface() {
        return this.baseInterface(DecisionTableInterface.class);
    }

    @Lazy
    @Bean
    @ConditionalOnMissingBean
    public RuleSetInterface ruleSetInterface() {
        return this.baseInterface(RuleSetInterface.class);
    }

}
