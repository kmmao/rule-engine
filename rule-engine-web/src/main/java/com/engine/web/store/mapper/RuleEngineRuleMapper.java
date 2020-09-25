package com.engine.web.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.engine.core.annotation.Param;
import com.engine.web.store.entity.RuleEngineCondition;
import com.engine.web.store.entity.RuleEngineRule;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2020-07-15
 */
public interface RuleEngineRuleMapper extends BaseMapper<RuleEngineRule> {

    /**
     * 统计规则中所有的条件
     *
     * @param ruleId 规则id
     * @return 条件集合
     */
    List<RuleEngineCondition> countCondition(@Param("ruleId") Integer ruleId);

}
