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
package com.engine.core.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.engine.core.Configuration;
import com.engine.core.Input;
import com.engine.core.value.Value;
import com.engine.core.condition.ConditionSet;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class Rule implements RuleParse {

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

    }

    /**
     * 规则入参
     */
    private Set<Parameter> parameters = new HashSet<>();

    public void addParameter(String name, String code, String valueType) {
        Parameter parameter = new Parameter();
        parameter.setName(name);
        parameter.setCode(code);
        parameter.setValueType(valueType);
        this.parameters.add(parameter);
    }

    @Data
    public static class Parameter {

        private String name;
        private String code;
        private String valueType;

    }

    @Nullable
    public Value execute(@NonNull Input input, @NonNull Configuration configuration) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("开始执行规则:{}", this.getCode());
            ConditionSet conditionSet = this.getConditionSet();
            //比较规则条件集
            if (!conditionSet.compare(input, configuration)) {
                Value defaultValue = this.getDefaultActionValue();
                if (Objects.nonNull(defaultValue)) {
                    log.info("条件不成立，存在默认结果，返回默认结果");
                    return defaultValue;
                }
                log.info("不存在默认结果，返回:null");
                return null;
            }
            //条件全部命中时候执行
            return this.getActionValue();
        } finally {
            log.info("引擎计算耗时:{}ms", System.currentTimeMillis() - startTime);
            // TODO: 2020/9/9 配置 耗时达到指定阀值时报警
        }
    }

    @Override
    public void fromJson(@NonNull String jsonString) {
        Rule rule = JSON.parseObject(jsonString, Rule.class, PARSER_CONFIG);
        BeanUtils.copyProperties(rule, this);
    }

    @Override
    @NonNull
    public String toJson() {
        return JSON.toJSONString(this, SerializerFeature.WriteClassName);
    }

}
