package cn.ruleengine.web.service;

import cn.ruleengine.web.store.entity.RuleEngineElement;
import cn.ruleengine.core.value.Value;
import cn.ruleengine.web.store.entity.RuleEngineVariable;
import cn.ruleengine.web.vo.condition.ConfigValue;

import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/17
 * @since 1.0.0
 */
public interface ValueResolve {

    /**
     * 解析值，变为Value
     *
     * @param type             0元素，1变量，2固定值
     * @param valueType        STRING,COLLECTION,BOOLEAN,NUMBER
     * @param value            type=0则为元素id，type=2则为具体的值
     * @param engineElementMap engineElementMap
     * @return value
     */
    Value getValue(Integer type, String valueType, String value, Map<Integer, RuleEngineElement> engineElementMap);

    /**
     * 解析值，变为Value
     *
     * @param type      0元素，1变量，2固定值
     * @param valueType STRING,COLLECTION,BOOLEAN,NUMBER
     * @param value     type=0则为元素id，type=2则为具体的值
     * @return value
     */
    Value getValue(Integer type, String valueType, String value);


    /**
     * 解析值/变量/元素/固定值
     *
     * @param cValue Value
     * @return ConfigBean.Value
     */
    ConfigValue getConfigValue(Value cValue);

    /**
     * 解析值/变量/元素/固定值
     *
     * @param value     结果值/可能为变量/元素
     * @param type      变量/元素/固定值
     * @param valueType STRING/NUMBER...
     * @return Action
     */
    ConfigValue getConfigValue(String value, Integer type, String valueType);

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
    ConfigValue getConfigValue(String value, Integer type, String valueType, Map<Integer, RuleEngineVariable> variableMap, Map<Integer, RuleEngineElement> elementMap);
}
