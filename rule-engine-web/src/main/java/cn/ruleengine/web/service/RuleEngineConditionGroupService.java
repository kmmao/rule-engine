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

    Integer saveOrUpdateConditionGroup(SaveOrUpdateConditionGroup saveOrUpdateConditionGroup);

    Boolean delete(Integer id);

}
