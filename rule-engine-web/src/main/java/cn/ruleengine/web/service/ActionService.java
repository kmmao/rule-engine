package cn.ruleengine.web.service;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.value.VariableType;
import cn.ruleengine.web.store.entity.RuleEngineVariable;
import cn.ruleengine.web.store.manager.RuleEngineElementManager;
import cn.ruleengine.web.store.manager.RuleEngineVariableManager;
import cn.ruleengine.web.vo.condition.ConfigValue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/16
 * @since 1.0.0
 */
@Service
public class ActionService {

    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineElementManager ruleEngineElementManager;

    /**
     * 解析结果/默认结果
     *
     * @param value     结果值/可能为变量/元素
     * @param type      变量/元素/固定值
     * @param valueType STRING/NUMBER...
     * @return Action
     */
    public ConfigValue getAction(String value, Integer type, String valueType) {
        ConfigValue action = new ConfigValue();
        if (Validator.isEmpty(type)) {
            return action;
        }
        action.setValueType(valueType);
        action.setType(type);
        if (Validator.isEmpty(value)) {
            return action;
        }
        if (type.equals(VariableType.ELEMENT.getType())) {
            action.setValueName(this.ruleEngineElementManager.getById(value).getName());
        } else if (type.equals(VariableType.VARIABLE.getType())) {
            RuleEngineVariable engineVariable = this.ruleEngineVariableManager.getById(value);
            action.setValueName(engineVariable.getName());
            if (engineVariable.getType().equals(VariableType.CONSTANT.getType())) {
                action.setVariableValue(engineVariable.getValue());
            }
        }
        action.setValue(value);
        return action;
    }

}
