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

import cn.ruleengine.core.cache.DefaultFunctionCache;
import cn.ruleengine.core.rule.DefaultRuleListener;
import cn.ruleengine.core.rule.RuleListener;
import cn.ruleengine.core.cache.FunctionCache;
import org.springframework.lang.NonNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/14
 * @since 1.0.0
 */
public class Configuration {

    /**
     * 规则执行监听器,可以动态的在规则调用之前或之后对一些规则进行特殊处理
     */
    private RuleListener ruleListener = new DefaultRuleListener();

    /**
     * 规则函数缓存实现类
     */
    private FunctionCache functionCache = new DefaultFunctionCache();

    /**
     * 规则引擎变量
     */
    private EngineVariable engineVariable = new EngineVariable();


    @NonNull
    public RuleListener getRuleListener() {
        return ruleListener;
    }

    public void setRuleListener(@NonNull RuleListener ruleListener) {
        this.ruleListener = ruleListener;
    }

    @NonNull
    public FunctionCache getFunctionCache() {
        return functionCache;
    }

    public void setFunctionCache(@NonNull FunctionCache functionCache) {
        this.functionCache = functionCache;
    }

    @NonNull
    public EngineVariable getEngineVariable() {
        return engineVariable;
    }

    public void setEngineVariable(@NonNull EngineVariable engineVariable) {
        this.engineVariable = engineVariable;
    }

}
