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


import javax.annotation.Resource;

import cn.ruleengine.client.fegin.RuleSetInterface;
import lombok.extern.slf4j.Slf4j;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/6
 * @since 1.0.0
 */
@Slf4j
public class RuleEngineClient {

    @Resource
    private RuleEngineProperties ruleEngineProperties;
    @Resource
    private DecisionTableInterface decisionTableInterface;
    @Resource
    private GeneralRuleInterface generalRuleInterface;
    @Resource
    private RuleSetInterface ruleSetInterface;

    public RuleEngineClient() {
    }

    /**
     * 决策表
     *
     * @return DecisionTable
     */
    public DecisionTable decisionTable() {
        return new DecisionTable(this.ruleEngineProperties, this.decisionTableInterface);
    }

    /**
     * 普通规则
     *
     * @return GeneralRule
     */
    public GeneralRule generalRule() {
        return new GeneralRule(this.ruleEngineProperties, this.generalRuleInterface);
    }

    /**
     * 规则集
     *
     * @return RuleSet
     */
    public RuleSet ruleSet() {
        return new RuleSet(this.ruleEngineProperties, this.ruleSetInterface);
    }


    public RuleEngineProperties getRuleEngineProperties() {
        return this.ruleEngineProperties;
    }

    public DecisionTableInterface getDecisionTableInterface() {
        return this.decisionTableInterface;
    }

    public GeneralRuleInterface getGeneralRuleInterface() {
        return this.generalRuleInterface;
    }

    public RuleSetInterface getRuleSetInterface() {
        return this.ruleSetInterface;
    }

    public void setDecisionTableInterface(DecisionTableInterface decisionTableInterface) {
        this.decisionTableInterface = decisionTableInterface;
    }

    public void setGeneralRuleInterface(GeneralRuleInterface generalRuleInterface) {
        this.generalRuleInterface = generalRuleInterface;
    }

    public void setRuleSetInterface(RuleSetInterface ruleSetInterface) {
        this.ruleSetInterface = ruleSetInterface;
    }


}
