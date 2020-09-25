package com.engine.web.service.impl;


import com.engine.web.service.RuleEngineConditionGroupService;
import com.engine.web.store.entity.RuleEngineConditionGroup;
import com.engine.web.store.manager.RuleEngineConditionGroupManager;
import com.engine.web.vo.condition.group.SaveOrUpdateConditionGroup;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/28
 * @since 1.0.0
 */
@Service
public class RuleEngineConditionGroupServiceImpl implements RuleEngineConditionGroupService {

    @Resource
    private RuleEngineConditionGroupManager ruleEngineConditionGroupManager;

    @Override
    public Integer saveOrUpdateConditionGroup(SaveOrUpdateConditionGroup saveOrUpdateConditionGroup) {
        RuleEngineConditionGroup engineConditionGroup = new RuleEngineConditionGroup();
        engineConditionGroup.setId(saveOrUpdateConditionGroup.getId());
        engineConditionGroup.setName(saveOrUpdateConditionGroup.getName());
        engineConditionGroup.setRuleId(saveOrUpdateConditionGroup.getRuleId());
        engineConditionGroup.setOrderNo(saveOrUpdateConditionGroup.getOrderNo());
        this.ruleEngineConditionGroupManager.saveOrUpdate(engineConditionGroup);
        return engineConditionGroup.getId();
    }

    @Override
    public Boolean delete(Integer id) {
        return this.ruleEngineConditionGroupManager.removeById(id);
    }


}
