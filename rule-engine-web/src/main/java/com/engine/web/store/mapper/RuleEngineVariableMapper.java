package com.engine.web.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.engine.web.store.entity.RuleEngineRule;
import com.engine.web.store.entity.RuleEngineVariable;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2020-07-14
 */
public interface RuleEngineVariableMapper extends BaseMapper<RuleEngineVariable> {

    /**
     * 统计用到此变量的规则
     *
     * @param id 变量id
     * @return list
     */
    List<RuleEngineRule> countRule(Integer id);
}
