package cn.ruleengine.web.service;

import cn.ruleengine.web.vo.condition.group.SaveOrUpdateConditionGroup;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/28
 * @since 1.0.0
 */
public interface RuleEngineConditionGroupService {

    /**
     * 保存或者更新条件组
     *
     * @param saveOrUpdateConditionGroup 条件组信息
     * @return int
     */
    Integer saveOrUpdateConditionGroup(SaveOrUpdateConditionGroup saveOrUpdateConditionGroup);

    /**
     * 删除条件组
     *
     * @param id 条件组id
     * @return true
     */
    Boolean delete(Integer id);

}
