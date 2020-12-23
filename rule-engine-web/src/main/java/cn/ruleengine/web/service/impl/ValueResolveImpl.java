package cn.ruleengine.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.value.*;
import cn.ruleengine.web.service.ValueResolve;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import cn.ruleengine.web.vo.common.DataCacheMap;
import cn.ruleengine.web.vo.rule.RuleCountInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
@Component
public class ValueResolveImpl implements ValueResolve {

    @Resource
    private RuleEngineElementManager ruleEngineElementManager;
    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineConditionManager ruleEngineConditionManager;
    @Resource
    private RuleEngineFunctionManager ruleEngineFunctionManager;
    @Resource
    private RuleEngineFunctionValueManager ruleEngineFunctionValueManager;

    /**
     * 解析值，变为Value
     *
     * @param type      0元素，1变量，2固定值
     * @param valueType STRING,COLLECTION,BOOLEAN,NUMBER
     * @param value     type=0则为元素id，type=2则为具体的值
     * @param cacheMap  解析数据所用的缓存数据
     * @return value
     */
    @Override
    public Value getValue(Integer type, String valueType, String value, DataCacheMap cacheMap) {
        VariableType variableTypeEnum = VariableType.getByType(type);
        switch (variableTypeEnum) {
            case ELEMENT:
                RuleEngineElement ruleEngineElement = cacheMap.getElementMap().get(Integer.valueOf(value));
                return new Element(ruleEngineElement.getId(), ruleEngineElement.getCode(), ValueType.getByValue(valueType));
            case VARIABLE:
                RuleEngineVariable engineVariable = cacheMap.getVariableMap().get(Integer.valueOf(value));
                return new Variable(engineVariable.getId(), ValueType.getByValue(valueType));
            case CONSTANT:
                return new Constant(value, ValueType.getByValue(valueType));
            default:
                throw new IllegalStateException("Unexpected value: " + variableTypeEnum);
        }
    }

    /**
     * 解析值，变为Value
     *
     * @param type      0元素，1变量，2固定值
     * @param valueType STRING,COLLECTION,BOOLEAN,NUMBER
     * @param value     type=0则为元素id，type=2则为具体的值
     * @return value
     */
    @Override
    public Value getValue(Integer type, String valueType, String value) {
        VariableType variableTypeEnum = VariableType.getByType(type);
        switch (variableTypeEnum) {
            case ELEMENT:
                RuleEngineElement ruleEngineElement = this.ruleEngineElementManager.getById(value);
                return new Element(ruleEngineElement.getId(), ruleEngineElement.getCode(), ValueType.getByValue(valueType));
            case VARIABLE:
                RuleEngineVariable engineVariable = this.ruleEngineVariableManager.getById(value);
                return new Variable(engineVariable.getId(), ValueType.getByValue(valueType));
            case CONSTANT:
                return new Constant(value, ValueType.getByValue(valueType));
            default:
                throw new IllegalStateException("Unexpected value: " + variableTypeEnum);
        }
    }

    /**
     * 获取规则/变量配置所需数据缓存
     *
     * @return CacheMap
     */
    @Override
    public DataCacheMap getCacheMap() {
        DataCacheMap cacheMap = new DataCacheMap();
        List<RuleEngineElement> ruleEngineElements = this.ruleEngineElementManager.list();
        Map<Integer, RuleEngineElement> engineElementMap = ruleEngineElements.stream().collect(Collectors.toMap(RuleEngineElement::getId, Function.identity()));
        cacheMap.setElementMap(engineElementMap);
        List<RuleEngineVariable> engineVariables = this.ruleEngineVariableManager.list();
        Map<Integer, RuleEngineVariable> engineVariableMap = engineVariables.stream().collect(Collectors.toMap(RuleEngineVariable::getId, Function.identity()));
        cacheMap.setVariableMap(engineVariableMap);

        List<RuleEngineFunction> engineFunctions = this.ruleEngineFunctionManager.list();
        Map<Integer, RuleEngineFunction> engineFunctionMap = engineFunctions.stream().collect(Collectors.toMap(RuleEngineFunction::getId, Function.identity()));
        cacheMap.setFunctionMap(engineFunctionMap);

        List<RuleEngineFunctionValue> engineFunctionValues = this.ruleEngineFunctionValueManager.list();
        Map<Integer, List<RuleEngineFunctionValue>> functionValueMap = engineFunctionValues.stream().collect(Collectors.groupingBy(RuleEngineFunctionValue::getVariableId));
        cacheMap.setFunctionValueMap(functionValueMap);
        return cacheMap;
    }


    /**
     * 获取规则/变量配置所需数据缓存
     *
     * @return CacheMap
     */
    @Override
    public DataCacheMap getCacheMap(RuleCountInfo ruleCountInfo) {
        DataCacheMap cacheMap = new DataCacheMap();
        if (ruleCountInfo == null) {
            return cacheMap;
        }
        if (CollUtil.isNotEmpty(ruleCountInfo.getElementIds())) {
            List<RuleEngineElement> ruleEngineElements = ruleEngineElementManager.lambdaQuery()
                    .in(RuleEngineElement::getId, ruleCountInfo.getElementIds())
                    .list();
            Map<Integer, RuleEngineElement> engineElementMap = ruleEngineElements.stream().collect(Collectors.toMap(RuleEngineElement::getId, Function.identity()));
            cacheMap.setElementMap(engineElementMap);
        }

        if (CollUtil.isNotEmpty(ruleCountInfo.getVariableIds())) {
            List<RuleEngineVariable> engineVariables = ruleEngineVariableManager.lambdaQuery()
                    .in(RuleEngineVariable::getId, ruleCountInfo.getVariableIds())
                    .list();
            Map<Integer, RuleEngineVariable> engineVariableMap = engineVariables.stream().collect(Collectors.toMap(RuleEngineVariable::getId, Function.identity()));
            cacheMap.setVariableMap(engineVariableMap);
        }
        if (CollUtil.isNotEmpty(ruleCountInfo.getConditionIds())) {
            List<RuleEngineCondition> engineConditions = ruleEngineConditionManager.lambdaQuery()
                    .in(RuleEngineCondition::getId, ruleCountInfo.getConditionIds())
                    .list();
            Map<Integer, RuleEngineCondition> conditionMap = engineConditions.stream().collect(Collectors.toMap(RuleEngineCondition::getId, Function.identity()));
            cacheMap.setConditionMap(conditionMap);
        }
        return cacheMap;
    }
}
