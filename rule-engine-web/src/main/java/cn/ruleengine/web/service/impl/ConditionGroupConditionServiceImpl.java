package cn.ruleengine.web.service.impl;

import cn.ruleengine.web.service.ConditionGroupConditionService;
import cn.ruleengine.web.store.entity.RuleEngineConditionGroupCondition;
import cn.ruleengine.web.store.manager.RuleEngineConditionGroupConditionManager;
import cn.ruleengine.web.vo.conver.BasicConversion;
import cn.ruleengine.web.vo.condition.group.condition.SaveOrUpdateConditionGroupCondition;
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
public class ConditionGroupConditionServiceImpl implements ConditionGroupConditionService {

    @Resource
    private RuleEngineConditionGroupConditionManager ruleEngineConditionGroupConditionManager;

    @Override
    public Integer saveOrUpdateConditionGroupCondition(SaveOrUpdateConditionGroupCondition saveOrUpdateConditionGroup) {
        RuleEngineConditionGroupCondition groupCondition = BasicConversion.INSTANCE.conver(saveOrUpdateConditionGroup);
        this.ruleEngineConditionGroupConditionManager.saveOrUpdate(groupCondition);
        return groupCondition.getId();
    }

    @Override
    public Boolean delete(Integer id) {
        return this.ruleEngineConditionGroupConditionManager.removeById(id);
    }

}
