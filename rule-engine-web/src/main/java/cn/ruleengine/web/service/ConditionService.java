package cn.ruleengine.web.service;


import cn.ruleengine.web.store.entity.RuleEngineCondition;
import cn.ruleengine.web.store.entity.RuleEngineElement;
import cn.ruleengine.web.store.entity.RuleEngineVariable;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.condition.*;
import cn.ruleengine.core.rule.Rule;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
public interface ConditionService {

    /**
     * 保存条件
     *
     * @param addConditionRequest 条件配置信息
     * @return true
     */
    Boolean save(AddConditionRequest addConditionRequest);

    /**
     * 条件名称是否存在
     *
     * @param name 条件名称
     * @return true存在
     */
    Boolean conditionNameIsExists(String name);

    /**
     * 根绝id查询条件信息
     *
     * @param id 条件id
     * @return ConditionResponse
     */
    ConditionResponse getById(Integer id);

    /**
     * 条件转换
     *
     * @param engineCondition engineCondition
     * @return ConditionResponse
     */
    ConditionResponse getConditionResponse(RuleEngineCondition engineCondition);

    /**
     * 条件转换
     *
     * @param engineCondition engineCondition
     * @param variableMap     条件用到的变量
     * @param elementMap      条件用到的元素
     * @return ConditionResponse
     */
    ConditionResponse getConditionResponse(RuleEngineCondition engineCondition, Map<Integer, RuleEngineVariable> variableMap, Map<Integer, RuleEngineElement> elementMap);

    /**
     * 条件列表
     *
     * @param pageRequest 分页查询信息
     * @return page
     */
    PageResult<ListConditionResponse> list(PageRequest<ListConditionRequest> pageRequest);

    /**
     * 获取条件中的变量
     *
     * @param ruleEngineConditions 条件信息
     * @return map
     */
    Map<Integer, RuleEngineVariable> getConditionVariableMap(Collection<RuleEngineCondition> ruleEngineConditions);

    /**
     * 获取条件中的元素
     *
     * @param ruleEngineConditions 条件信息
     * @return map
     */
    Map<Integer, RuleEngineElement> getConditionElementMap(Collection<RuleEngineCondition> ruleEngineConditions);

    /**
     * 更新条件
     *
     * @param updateConditionRequest 更新条件
     * @return true
     */
    Boolean update(UpdateConditionRequest updateConditionRequest);

    /**
     * 删除条件
     *
     * @param id 条件id
     * @return true：删除成功
     */
    Boolean delete(Integer id);

    /**
     * 根据id获取条件中的元素
     *
     * @param id 条件id
     * @return list
     */
    Set<Rule.Parameter> getParameter(Integer id);

    /**
     * 测试运行条件
     *
     * @param executeCondition 参数
     * @return true/false
     */
    Boolean run(ExecuteConditionRequest executeCondition);

}
