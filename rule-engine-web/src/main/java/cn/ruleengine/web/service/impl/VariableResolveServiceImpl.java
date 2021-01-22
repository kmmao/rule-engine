package cn.ruleengine.web.service.impl;

import cn.hutool.core.collection.CollUtil;

import cn.ruleengine.core.value.Function;
import cn.ruleengine.core.value.Value;
import cn.ruleengine.core.value.ValueType;
import cn.ruleengine.core.value.VariableType;
import cn.ruleengine.web.service.ValueResolve;
import cn.ruleengine.web.service.VariableResolveService;
import cn.ruleengine.web.store.entity.RuleEngineElement;
import cn.ruleengine.web.store.entity.RuleEngineFunction;
import cn.ruleengine.web.store.entity.RuleEngineFunctionValue;
import cn.ruleengine.web.store.entity.RuleEngineVariable;
import cn.ruleengine.web.store.manager.RuleEngineElementManager;
import cn.ruleengine.web.store.manager.RuleEngineFunctionManager;
import cn.ruleengine.web.store.manager.RuleEngineFunctionValueManager;
import cn.ruleengine.web.store.manager.RuleEngineVariableManager;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/17
 * @since 1.0.0
 */
@Slf4j
@Service
public class VariableResolveServiceImpl implements VariableResolveService {

    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineFunctionValueManager ruleEngineFunctionValueManager;
    @Resource
    private RuleEngineFunctionManager ruleEngineFunctionManager;
    @Resource
    private ValueResolve valueResolve;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private RuleEngineElementManager ruleEngineElementManager;

    /**
     * 获取所有的变量/函数配置信息
     *
     * @return 变量
     */
    @Override
    public Map<Integer, Value> getAllVariable() {
        log.info("开始加载规则引擎变量");
        Map<Integer, Value> maps = new HashMap<>(100);
        // 查询到所有的变量 待优化
        List<RuleEngineVariable> engineVariables = this.ruleEngineVariableManager.list();
        List<RuleEngineFunction> engineFunctions = this.ruleEngineFunctionManager.list();
        Map<Integer, RuleEngineFunction> engineFunctionMap = engineFunctions.stream().collect(Collectors.toMap(RuleEngineFunction::getId, java.util.function.Function.identity()));
        List<RuleEngineFunctionValue> engineFunctionValues = this.ruleEngineFunctionValueManager.list();
        Map<Integer, List<RuleEngineFunctionValue>> functionValueMap = engineFunctionValues.stream().collect(Collectors.groupingBy(RuleEngineFunctionValue::getVariableId));
        List<RuleEngineElement> ruleEngineElements = this.ruleEngineElementManager.list();
        Map<Integer, RuleEngineElement> engineElementMap = ruleEngineElements.stream().collect(Collectors.toMap(RuleEngineElement::getId, java.util.function.Function.identity()));
        for (RuleEngineVariable engineVariable : engineVariables) {
            try {
                Integer type = engineVariable.getType();
                if (VariableType.CONSTANT.getType().equals(type)) {
                    Value value = this.valueResolve.getValue(VariableType.CONSTANT.getType(), engineVariable.getValueType(), engineVariable.getValue(), engineElementMap);
                    maps.put(engineVariable.getId(), value);
                } else if (VariableType.FUNCTION.getType().equals(type)) {
                    maps.put(engineVariable.getId(), this.functionProcess(engineVariable, engineFunctionMap, functionValueMap, engineElementMap));
                }
            } catch (Exception e) {
                log.warn("加载变量失败，变量Id：{}", engineVariable.getId(), e);
            }
        }
        return maps;
    }


    /**
     * 根据变量获取变量/函数配置信息
     *
     * @param id 变量id
     * @return 变量
     */
    @Override
    public Value getVarById(Integer id) {
        RuleEngineVariable ruleEngineVariable = this.ruleEngineVariableManager.getById(id);
        if (Objects.equals(ruleEngineVariable.getType(), VariableType.CONSTANT.getType())) {
            return this.valueResolve.getValue(VariableType.CONSTANT.getType(), ruleEngineVariable.getValueType(), ruleEngineVariable.getValue());
        }
        RuleEngineFunction engineFunction = this.ruleEngineFunctionManager.getById(ruleEngineVariable.getValue());

        List<RuleEngineFunctionValue> functionValueList = this.ruleEngineFunctionValueManager.lambdaQuery()
                .eq(RuleEngineFunctionValue::getFunctionId, ruleEngineVariable.getValue())
                .eq(RuleEngineFunctionValue::getVariableId, ruleEngineVariable.getId()).list();

        Map<String, Value> param = new HashMap<>(10);
        if (CollUtil.isNotEmpty(functionValueList)) {
            for (RuleEngineFunctionValue engineFunctionValue : functionValueList) {
                param.put(engineFunctionValue.getParamCode(), this.valueResolve.getValue(engineFunctionValue.getType(), engineFunctionValue.getValueType(), engineFunctionValue.getValue()));
            }
        }
        Object abstractFunction = this.applicationContext.getBean(engineFunction.getExecutor());
        return new Function(engineFunction.getId(), abstractFunction, ValueType.getByValue(engineFunction.getReturnValueType()), param);

    }


    /**
     * 规则引擎函数处理
     *
     * @param ruleEngineVariable 规则函数元数据
     * @param engineFunctionMap  函数缓存数据
     * @return Function
     */
    @Override
    public Function functionProcess(RuleEngineVariable ruleEngineVariable, Map<Integer, RuleEngineFunction> engineFunctionMap, Map<Integer, List<RuleEngineFunctionValue>> functionValueMap, Map<Integer, RuleEngineElement> engineElementMap) {
        Integer functionId = Integer.valueOf(ruleEngineVariable.getValue());
        RuleEngineFunction engineFunction = engineFunctionMap.get(functionId);

        List<RuleEngineFunctionValue> functionValueList = functionValueMap.get(ruleEngineVariable.getId());
        Map<String, Value> param = new HashMap<>(10);
        if (CollUtil.isNotEmpty(functionValueList)) {
            for (RuleEngineFunctionValue engineFunctionValue : functionValueList) {
                param.put(engineFunctionValue.getParamCode(), this.valueResolve.getValue(engineFunctionValue.getType(), engineFunctionValue.getValueType(), engineFunctionValue.getValue(), engineElementMap));
            }
        }
        Object abstractFunction = this.applicationContext.getBean(engineFunction.getExecutor());
        return new Function(engineFunction.getId(), abstractFunction, ValueType.getByValue(engineFunction.getReturnValueType()), param);
    }

}
