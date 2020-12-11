package cn.ruleengine.web.service.impl;


import cn.ruleengine.web.vo.rule.RuleCountInfo;
import com.engine.core.Engine;
import com.engine.core.condition.Condition;
import com.engine.core.condition.ConditionGroup;
import com.engine.core.condition.ConditionSet;
import com.engine.core.exception.ValidException;
import com.engine.core.rule.Rule;
import com.engine.core.value.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/27
 * @since 1.0.0
 */
@Slf4j
@Service
public class RuleParameterService {

    @Resource
    private Engine engine;

    /**
     * 统计引用此规则的所有的元素变量
     *
     * @param rule rule
     * @return RuleCountInfo
     */
    public Set<Rule.Parameter> getParameters(Rule rule) {
        Set<Rule.Parameter> parameters = new HashSet<>();
        RuleCountInfo countInfo = new RuleCountInfo();
        countInfo.setRuleId(rule.getId());
        ConditionSet conditionSet = rule.getConditionSet();
        List<ConditionGroup> conditionGroups = conditionSet.getConditionGroups();
        for (ConditionGroup conditionGroup : conditionGroups) {
            List<Condition> conditions = conditionGroup.getConditions();
            for (Condition condition : conditions) {
                this.getFromVariableElement(parameters, condition.getRightValue());
                this.getFromVariableElement(parameters, condition.getLeftValue());
            }
        }
        this.getFromTheResult(parameters, rule);
        return parameters;
    }

    /**
     * 统计规则结果中引用的元素变量
     *
     * @param parameters parameters
     * @param rule       rule
     */
    private void getFromTheResult(Set<Rule.Parameter> parameters, Rule rule) {
        Value value = rule.getActionValue();
        this.getFromVariableElement(parameters, value);
        Value defaultActionValue = rule.getDefaultActionValue();
        if (defaultActionValue != null) {
            this.getFromVariableElement(parameters, defaultActionValue);
        }
    }

    /**
     * 统计函数参数中引用的元素/变量信息
     *
     * @param parameters parameters
     * @param value      value
     */
    private void getFromVariableElement(Set<Rule.Parameter> parameters, Value value) {
        if (value instanceof Variable) {
            Value val = this.engine.getEngineVariable().getVariable(((Variable) value).getVariableId());
            if (val instanceof Function) {
                Function function = (Function) val;
                Map<String, Value> param = function.getParam();
                Collection<Value> values = param.values();
                for (Value v : values) {
                    if (v instanceof Element) {
                        Rule.Parameter parameter = new Rule.Parameter();
                        parameter.setName(((Element) v).getElementName());
                        parameter.setCode(((Element) v).getElementCode());
                        parameter.setValueType(v.getValueType());
                        parameters.add(parameter);
                    } else if (v instanceof Variable) {
                        try {
                            this.getFromVariableElement(parameters, v);
                        } catch (StackOverflowError e) {
                            log.error("堆栈溢出错误", e);
                            throw new ValidException("请检查规则变量是否存在循环引用");
                        }
                    }
                }
            }
        } else if (value instanceof Element) {
            Rule.Parameter parameter = new Rule.Parameter();
            parameter.setName(((Element) value).getElementName());
            parameter.setCode(((Element) value).getElementCode());
            parameter.setValueType(value.getValueType());
            parameters.add(parameter);
        }
    }

}
