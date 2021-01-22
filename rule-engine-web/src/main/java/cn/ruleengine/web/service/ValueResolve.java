package cn.ruleengine.web.service;

import cn.ruleengine.web.store.entity.RuleEngineElement;
import cn.ruleengine.core.value.Value;
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

}
