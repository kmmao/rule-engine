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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.Closeable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/2/29
 * @since 1.0.0
 */
@Slf4j
public abstract class Engine<T extends DataSupport> implements Closeable {

    /**
     * 启动时加载的规则/决策表
     */
    private final Map<String, Map<String, T>> workspaceMap = new ConcurrentHashMap<>();


    /**
     * 规则引擎运行所需的参数
     */
    @Getter
    private final RuleEngineConfiguration configuration;

    /**
     * 可传入配置信息，包括规则监听器，规则变量...
     *
     * @param configuration 规则引擎运行所需配置参数
     */
    public Engine(@NonNull RuleEngineConfiguration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
    }


    public Map<String, Map<String, T>> getWorkspaceMap() {
        return Collections.unmodifiableMap(this.workspaceMap);
    }

    /**
     * 从引擎中根据决策表code查询一个决策表/规则
     *
     * @param workspaceCode 工作空间code
     * @param ruleCode      决策表/规则code
     * @return DecisionTable
     */
    public T get(String workspaceCode, String ruleCode) {
        Objects.requireNonNull(workspaceCode);
        Objects.requireNonNull(ruleCode);
        Map<String, T> workspaceMap = this.workspaceMap.get(workspaceCode);
        if (workspaceMap == null) {
            throw new EngineException("Can't find this workspace：" + workspaceCode);
        }
        return workspaceMap.get(ruleCode);
    }

    /**
     * 根据入参来执行引擎，并返回结果
     *
     * @param input         输入参数
     * @param workspaceCode 工作空间code
     * @param code          规则/决策表Code
     * @return 规则引擎计算的结果
     */
    public abstract Output execute(@NonNull Input input, @NonNull String workspaceCode, @NonNull String code);


    /**
     * 是否存在某规则/决策表
     *
     * @param workspaceCode 工作空间code
     * @param ruleCode      规则/决策表code
     * @return true存在
     */
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
     * 添加单个
     *
     * @param dataSupport 配置信息
     */
    public synchronized void add(T dataSupport) {
        Objects.requireNonNull(dataSupport);
        String workspaceCode = Objects.requireNonNull(dataSupport.getWorkspaceCode());
        String ruleSetCode = Objects.requireNonNull(dataSupport.getCode());
        if (!this.workspaceMap.containsKey(workspaceCode)) {
            this.workspaceMap.put(workspaceCode, new ConcurrentHashMap<>());
        }
        this.workspaceMap.get(workspaceCode).put(ruleSetCode, dataSupport);
    }


    /**
     * 添加多个
     *
     * @param dataSupports 配置信息列表
     */
    public void addMultiple(List<T> dataSupports) {
        Objects.requireNonNull(dataSupports);
        dataSupports.forEach(this::add);
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

    /**
     * 销毁规则引擎
     */
    @Override
    public void close() {
        this.workspaceMap.clear();
        this.configuration.close();
    }

}
