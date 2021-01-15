package cn.ruleengine.web.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.web.service.RuleEngineConditionGroupService;
import cn.ruleengine.web.store.entity.RuleEngineConditionGroup;
import cn.ruleengine.web.store.entity.RuleEngineConditionGroupCondition;
import cn.ruleengine.web.store.manager.RuleEngineConditionGroupConditionManager;
import cn.ruleengine.web.store.manager.RuleEngineConditionGroupManager;
import cn.ruleengine.web.vo.condition.ConditionGroupCondition;
import cn.ruleengine.web.vo.condition.ConditionGroupConfig;
import cn.ruleengine.web.vo.condition.group.SaveOrUpdateConditionGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/28
 * @since 1.0.0
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class RuleEngineConditionGroupServiceImpl implements RuleEngineConditionGroupService {

    @Resource
    private RuleEngineConditionGroupManager ruleEngineConditionGroupManager;
    @Resource
    private RuleEngineConditionGroupConditionManager ruleEngineConditionGroupConditionManager;

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
     * 保存条件组
     *
     * @param ruleId         规则id
     * @param conditionGroup 条件组信息
     */
    @Override
    public void saveConditionGroup(Integer ruleId, List<ConditionGroupConfig> conditionGroup) {
        if (CollUtil.isEmpty(conditionGroup)) {
            return;
        }
        List<RuleEngineConditionGroupCondition> ruleEngineConditionGroupConditions = new LinkedList<>();
        for (ConditionGroupConfig groupConfig : conditionGroup) {
            RuleEngineConditionGroup engineConditionGroup = new RuleEngineConditionGroup();
            engineConditionGroup.setName(groupConfig.getName());
            engineConditionGroup.setRuleId(ruleId);
            engineConditionGroup.setOrderNo(groupConfig.getOrderNo());
            this.ruleEngineConditionGroupManager.save(engineConditionGroup);
            List<ConditionGroupCondition> conditionGroupConditions = groupConfig.getConditionGroupCondition();
            if (CollUtil.isNotEmpty(conditionGroupConditions)) {
                for (ConditionGroupCondition conditionGroupCondition : conditionGroupConditions) {
                    RuleEngineConditionGroupCondition ruleEngineConditionGroupCondition = new RuleEngineConditionGroupCondition();
                    ruleEngineConditionGroupCondition.setConditionId(conditionGroupCondition.getCondition().getId());
                    ruleEngineConditionGroupCondition.setConditionGroupId(engineConditionGroup.getId());
                    ruleEngineConditionGroupCondition.setOrderNo(conditionGroupCondition.getOrderNo());
                    ruleEngineConditionGroupConditions.add(ruleEngineConditionGroupCondition);
                }
            }
        }
        if (CollUtil.isNotEmpty(ruleEngineConditionGroupConditions)) {
            this.ruleEngineConditionGroupConditionManager.saveBatch(ruleEngineConditionGroupConditions);
        }
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
