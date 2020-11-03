/**
 * Copyright @2020 dingqianwen
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
package com.engine.core;


import com.engine.core.cache.FunctionCache;
import com.engine.core.exception.EngineException;
import com.engine.core.rule.Rule;
import com.engine.core.rule.RuleListener;
import com.engine.core.value.Value;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.Closeable;
import java.util.Collection;
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
@NoArgsConstructor
@Slf4j
public class DefaultEngine implements Engine, Closeable {


    /**
     * 启动时加载的规则
     */
    private Map<String, Rule> ruleMap = new ConcurrentHashMap<>();

    /**
     * 规则引擎运行所需的参数
     */
    @Setter
    @Getter
    private Configuration configuration = new Configuration();

    /**
     * 可传入配置信息，包括规则监听器，规则变量...
     *
     * @param configuration 规则引擎运行所需配置参数
     */
    public DefaultEngine(@NonNull Configuration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
    }

    /**
     * 获取规则引擎变量
     *
     * @return 规则引擎变量
     */
    @Override
    public EngineVariable getEngineVariable() {
        return this.configuration.getEngineVariable();
    }

    /**
     * 规则引擎中的规则数量
     *
     * @return int
     */
    @Override
    public int size() {
        return this.ruleMap.size();
    }

    @Override
    public Rule getRule(String ruleCode) {
        return this.ruleMap.get(ruleCode);
    }

    /**
     * 设置规则运行监听器
     *
     * @param ruleListener 规则监听器
     */
    public void setRuleListener(@NonNull RuleListener ruleListener) {
        Objects.requireNonNull(ruleListener);
        this.configuration.setRuleListener(ruleListener);
    }

    /**
     * 设置函数缓存实现类
     *
     * @param functionCache 缓存实现类
     */
    public void setFunctionCache(@NonNull FunctionCache functionCache) {
        Objects.requireNonNull(functionCache);
        this.configuration.setFunctionCache(functionCache);
    }

    /**
     * 规则执行，当条件全部成立时，返回规则执行结果{@link Rule#getActionValue()}
     * 否则查看是否存在默认结果，存在返回默认结果{@link Rule#getDefaultActionValue()}，否则返回null
     *
     * @param ruleCode 规则Code
     * @return 规则执行结果
     */
    @Override
    public OutPut execute(@NonNull Input input, @NonNull String ruleCode) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(ruleCode);
        Rule rule = ruleMap.get(ruleCode);
        if (rule == null) {
            throw new EngineException("no rule:{}", ruleCode);
        }
        RuleListener listener = configuration.getRuleListener();
        listener.before(rule, input);
        try {
            Value value = rule.execute(input, configuration);
            DefaultOutPut outPut;
            if (value == null) {
                outPut = new DefaultOutPut(null, null);
            } else {
                outPut = new DefaultOutPut(value.getValue(input, configuration), value.getDataType());
            }
            listener.after(rule, input, outPut);
            return outPut;
        } catch (Exception exception) {
            listener.onException(rule, input, exception);
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
    public boolean isExistsRule(String ruleCode) {
        return this.ruleMap.containsKey(ruleCode);
    }

    /**
     * 添加规则
     *
     * @param rule 规则配置信息
     */
    @Override
    public void addRule(@NonNull Rule rule) {
        Objects.requireNonNull(rule);
        String ruleCode = Objects.requireNonNull(rule.getCode());
        this.ruleMap.put(ruleCode, rule);
    }

    /**
     * 添加多个规则
     *
     * @param rules 规则配置信息列表
     */
    @Override
    public void addMultipleRule(@NonNull List<Rule> rules) {
        Objects.requireNonNull(rules);
        rules.forEach(this::addRule);
    }

    /**
     * 从规则引擎删除一个规则
     *
     * @param ruleCode 规则code
     */
    @Override
    public void removeRule(@NonNull String ruleCode) {
        Objects.requireNonNull(ruleCode);
        this.ruleMap.remove(ruleCode);
    }

    /**
     * 销毁规则引擎
     */
    @Override
    public void close() {
        this.ruleMap.clear();
        this.configuration.getEngineVariable().close();
        this.configuration.getFunctionCache().clear();
        log.info("The rules engine has been destroyed");
    }
}
