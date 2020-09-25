package com.engine.web.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.engine.web.store.entity.RuleEngineElement;
import com.engine.web.store.entity.RuleEngineRule;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2020-07-14
 */
public interface RuleEngineElementMapper extends BaseMapper<RuleEngineElement> {

    /**
     * 统计引用此规则的元素
     *
     * @param id 元素id
     * @return list
     */
    List<RuleEngineRule> countRule(Integer id);
}
