package cn.ruleengine.web.service.impl;

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
    @Resource
    private RuleEngineFunctionManager ruleEngineFunctionManager;
    @Resource
    private RuleEngineFunctionValueManager ruleEngineFunctionValueManager;

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

}
