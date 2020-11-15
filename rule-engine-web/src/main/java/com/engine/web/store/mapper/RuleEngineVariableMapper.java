package com.engine.web.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.engine.core.annotation.Param;
import com.engine.web.store.entity.RuleEngineRule;
import com.engine.web.store.entity.RuleEngineVariable;
import com.engine.web.vo.variable.VariableRuleCount;

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
     * 统计发布规则中引用此变量的数量
     *
     * @param variableId 变量id
     * @return 引用的数量
     */
    Integer countPublishRuleVar(@Param("variableId") Integer variableId);

}
