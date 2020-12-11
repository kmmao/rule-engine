package cn.ruleengine.web.vo.common;


import cn.ruleengine.web.store.entity.*;
import lombok.Data;

import java.util.HashMap;
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
@Data
public class DataCacheMap {

    private Map<Integer, RuleEngineElement> elementMap = new HashMap<>();

    private Map<Integer, RuleEngineVariable> variableMap = new HashMap<>();

    private Map<String, RuleEngineVariable> variableNameMap = new HashMap<>();

    private Map<Integer, RuleEngineFunction> functionMap = new HashMap<>();

    private Map<Integer, List<RuleEngineFunctionValue>> functionValueMap = new HashMap<>();

    private Map<Integer, RuleEngineCondition> conditionMap = new HashMap<>();

}
