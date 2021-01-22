package cn.ruleengine.web.service;

import cn.ruleengine.web.store.entity.RuleEngineElement;
import cn.ruleengine.web.store.entity.RuleEngineFunction;
import cn.ruleengine.web.store.entity.RuleEngineFunctionValue;
import cn.ruleengine.web.store.entity.RuleEngineVariable;
import cn.ruleengine.core.value.Function;
import cn.ruleengine.core.value.Value;

import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/17
 * @since 1.0.0
 */
public interface VariableResolveService {

    /**
     * 获取所有的变量/函数配置信息
     *
     * @return 变量
     */
    Map<Integer, Value> getAllVariable();

    /**
     * 根据变量获取变量/函数配置信息
     *
     * @param id 变量id
     * @return 变量
     */
    Value getVarById(Integer id);

    /**
     * 规则引擎函数处理
     *
     * @param ruleEngineVariable 规则函数元数据
     * @param engineFunctionMap  engineFunction缓存数据
     * @return Function
     */
    Function functionProcess(RuleEngineVariable ruleEngineVariable, Map<Integer, RuleEngineFunction> engineFunctionMap, Map<Integer, List<RuleEngineFunctionValue>> functionValueMap, Map<Integer, RuleEngineElement> engineElementMap);

}
