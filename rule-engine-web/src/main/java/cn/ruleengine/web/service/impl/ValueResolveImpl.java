package cn.ruleengine.web.service.impl;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.value.*;
import cn.ruleengine.web.service.ValueResolve;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import cn.ruleengine.web.vo.condition.ConfigValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

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

    /**
     * 解析值，变为Value
     *
     * @param type             0元素，1变量，2固定值
     * @param valueType        STRING,COLLECTION,BOOLEAN,NUMBER
     * @param value            type=0则为元素id，type=2则为具体的值
     * @param engineElementMap engineElement缓存数据
     * @return value
     */
    @Override
    public Value getValue(Integer type, String valueType, String value, Map<Integer, RuleEngineElement> engineElementMap) {
        VariableType variableTypeEnum = VariableType.getByType(type);
        switch (variableTypeEnum) {
            case ELEMENT:
                RuleEngineElement ruleEngineElement = engineElementMap.get(Integer.valueOf(value));
                return new Element(ruleEngineElement.getId(), ruleEngineElement.getCode(), ValueType.getByValue(valueType));
            case VARIABLE:
                return new Variable(Integer.valueOf(value), ValueType.getByValue(valueType));
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
                return new Variable(Integer.valueOf(value), ValueType.getByValue(valueType));
            case CONSTANT:
                return new Constant(value, ValueType.getByValue(valueType));
            default:
                throw new IllegalStateException("Unexpected value: " + variableTypeEnum);
        }
    }


    /**
     * 解析值/变量/元素/固定值
     *
     * @param cValue Value
     * @return ConfigBean.Value
     */
    @Override
    public ConfigValue getConfigValue(Value cValue) {
        ConfigValue value = new ConfigValue();
        value.setValueType(cValue.getValueType().getValue());
        if (cValue instanceof Constant) {
            value.setType(VariableType.CONSTANT.getType());
            Constant constant = (Constant) cValue;
            value.setValue(String.valueOf(constant.getValue()));
            value.setValueName(String.valueOf(constant.getValue()));
        } else if (cValue instanceof Element) {
            value.setType(VariableType.ELEMENT.getType());
            Element element = (Element) cValue;
            RuleEngineElement ruleEngineElement = this.ruleEngineElementManager.getById(element.getElementId());
            value.setValue(String.valueOf(element.getElementId()));
            value.setValueName(ruleEngineElement.getName());
        } else if (cValue instanceof Variable) {
            value.setType(VariableType.VARIABLE.getType());
            Variable variable = (Variable) cValue;
            value.setValue(String.valueOf(variable.getVariableId()));
            RuleEngineVariable engineVariable = this.ruleEngineVariableManager.getById(variable.getVariableId());
            value.setValueName(engineVariable.getName());
            if (engineVariable.getType().equals(VariableType.CONSTANT.getType())) {
                value.setVariableValue(engineVariable.getValue());
            }
        }
        return value;
    }

    /**
     * 解析值/变量/元素/固定值
     *
     * @param value     结果值/可能为变量/元素
     * @param type      变量/元素/固定值
     * @param valueType STRING/NUMBER...
     * @return Action
     */
    @Override
    public ConfigValue getConfigValue(String value, Integer type, String valueType) {
        ConfigValue configValue = new ConfigValue();
        if (Validator.isEmpty(type)) {
            return configValue;
        }
        configValue.setValueType(valueType);
        configValue.setType(type);
        if (Validator.isEmpty(value)) {
            return configValue;
        }
        if (type.equals(VariableType.ELEMENT.getType())) {
            configValue.setValueName(this.ruleEngineElementManager.getById(value).getName());
        } else if (type.equals(VariableType.VARIABLE.getType())) {
            RuleEngineVariable engineVariable = this.ruleEngineVariableManager.getById(value);
            configValue.setValueName(engineVariable.getName());
            if (engineVariable.getType().equals(VariableType.CONSTANT.getType())) {
                configValue.setVariableValue(engineVariable.getValue());
            }
        }
        configValue.setValue(value);
        return configValue;
    }

    /**
     * 如果是变量，查询到变量name，如果是元素查询到元素name
     *
     * @param type        类型 变量/元素/固定值
     * @param value       值
     * @param valueType   值类型 STRING/NUMBER...
     * @param variableMap 变量缓存
     * @param elementMap  元素缓存
     * @return ConfigValue
     */
    @Override
    public ConfigValue getConfigValue(String value, Integer type, String valueType, Map<Integer, RuleEngineVariable> variableMap, Map<Integer, RuleEngineElement> elementMap) {
        String valueName = value;
        String variableValue = null;
        if (type.equals(VariableType.ELEMENT.getType())) {
            valueName = elementMap.get(Integer.valueOf(value)).getName();
        } else if (type.equals(VariableType.VARIABLE.getType())) {
            RuleEngineVariable engineVariable = variableMap.get(Integer.valueOf(value));
            valueName = engineVariable.getName();
            if (engineVariable.getType().equals(VariableType.CONSTANT.getType())) {
                variableValue = engineVariable.getValue();
            }
        }
        ConfigValue configBeanValue = new ConfigValue();
        configBeanValue.setType(type);
        configBeanValue.setValue(value);
        configBeanValue.setValueName(valueName);
        configBeanValue.setVariableValue(variableValue);
        configBeanValue.setValueType(valueType);
        return configBeanValue;
    }

}
