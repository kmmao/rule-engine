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

import cn.ruleengine.core.decisiontable.DecisionTable;
import cn.ruleengine.core.exception.EngineException;
import cn.ruleengine.core.listener.DecisionTableExecuteListener;
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
 *
 * @author 丁乾文
 * @create 2020/12/27
 * @since 1.0.0
 */
@Slf4j
public class DecisionTableEngine implements Engine {

    /**
     * 启动时加载的决策表
     */
    private Map<String, Map<String, DecisionTable>> workspaceMap = new ConcurrentHashMap<>();

    /**
     * 规则引擎运行所需的参数
     */
    @Setter
    private RuleEngineConfiguration configuration;


    /**
     * 可传入配置信息，包括规则监听器，决策表、规则变量...
     *
     * @param configuration 决策表引擎运行所需配置参数
     */
    public DecisionTableEngine(@NonNull RuleEngineConfiguration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
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
     * 从引擎中根据决策表code查询一个决策表
     *
     * @param workspaceCode 工作空间code
     * @param code          决策表code
     * @return DecisionTable
     */
    public DecisionTable getDecisionTable(String workspaceCode, String code) {
        Objects.requireNonNull(workspaceCode);
        Objects.requireNonNull(code);
        Map<String, DecisionTable> workspaceMap = this.workspaceMap.get(workspaceCode);
        if (workspaceMap == null) {
            throw new EngineException("Can't find this workspace：" + workspaceCode);
        }
        return workspaceMap.get(code);
    }

    /**
     * 执行决策表
     *
     * @param input             输入参数
     * @param workspaceCode     工作空间code
     * @param decisionTableCode 决策表code
     * @return OutPut
     */
    @Override
    public OutPut execute(@NonNull Input input, @NonNull String workspaceCode, @NonNull String decisionTableCode) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(workspaceCode);
        Objects.requireNonNull(decisionTableCode);
        DecisionTable decisionTable = this.getDecisionTable(workspaceCode, decisionTableCode);
        if (decisionTable == null) {
            throw new EngineException("no decision table:{}", decisionTableCode);
        }
        log.info("开始执行决策表:{}", decisionTable.getCode());
        DecisionTableExecuteListener listener = this.configuration.getDecisionTableExecuteListener();
        listener.before(decisionTable, input);
        try {
            List<Object> actions = decisionTable.execute(input, this.configuration);
            DefaultOutPut outPut = new DefaultOutPut(actions);
            listener.after(decisionTable, input, outPut);
            return outPut;
        } catch (Exception exception) {
            listener.onException(decisionTable, input, exception);
            throw exception;
        }
    }


    /**
     * 是否存在某决策表
     *
     * @param code 决策表code
     * @return true存在
     */
    @Override
    public boolean isExists(String workspaceCode, String code) {
        if (workspaceCode == null || code == null) {
            return false;
        }
        if (!this.workspaceMap.containsKey(workspaceCode)) {
            return false;
        }
        return this.workspaceMap.get(workspaceCode).containsKey(code);
    }

    /**
     * 添加决策表
     *
     * @param decisionTable 决策表配置信息
     */
    public synchronized void addDecisionTable(DecisionTable decisionTable) {
        Objects.requireNonNull(decisionTable);
        String workspaceCode = Objects.requireNonNull(decisionTable.getWorkspaceCode());
        String ruleCode = Objects.requireNonNull(decisionTable.getCode());
        if (!this.workspaceMap.containsKey(workspaceCode)) {
            this.workspaceMap.put(workspaceCode, new ConcurrentHashMap<>());
        }
        this.workspaceMap.get(workspaceCode).put(ruleCode, decisionTable);
    }

    /**
     * 添加多个决策表
     *
     * @param decisionTables 决策表配置信息列表
     */
    public void addMultipleDecisionTable(@NonNull List<DecisionTable> decisionTables) {
        Objects.requireNonNull(decisionTables);
        decisionTables.forEach(this::addDecisionTable);
    }

    /**
     * 从决策表引擎删除一个决策表
     *
     * @param code 决策表code
     */
    public void removeDecisionTable(String workspaceCode, @NonNull String code) {
        if (this.workspaceMap.containsKey(workspaceCode)) {
            this.workspaceMap.get(workspaceCode).remove(code);
        }
    }

    /**
     * 销毁决策表引擎
     */
    @Override
    public void close() {
        this.workspaceMap.clear();
        log.info("The decision table engine has been destroyed");
    }

}
