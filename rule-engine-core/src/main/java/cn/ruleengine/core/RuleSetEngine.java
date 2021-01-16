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
import cn.ruleengine.core.rule.RuleSet;
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
public class RuleSetEngine implements Engine {


    /**
     * 启动时加载的规则
     */
    private final Map<String, Map<String, RuleSet>> workspaceMap = new ConcurrentHashMap<>();

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
    public RuleSetEngine(@NonNull RuleEngineConfiguration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
    }


    @Override
    public RuleEngineConfiguration getConfiguration() {
        return this.configuration;
    }

    public RuleSet getRuleSet(String workspaceCode, String ruleCode) {
        Objects.requireNonNull(workspaceCode);
        Objects.requireNonNull(ruleCode);
        Map<String, RuleSet> workspaceMap = this.workspaceMap.get(workspaceCode);
        if (workspaceMap == null) {
            throw new EngineException("Can't find this workspace：" + workspaceCode);
        }
        return workspaceMap.get(ruleCode);
    }

    @Override
    public OutPut execute(Input input, String workspaceCode, String code) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(workspaceCode);
        Objects.requireNonNull(code);
        RuleSet ruleSet = this.getRuleSet(workspaceCode, code);
        if (ruleSet == null) {
            throw new EngineException("no ruleSet:{}", code);
        }
        log.info("开始执行规则集:{}", ruleSet.getCode());
        ExecuteListener<RuleSet> listener = this.configuration.getRuleSetListener();
        listener.before(ruleSet, input);
        try {
            DefaultOutPut outPut = new DefaultOutPut(ruleSet.execute(input, this.configuration));
            listener.after(ruleSet, input, outPut);
            return outPut;
        } catch (Exception exception) {
            listener.onException(ruleSet, input, exception);
            throw exception;
        }
    }

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


    public synchronized void addRuleSet(RuleSet ruleSet) {
        Objects.requireNonNull(ruleSet);
        String workspaceCode = Objects.requireNonNull(ruleSet.getWorkspaceCode());
        String ruleSetCode = Objects.requireNonNull(ruleSet.getCode());
        if (!this.workspaceMap.containsKey(workspaceCode)) {
            this.workspaceMap.put(workspaceCode, new ConcurrentHashMap<>());
        }
        if (ruleSet.isEnableMonitor()) {
            // 代理这个规则集进行监控
        }
        this.workspaceMap.get(workspaceCode).put(ruleSetCode, ruleSet);
    }

    public void addMultipleRuleSet(List<RuleSet> ruleSets) {
        Objects.requireNonNull(ruleSets);
        ruleSets.forEach(this::addRuleSet);
    }

    /**
     * 销毁规则引擎
     */
    @Override
    public void close() {
        this.workspaceMap.clear();
        log.info("The rules engine has been destroyed");
    }

    /**
     * 从规则引擎删除一个规则集
     *
     * @param ruleSetCode 规则集code
     */
    public void remove(String workspaceCode, @NonNull String ruleSetCode) {
        if (this.workspaceMap.containsKey(workspaceCode)) {
            this.workspaceMap.get(workspaceCode).remove(ruleSetCode);
        }
    }
}
