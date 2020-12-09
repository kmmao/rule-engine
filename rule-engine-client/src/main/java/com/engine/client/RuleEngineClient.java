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
package com.engine.client;


import javax.annotation.Resource;

import com.engine.client.exception.ExecuteException;
import com.engine.client.exception.ValidException;
import com.engine.client.model.ElementField;
import com.engine.client.model.RuleModel;
import com.engine.client.result.BatchOutPut;
import com.engine.client.result.ExecuteRuleResult;
import com.engine.client.result.IsExistsResult;
import com.engine.client.result.OutPut;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/6
 * @since 1.0.0
 */
@Slf4j
@Data
public class RuleEngineClient {

    @Resource
    private RuleEngineProperties ruleEngineProperties;
    @Resource
    private RestTemplate restTemplate;

    public RuleEngineClient() {

    }

    /**
     * 调用规则引擎中的规则
     *
     * @param ruleCode 规则code
     * @param input    规则参数
     * @return 规则结果
     */
    public OutPut execute(@NonNull String ruleCode, @NonNull Map<String, Object> input) {
        Objects.requireNonNull(ruleCode);
        Objects.requireNonNull(input);
        Map<String, Object> param = new HashMap<>(5);
        param.put("ruleCode", ruleCode);
        param.put("workspaceCode", this.ruleEngineProperties.getWorkspaceCode());
        param.put("accessKeyId", this.ruleEngineProperties.getAccessKeyId());
        param.put("accessKeySecret", this.ruleEngineProperties.getAccessKeySecret());
        param.put("param", input);
        String executeRuleUrl = this.ruleEngineProperties.getExecuteRuleUrl();
        ExecuteRuleResult result = this.restTemplate.postForObject(executeRuleUrl, param, ExecuteRuleResult.class);
        log.info("rule execute result is " + result);
        if (result == null) {
            return null;
        }
        if (!result.isSuccess()) {
            throw new ExecuteException(result.getMessage());
        }
        return result.getData();
    }

    /**
     * 根据规则模型解析调用引擎中的规则
     *
     * @param model 规则调用模型
     * @return 规则结果
     * @see RuleModel
     */
    @SneakyThrows
    public OutPut execute(@NonNull Object model) {
        Objects.requireNonNull(model);
        if (!model.getClass().isAnnotationPresent(RuleModel.class)) {
            throw new ValidException("{}找不到RuleModel注解", model.getClass());
        }
        RuleModel ruleModel = model.getClass().getAnnotation(RuleModel.class);
        String ruleCode = ruleModel.ruleCode();
        if (StringUtils.isEmpty(ruleCode)) {
            ruleCode = model.getClass().getSimpleName();
        }
        Map<String, Object> input = new HashMap<>();
        Field[] fields = model.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            String elementCode = field.getName();
            if (field.isAnnotationPresent(ElementField.class)) {
                ElementField elementField = field.getAnnotation(ElementField.class);
                String code = elementField.code();
                if (!StringUtils.isEmpty(code)) {
                    elementCode = code;
                }
            }
            Object value = field.get(model);
            input.put(elementCode, value);
        }
        return this.execute(ruleCode, input);
    }

    /**
     * 引擎中是否存在此规则
     *
     * @param ruleCode 规则code
     * @return true 存在
     */
    public boolean isExists(String ruleCode) {
        if (StringUtils.isEmpty(ruleCode)) {
            return false;
        }
        Map<String, Object> param = new HashMap<>(4);
        param.put("ruleCode", ruleCode);
        param.put("workspaceCode", this.ruleEngineProperties.getWorkspaceCode());
        param.put("accessKeyId", this.ruleEngineProperties.getAccessKeyId());
        param.put("accessKeySecret", this.ruleEngineProperties.getAccessKeySecret());
        String isExistsRuleUrl = this.ruleEngineProperties.getIsExistsRuleUrl();
        IsExistsResult result = this.restTemplate.postForObject(isExistsRuleUrl, param, IsExistsResult.class);
        if (result == null) {
            return false;
        }
        return result.getData();
    }

    /**
     * 批量执行规则
     *
     * @param batchParam 批量参数
     * @return list
     */
    public List<BatchOutPut> batchExecute(BatchParam batchParam) {
        String batchExecuteRuleUrl = this.ruleEngineProperties.getBatchExecuteRuleUrl();
        return null;
    }

}
