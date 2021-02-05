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
package cn.ruleengine.core.rule;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.DataSupport;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.JsonParse;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.rule.strategy.RuleSetStrategy;
import cn.ruleengine.core.rule.strategy.RuleSetStrategyFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 引入规则集，改进单规则弊端
 *
 * @author 丁乾文
 * @create 2020/12/27
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class RuleSet extends DataSupport implements JsonParse {

    /**
     * 规则集
     */
    private List<Rule> rules = new ArrayList<>();

    /**
     * 默认规则
     */
    private Rule defaultRule;

    /**
     * 默认匹配所有的规则结果
     */
    private RuleSetStrategyType strategyType = RuleSetStrategyType.ALL_RULE;

    /**
     * @deprecated 后面用DataSupport中的AbnormalAlarm替代
     */
    @JsonIgnore
    @Deprecated
    private AbnormalAlarm abnormalAlarm = new AbnormalAlarm();


    public void addRule(@NonNull Rule rule) {
        Objects.requireNonNull(rule);
        this.rules.add(rule);
    }

    @Override
    @Nullable
    public Object execute(@NonNull Input input, @NonNull RuleEngineConfiguration configuration) {
        long startTime = System.currentTimeMillis();
        try {
            RuleSetStrategy ruleSetStrategy = RuleSetStrategyFactory.getInstance(this.strategyType);
            List<Object> actions = ruleSetStrategy.compute(rules, input, configuration);
            if (CollUtil.isNotEmpty(actions)) {
                return actions;
            }
            Rule defaultRule = this.getDefaultRule();
            if (Objects.nonNull(defaultRule)) {
                log.info("结果未命中，存在默认规则，返回默认规则结果");
                return defaultRule.execute(input, configuration);
            }
            log.info("结果未命中，不存在默认规则，返回:null");
            return null;
        } finally {
            long cost = System.currentTimeMillis() - startTime;
            log.info("引擎计算耗时:{}ms", cost);
            if (cost >= this.getAbnormalAlarm().getTimeOutThreshold()) {
                log.warn("警告：规则集执行超过最大阈值，请检查规则配置，规则Code:{}", this.getCode());
            }
        }
    }

    /**
     * 根据rule set json字符串构建一个规则
     *
     * @param jsonString rule json字符串
     * @return rule
     */
    @SneakyThrows
    public static RuleSet buildRuleSet(@NonNull String jsonString) {
        return OBJECT_MAPPER.readValue(jsonString, RuleSet.class);
    }

}
