package cn.ruleengine.web.service;

import cn.ruleengine.web.vo.condition.group.condition.SaveOrUpdateConditionGroupCondition;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/28
 * @since 1.0.0
 */
public interface ConditionGroupConditionService {

    /**
     * 保存或者更新条件组
     *
     * @param saveOrUpdateConditionGroup 条件组条件信息
     * @return int
     */
    Integer saveOrUpdateConditionGroupCondition(SaveOrUpdateConditionGroupCondition saveOrUpdateConditionGroup);

    /**
     * 删除条件组
     *
     * @param id 条件组条件id
     * @return true
     */
    Boolean delete(Integer id);

}
