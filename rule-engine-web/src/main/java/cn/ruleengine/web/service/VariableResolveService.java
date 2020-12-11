package cn.ruleengine.web.service;

import cn.ruleengine.web.store.entity.RuleEngineVariable;
import cn.ruleengine.web.vo.common.DataCacheMap;
import cn.ruleengine.core.value.Function;
import cn.ruleengine.core.value.Value;

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
     * @param cacheMap           配置缓存数据
     * @return Function
     */
    Function functionProcess(RuleEngineVariable ruleEngineVariable, DataCacheMap cacheMap);
}
