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

import com.engine.core.cache.DefaultFunctionCache;
import com.engine.core.cache.FunctionCache;
import com.engine.core.rule.DefaultRuleListener;
import com.engine.core.rule.RuleListener;
import lombok.Data;
import org.springframework.lang.NonNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/14
 * @since 1.0.0
 */
@Data
public class Configuration {

    /**
     * 规则执行监听器,可以动态的在规则调用之前或之后对一些规则进行特殊处理
     */
    @NonNull
    private RuleListener ruleListener = new DefaultRuleListener();

    /**
     * 规则函数缓存实现类
     */
    @NonNull
    private FunctionCache functionCache = new DefaultFunctionCache();

    /**
     * 规则引擎变量
     */
    @NonNull
    private EngineVariable engineVariable = new EngineVariable();

}
