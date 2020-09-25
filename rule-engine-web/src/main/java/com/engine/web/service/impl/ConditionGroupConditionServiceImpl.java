package com.engine.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.engine.web.service.ConditionGroupConditionService;
import com.engine.web.store.entity.RuleEngineConditionGroupCondition;
import com.engine.web.store.manager.RuleEngineConditionGroupConditionManager;
import com.engine.web.vo.condition.group.condition.SaveOrUpdateConditionGroupCondition;
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
        RuleEngineConditionGroupCondition groupCondition = new RuleEngineConditionGroupCondition();
        BeanUtil.copyProperties(saveOrUpdateConditionGroup, groupCondition);
        this.ruleEngineConditionGroupConditionManager.saveOrUpdate(groupCondition);
        return groupCondition.getId();
    }

    @Override
    public Boolean delete(Integer id) {
        return this.ruleEngineConditionGroupConditionManager.removeById(id);
    }

}
