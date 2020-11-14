package com.engine.web.service;


import com.engine.web.store.entity.RuleEngineCondition;
import com.engine.web.store.entity.RuleEngineElement;
import com.engine.web.store.entity.RuleEngineRule;
import com.engine.web.store.entity.RuleEngineVariable;
import com.engine.web.vo.base.request.PageRequest;
import com.engine.web.vo.base.response.PageResult;
import com.engine.web.vo.condition.*;

import java.util.List;
import java.util.Map;

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


}
