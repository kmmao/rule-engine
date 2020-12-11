package cn.ruleengine.web.store.mapper;

import cn.ruleengine.web.store.entity.RuleEngineCondition;
import cn.ruleengine.web.store.entity.RuleEngineRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.ruleengine.core.annotation.Param;

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

    /**
     * 根据id更新
     *
     * @param ruleEngineRule 规则信息
     * @return int
     */
    int updateRuleById(RuleEngineRule ruleEngineRule);

}
