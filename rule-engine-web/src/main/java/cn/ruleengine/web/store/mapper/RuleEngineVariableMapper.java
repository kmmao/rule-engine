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
    Integer countPublishRuleVariable(@Param("variableId") Integer variableId);

    /**
     * 统计发布规则集中引用此变量的数量
     *
     * @param variableId 变量id
     * @return 引用的数量
     */
    Integer countPublishRuleSetVariable(@Param("variableId") Integer variableId);

    /**
     * 统计规则中引用此变量的数量
     *
     * @param variableId 变量id
     * @return 引用的数量
     */
    Integer countRuleVariable(Integer variableId);

    /**
     * 统计规则集中引用此变量的数量
     *
     * @param variableId 变量id
     * @return 引用的数量
     */
    Integer countRuleSetVariable(Integer variableId);

    /**
     * 统计决策表引用此变量的数量
     *
     * @param id id
     * @return int
     */
    int countDecisionTableVariableId(@Param("id") Integer id);

    /**
     * 统计发布决策表引用此变量的数量
     *
     * @param id id
     * @return int
     */
    int countPublishDecisionTableVariableId(@Param("id") Integer id);
}
