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
package cn.ruleengine.core;


import cn.ruleengine.core.exception.EngineException;
import cn.ruleengine.core.listener.ExecuteListener;
import cn.ruleengine.core.monitor.GeneralRuleMonitorProxy;
import cn.ruleengine.core.rule.GeneralRule;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 把规则引擎执行流程反抽象为写程序
 * 程序设计到变量，以及等等数据方法
 *
 * @author dingqianwen
 * @date 2020/3/2
 * @since 1.0.0
 */
@Slf4j
public class GeneralRuleEngine implements Engine {


    /**
     * 启动时加载的规则
     */
    private Map<String, Map<String, GeneralRule>> workspaceMap = new ConcurrentHashMap<>();

    /**
     * 规则引擎运行所需的参数
     */
    @Setter
    private RuleEngineConfiguration configuration;

    /**
     * 可传入配置信息，包括规则监听器，规则变量...
     *
     * @param configuration 规则引擎运行所需配置参数
     */
    public GeneralRuleEngine(@NonNull RuleEngineConfiguration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
    }


    /**
     * 从引擎中根据规则code查询一个规则
     *
     * @param workspaceCode 工作空间code
     * @param ruleCode      规则code
     * @return Rule
     */
    public GeneralRule getGeneralRule(String workspaceCode, String ruleCode) {
        Objects.requireNonNull(workspaceCode);
        Objects.requireNonNull(ruleCode);
        Map<String, GeneralRule> workspaceMap = this.workspaceMap.get(workspaceCode);
        if (workspaceMap == null) {
            throw new EngineException("Can't find this workspace：" + workspaceCode);
        }
        return workspaceMap.get(ruleCode);
    }

    /**
     * 获取规则引擎运行所需的参数
     *
     * @return RuleEngineConfiguration
     */
    @Override
    public RuleEngineConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
     * 规则执行，当条件全部成立时，返回规则执行结果{@link GeneralRule#getActionValue()}
     * 否则查看是否存在默认结果，存在返回默认结果{@link GeneralRule#getDefaultActionValue()}，否则返回null
     *
     * @param ruleCode 规则Code
     * @return 规则执行结果
     */
    @Override
    public OutPut execute(@NonNull Input input, @NonNull String workspaceCode, @NonNull String ruleCode) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(workspaceCode);
        Objects.requireNonNull(ruleCode);
        GeneralRule generalRule = this.getGeneralRule(workspaceCode, ruleCode);
        if (generalRule == null) {
            throw new EngineException("no rule:{}", ruleCode);
        }
        log.info("开始执行规则:{}", generalRule.getCode());
        ExecuteListener<GeneralRule> listener = this.configuration.getGeneralRuleListener();
        listener.before(generalRule, input);
        try {
            Object action = generalRule.execute(input, this.configuration);
            DefaultOutPut outPut = new DefaultOutPut(action);
            listener.after(generalRule, input, outPut);
            return outPut;
        } catch (Exception exception) {
            listener.onException(generalRule, input, exception);
            throw exception;
        }
    }

    /**
     * 是否存在某规则
     *
     * @param ruleCode 规则code
     * @return true存在
     */
    @Override
    public boolean isExists(String workspaceCode, String ruleCode) {
        if (workspaceCode == null || ruleCode == null) {
            return false;
        }
        if (!this.workspaceMap.containsKey(workspaceCode)) {
            return false;
        }
        return this.workspaceMap.get(workspaceCode).containsKey(ruleCode);
    }

    /**
     * 添加规则
     *
     * @param generalRule 规则配置信息
     */
    public synchronized void addGeneralRule(@NonNull GeneralRule generalRule) {
        Objects.requireNonNull(generalRule);
        String workspaceCode = Objects.requireNonNull(generalRule.getWorkspaceCode());
        String ruleCode = Objects.requireNonNull(generalRule.getCode());
        if (!this.workspaceMap.containsKey(workspaceCode)) {
            this.workspaceMap.put(workspaceCode, new ConcurrentHashMap<>());
        }
        // 如果开启监控，返回一个被代理的规则对象
        if (generalRule.isEnableMonitor()) {
            GeneralRuleMonitorProxy ruleMonitorProxy = new GeneralRuleMonitorProxy();
            generalRule = ruleMonitorProxy.getRuleProxy(generalRule);
        }
        this.workspaceMap.get(workspaceCode).put(ruleCode, generalRule);
    }

    /**
     * 添加多个规则
     *
     * @param rules 规则配置信息列表
     */
    public void addMultipleGeneralRule(@NonNull List<GeneralRule> rules) {
        Objects.requireNonNull(rules);
        rules.forEach(this::addGeneralRule);
    }

    /**
     * 从规则引擎删除一个规则
     *
     * @param ruleCode 规则code
     */
    public void remove(String workspaceCode, @NonNull String ruleCode) {
        if (this.workspaceMap.containsKey(workspaceCode)) {
            this.workspaceMap.get(workspaceCode).remove(ruleCode);
        }
    }

    /**
     * 销毁规则引擎
     */
    @Override
    public void close() {
        this.workspaceMap.clear();
        log.info("The rules engine has been destroyed");
    }

}
