package cn.ruleengine.web.service.impl;


import cn.ruleengine.web.service.RuleEngineConditionGroupService;
import cn.ruleengine.web.store.entity.RuleEngineConditionGroup;
import cn.ruleengine.web.store.manager.RuleEngineConditionGroupManager;
import cn.ruleengine.web.vo.condition.group.SaveOrUpdateConditionGroup;
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

    /**
     * 保存或者更新条件组
     *
     * @param saveOrUpdateConditionGroup 条件组信息
     * @return int
     */
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


    /**
     * 删除条件组
     *
     * @param id 条件组id
     * @return true
     */
    @Override
    public Boolean delete(Integer id) {
        return this.ruleEngineConditionGroupManager.removeById(id);
    }


}
