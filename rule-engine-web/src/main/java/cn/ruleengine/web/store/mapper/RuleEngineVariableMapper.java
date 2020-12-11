package cn.ruleengine.web.store.mapper;

import cn.ruleengine.web.store.entity.RuleEngineVariable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.ruleengine.core.annotation.Param;

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
