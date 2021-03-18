package cn.ruleengine.client;

import cn.ruleengine.client.exception.ExecuteException;
import cn.ruleengine.client.exception.ValidException;
import cn.ruleengine.client.fegin.BaseInterface;
import cn.ruleengine.client.model.BatchSymbol;
import cn.ruleengine.client.model.ElementField;
import cn.ruleengine.client.model.Model;
import cn.ruleengine.client.param.BatchParam;
import cn.ruleengine.client.param.ExecuteParam;
import cn.ruleengine.client.param.IsExistsParam;
import cn.ruleengine.client.result.*;
import lombok.Getter;
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
 * @create 2020-12-29 16:12
 * @since 1.0.0
 */
@Slf4j
public class Executor {

    @Getter
    private final RuleEngineProperties ruleEngineProperties;
    @Getter
    private final BaseInterface baseInterface;

    public Executor(RuleEngineProperties ruleEngineProperties, BaseInterface baseInterface) {
        this.ruleEngineProperties = ruleEngineProperties;
        this.baseInterface = baseInterface;
    }

    /**
     * 调用规则引擎中的规则
     *
     * @param code  规则code
     * @param input 规则参数
     * @return 规则结果
     */
    public Output execute(@NonNull String code, @NonNull Map<String, Object> input) {
        Objects.requireNonNull(code);
        Objects.requireNonNull(input);
        ExecuteParam executeParam = new ExecuteParam();
        executeParam.setCode(code);
        executeParam.setWorkspaceCode(this.ruleEngineProperties.getWorkspaceCode());
        executeParam.setAccessKeyId(this.ruleEngineProperties.getAccessKeyId());
        executeParam.setAccessKeySecret(this.ruleEngineProperties.getAccessKeySecret());
        executeParam.setParam(input);
        log.info("execute param is " + executeParam);
        ExecuteResult result = this.baseInterface.execute(executeParam);
        log.info("execute result is " + result);
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
     * @see Model
     */
    @SneakyThrows
    public Output execute(@NonNull Object model) {
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
        return this.execute(this.getCode(model), input);
    }

    /**
     * 校验规则model
     *
     * @param model 规则model
     */
    private void validRuleModel(Object model) {
        Objects.requireNonNull(model);
        if (!model.getClass().isAnnotationPresent(Model.class)) {
            throw new ValidException("%s 找不到Model注解", model.getClass());
        }
    }

    /**
     * 解析获取规则code
     *
     * @param model 规则model
     * @return 规则code
     */
    @SneakyThrows
    private String getCode(Object model) {
        Model ruleModel = model.getClass().getAnnotation(Model.class);
        String code = ruleModel.code();
        if (StringUtils.isEmpty(code)) {
            code = model.getClass().getSimpleName();
        }
        return code;
    }


    /**
     * 引擎中是否存在此规则
     *
     * @param code 规则code
     * @return true 存在
     */
    public boolean isExists(String code) {
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        IsExistsParam existsParam = new IsExistsParam();
        existsParam.setCode(code);
        existsParam.setWorkspaceCode(this.ruleEngineProperties.getWorkspaceCode());
        existsParam.setAccessKeyId(this.ruleEngineProperties.getAccessKeyId());
        existsParam.setAccessKeySecret(this.ruleEngineProperties.getAccessKeySecret());
        log.info("isExists param is " + existsParam);
        IsExistsResult result = this.baseInterface.isExists(existsParam);
        log.info("isExists result is " + result);
        if (!result.isSuccess()) {
            throw new ExecuteException(result.getMessage());
        }
        return result.getData();
    }

    /**
     * 批量执行规则
     *
     * @param models 规则执行信息，规则code以及规则入参
     * @return BatchOutput
     * @see BatchSymbol 标记规则使用，防止传入规则与规则输出结果顺序错误时,作用在属性上
     */
    @SneakyThrows
    public List<BatchOutput> batchExecute(@NonNull List<Object> models) {
        return this.batchExecute(100, -1L, models);
    }

    /**
     * 批量执行规则
     *
     * @param threadSegNumber 指定一个线程处理多少规则
     * @param timeout         执行超时时间，-1永不超时
     * @param models          规则执行信息，规则code以及规则入参
     * @return BatchOutput
     * @see BatchSymbol 标记规则使用，防止传入规则与规则输出结果顺序错误时,作用在属性上
     */
    public List<BatchOutput> batchExecute(@NonNull Integer threadSegNumber, @NonNull Long timeout, @NonNull List<Object> models) {
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
        // 解析模型为可执行数据
        List<BatchParam.ExecuteInfo> executeInfos = this.processModels(models);
        batchParam.setExecuteInfos(executeInfos);
        log.info("batchExecute param is " + batchParam);
        BatchExecuteResult result = this.baseInterface.batchExecute(batchParam);
        log.info("batchExecute result is " + result);
        if (!result.isSuccess()) {
            throw new ExecuteException(result.getMessage());
        }
        return result.getData();
    }

    /**
     * 解析模型数据
     *
     * @param models model
     * @return list
     */
    @SneakyThrows
    private List<BatchParam.ExecuteInfo> processModels(List<Object> models) {
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
            executeInfo.setCode(this.getCode(model));
            executeInfo.setParam(param);
            executeInfos.add(executeInfo);
        }
        return executeInfos;
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
