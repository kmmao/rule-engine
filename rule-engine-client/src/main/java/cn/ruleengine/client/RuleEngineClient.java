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

import java.util.HashMap;

import cn.ruleengine.client.model.BatchSymbol;

import java.util.ArrayList;


import javax.annotation.Resource;

import cn.ruleengine.client.exception.ValidException;
import cn.ruleengine.client.fegin.RuleInterface;
import cn.ruleengine.client.model.RuleModel;
import cn.ruleengine.client.param.BatchParam;
import cn.ruleengine.client.param.ExecuteParam;
import cn.ruleengine.client.param.IsExistsParam;
import cn.ruleengine.client.result.*;
import cn.ruleengine.client.exception.ExecuteException;
import cn.ruleengine.client.model.ElementField;
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
        log.info("rule execute param is " + executeParam);
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
        this.validRuleModel(model);
        Map<String, Object> input = new HashMap<>();
        Field[] fields = model.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
            }
            Object value = field.get(model);
            input.put(this.getElementCode(field), value);
        }
        return this.execute(this.getRuleCode(model), input);
    }

    /**
     * 校验规则model
     *
     * @param model 规则model
     */
    private void validRuleModel(Object model) {
        Objects.requireNonNull(model);
        if (!model.getClass().isAnnotationPresent(RuleModel.class)) {
            throw new ValidException("%s 找不到RuleModel注解", model.getClass());
        }
    }

    /**
     * 解析获取规则code
     *
     * @param model 规则model
     * @return 规则code
     */
    @SneakyThrows
    private String getRuleCode(Object model) {
        RuleModel ruleModel = model.getClass().getAnnotation(RuleModel.class);
        String ruleCode = ruleModel.ruleCode();
        if (StringUtils.isEmpty(ruleCode)) {
            ruleCode = model.getClass().getSimpleName();
        }
        return ruleCode;
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
        log.info("rule isExists param is " + existsParam);
        IsExistsResult result = this.ruleInterface.isExists(existsParam);
        log.info("rule isExists result is " + result);
        if (!result.isSuccess()) {
            throw new ExecuteException(result.getMessage());
        }
        return result.getData();
    }

    /**
     * 批量执行规则
     *
     * @param models 规则执行信息，规则code以及规则入参
     * @return BatchOutPut
     * @see BatchSymbol 标记规则使用，防止传入规则与规则输出结果顺序错误时,作用在属性上
     */
    @SneakyThrows
    public List<BatchOutPut> batchExecute(@NonNull List<Object> models) {
        return this.batchExecute(100, -1L, models);
    }

    /**
     * 批量执行规则
     *
     * @param threadSegNumber 指定一个线程处理多少规则
     * @param timeout         执行超时时间，-1永不超时
     * @param models          规则执行信息，规则code以及规则入参
     * @return BatchOutPut
     * @see BatchSymbol 标记规则使用，防止传入规则与规则输出结果顺序错误时,作用在属性上
     */
    @SneakyThrows
    public List<BatchOutPut> batchExecute(@NonNull Integer threadSegNumber, @NonNull Long timeout, @NonNull List<Object> models) {
        Objects.requireNonNull(threadSegNumber);
        Objects.requireNonNull(timeout);
        Objects.requireNonNull(models);
        if (models.isEmpty()) {
            return Collections.emptyList();
        }
        BatchParam batchParam = new BatchParam();
        batchParam.setWorkspaceCode(this.ruleEngineProperties.getWorkspaceCode());
        batchParam.setAccessKeyId(this.ruleEngineProperties.getAccessKeyId());
        batchParam.setAccessKeySecret(this.ruleEngineProperties.getAccessKeySecret());
        batchParam.setThreadSegNumber(threadSegNumber);
        batchParam.setTimeout(timeout);
        List<BatchParam.ExecuteInfo> executeInfos = new ArrayList<>(models.size());
        for (Object model : models) {
            this.validRuleModel(model);
            Map<String, Object> param = new HashMap<>();
            Field[] fields = model.getClass().getDeclaredFields();
            StringBuilder symbol = null;
            for (Field field : fields) {
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                Object value = field.get(model);
                if (field.isAnnotationPresent(BatchSymbol.class)) {
                    if (symbol == null) {
                        symbol = new StringBuilder();
                    }
                    symbol.append(value).append(",");
                }
                param.put(this.getElementCode(field), value);
            }
            BatchParam.ExecuteInfo executeInfo = new BatchParam.ExecuteInfo();
            if (symbol != null) {
                symbol.deleteCharAt(symbol.length() - 1);
                executeInfo.setSymbol(symbol.toString());
            }
            executeInfo.setRuleCode(this.getRuleCode(model));
            executeInfo.setParam(param);
            executeInfos.add(executeInfo);
        }
        batchParam.setExecuteInfos(executeInfos);
        log.info("rule batchExecute param is " + batchParam);
        BatchExecuteRuleResult result = this.ruleInterface.batchExecute(batchParam);
        log.info("rule batchExecute result is " + result);
        if (!result.isSuccess()) {
            throw new ExecuteException(result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取元素code
     *
     * @param field field
     * @return 元素code
     */
    private String getElementCode(Field field) {
        String elementCode = field.getName();
        if (field.isAnnotationPresent(ElementField.class)) {
            ElementField elementField = field.getAnnotation(ElementField.class);
            String code = elementField.code();
            if (!StringUtils.isEmpty(code)) {
                elementCode = code;
            }
        }
        return elementCode;
    }

}
