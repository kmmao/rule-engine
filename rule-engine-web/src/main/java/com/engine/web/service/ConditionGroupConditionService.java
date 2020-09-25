package com.engine.web.service;

import com.engine.web.vo.condition.group.condition.SaveOrUpdateConditionGroupCondition;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/28
 * @since 1.0.0
 */
public interface ConditionGroupConditionService {

    Integer saveOrUpdateConditionGroupCondition(SaveOrUpdateConditionGroupCondition saveOrUpdateConditionGroup);

    Boolean delete(Integer id);

}
