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

import cn.ruleengine.core.Configuration;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.condition.Precondition;
import cn.ruleengine.core.value.Value;
import cn.ruleengine.core.condition.ConditionSet;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.*;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/3/9
 * @since 1.0.0
 */
@Slf4j
@Data
public class Rule implements JsonParse {

    /**
     * 规则id
     */
    private Integer id;

    /**
     * 规则Code
     */
    private String code;
    /**
     * 规则名称
     */
    private String name;

    private String description;

    /**
     * 工作空间
     */
    private Integer workspaceId;
    /**
     * 工作空间code
     */
    private String workspaceCode;
    /**
     * 前提条件
     * <p>
     * 如果前提条件不满足，则直接返回默认结果
     */
    private Precondition precondition = new Precondition();
    /**
     * 当条件全部满足时候返回此规则结果
     */
    private ConditionSet conditionSet = new ConditionSet();
    /**
     * 返回结果
     */
    private Value actionValue;
    /**
     * 规则默认值
     */
    private Value defaultActionValue;
    /**
     * 是否开启监控
     */
    private boolean enableMonitor = false;

    /**
     * 规则运行发生异常，邮件接收人
     */
    private AbnormalAlarm abnormalAlarm = new AbnormalAlarm();

    @Data
    public static class AbnormalAlarm {
        /**
         * 是否启用
         */
        private Boolean enable = false;
        /**
         * 邮件接收人
         */
        private String[] email;

        /**
         * 规则执行超时阈值，默认3秒
         */
        private long timeOutThreshold = 3000;

    }

    @Data
    public static class Parameter {

        private String name;
        private String code;
        private String valueType;

    }

    /**
     * 执行规则
     *
     * @param input         入参
     * @param configuration 规则引擎配置
     * @return 规则返回值
     */
    @Nullable
    public Value execute(@NonNull Input input, @NonNull Configuration configuration) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("开始计算前提条件");
            if (this.precondition.compare(input, configuration)) {
                log.info("前提条件成立,开始计算条件集");
                // 比较规则条件集
                if (this.conditionSet.compare(input, configuration)) {
                    // 条件全部命中时候执行
                    return this.getActionValue();
                }
            }
            Value defaultValue = this.getDefaultActionValue();
            if (Objects.nonNull(defaultValue)) {
                log.info("结果未命中，存在默认结果，返回默认结果");
                return defaultValue;
            }
            log.info("结果未命中，不存在默认结果，返回:null");
            return null;
        } finally {
            long cost = System.currentTimeMillis() - startTime;
            log.info("引擎计算耗时:{}ms", cost);
            if (cost >= this.getAbnormalAlarm().getTimeOutThreshold()) {
                log.warn("警告：规则执行超过最大阈值，请检查规则配置，规则Code:{}", this.getCode());
            }
        }
    }

    /**
     * 根据rule json字符串构建一个规则
     *
     * @param jsonString rule json字符串
     * @return rule
     */
    @SneakyThrows
    public static Rule buildRule(@NonNull String jsonString) {
        return OBJECT_MAPPER.readValue(jsonString, Rule.class);
    }

    @SneakyThrows
    @Override
    public void fromJson(@NonNull String jsonString) {
        Rule rule = Rule.buildRule(jsonString);
        this.setId(rule.getId());
        this.setCode(rule.getCode());
        this.setName(rule.getName());
        this.setDescription(rule.getDescription());
        this.setWorkspaceId(rule.getWorkspaceId());
        this.setWorkspaceCode(rule.getWorkspaceCode());
        this.setConditionSet(rule.getConditionSet());
        this.setActionValue(rule.getActionValue());
        this.setDefaultActionValue(rule.getDefaultActionValue());
        this.setAbnormalAlarm(rule.getAbnormalAlarm());
        this.setEnableMonitor(rule.isEnableMonitor());
    }


}
