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
package cn.ruleengine.client;


import javax.annotation.Resource;

import cn.ruleengine.client.exception.ValidException;
import cn.ruleengine.client.fegin.RuleInterface;
import cn.ruleengine.client.model.RuleModel;
import cn.ruleengine.client.param.BatchParam;
import cn.ruleengine.client.param.ExecuteParam;
import cn.ruleengine.client.param.IsExistsParam;
import cn.ruleengine.client.result.BatchOutPut;
import cn.ruleengine.client.result.ExecuteRuleResult;
import cn.ruleengine.client.result.OutPut;
import cn.ruleengine.client.exception.ExecuteException;
import cn.ruleengine.client.model.ElementField;
import cn.ruleengine.client.result.IsExistsResult;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

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
    private RuleInterface ruleInterface;

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
        ExecuteParam executeParam = new ExecuteParam();
        executeParam.setRuleCode(ruleCode);
        executeParam.setWorkspaceCode(this.ruleEngineProperties.getWorkspaceCode());
        executeParam.setAccessKeyId(this.ruleEngineProperties.getAccessKeyId());
        executeParam.setAccessKeySecret(this.ruleEngineProperties.getAccessKeySecret());
        executeParam.setParam(input);
        log.info("rule execute param is {}", executeParam);
        ExecuteRuleResult result = this.ruleInterface.execute(executeParam);
        log.info("rule execute result is " + result);
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
        IsExistsParam existsParam = new IsExistsParam();
        existsParam.setRuleCode(ruleCode);
        existsParam.setWorkspaceCode(this.ruleEngineProperties.getWorkspaceCode());
        existsParam.setAccessKeyId(this.ruleEngineProperties.getAccessKeyId());
        existsParam.setAccessKeySecret(this.ruleEngineProperties.getAccessKeySecret());
        log.info("rule isExists param is {}", existsParam);
        IsExistsResult isExistsResult = this.ruleInterface.isExists(existsParam);
        log.info("rule isExists result is " + isExistsResult);
        return isExistsResult.getData();
    }

    /**
     * 批量执行规则
     *
     * @param batchParam 批量参数
     * @return list
     */
    public List<BatchOutPut> batchExecute(BatchParam batchParam) {
        return null;
    }

}
