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
import cn.ruleengine.core.monitor.SimpleRuleMonitorProxy;
import cn.ruleengine.core.rule.Rule;
import cn.ruleengine.core.rule.SimpleRule;
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
public class SimpleRuleEngine implements Engine {


    /**
     * 启动时加载的规则
     */
    private Map<String, Map<String, SimpleRule>> workspaceMap = new ConcurrentHashMap<>();

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
    public SimpleRuleEngine(@NonNull RuleEngineConfiguration configuration) {
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
    public SimpleRule getSimpleRule(String workspaceCode, String ruleCode) {
        Objects.requireNonNull(workspaceCode);
        Objects.requireNonNull(ruleCode);
        Map<String, SimpleRule> workspaceMap = this.workspaceMap.get(workspaceCode);
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
     * 规则执行，当条件全部成立时，返回规则执行结果{@link SimpleRule#getActionValue()}
     * 否则查看是否存在默认结果，存在返回默认结果{@link SimpleRule#getDefaultActionValue()}，否则返回null
     *
     * @param ruleCode 规则Code
     * @return 规则执行结果
     */
    @Override
    public OutPut execute(@NonNull Input input, @NonNull String workspaceCode, @NonNull String ruleCode) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(workspaceCode);
        Objects.requireNonNull(ruleCode);
        SimpleRule simpleRule = this.getSimpleRule(workspaceCode, ruleCode);
        if (simpleRule == null) {
            throw new EngineException("no rule:{}", ruleCode);
        }
        log.info("开始执行规则:{}", simpleRule.getCode());
        ExecuteListener<SimpleRule> listener = this.configuration.getSimpleRuleListener();
        listener.before(simpleRule, input);
        try {
            Object action = simpleRule.execute(input, this.configuration);
            DefaultOutPut outPut = new DefaultOutPut(action);
            listener.after(simpleRule, input, outPut);
            return outPut;
        } catch (Exception exception) {
            listener.onException(simpleRule, input, exception);
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
     * @param simpleRule 规则配置信息
     */
    public synchronized void addSimpleRule(@NonNull SimpleRule simpleRule) {
        Objects.requireNonNull(simpleRule);
        String workspaceCode = Objects.requireNonNull(simpleRule.getWorkspaceCode());
        String ruleCode = Objects.requireNonNull(simpleRule.getCode());
        if (!this.workspaceMap.containsKey(workspaceCode)) {
            this.workspaceMap.put(workspaceCode, new ConcurrentHashMap<>());
        }
        // 如果开启监控，返回一个被代理的规则对象
        if (simpleRule.isEnableMonitor()) {
            SimpleRuleMonitorProxy ruleMonitorProxy = new SimpleRuleMonitorProxy();
            simpleRule = ruleMonitorProxy.getRuleProxy(simpleRule);
        }
        this.workspaceMap.get(workspaceCode).put(ruleCode, simpleRule);
    }

    /**
     * 添加多个规则
     *
     * @param rules 规则配置信息列表
     */
    public void addMultipleSimpleRule(@NonNull List<SimpleRule> rules) {
        Objects.requireNonNull(rules);
        rules.forEach(this::addSimpleRule);
    }

    /**
     * 从规则引擎删除一个规则
     *
     * @param ruleCode 规则code
     */
    public void removeSimpleRule(String workspaceCode, @NonNull String ruleCode) {
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
