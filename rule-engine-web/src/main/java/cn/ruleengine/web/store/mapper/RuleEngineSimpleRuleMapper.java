package cn.ruleengine.web.store.mapper;

import cn.ruleengine.web.store.entity.RuleEngineSimpleRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2020-12-29
 */
public interface RuleEngineSimpleRuleMapper extends BaseMapper<RuleEngineSimpleRule> {

    /**
     * 根据id更新
     *
     * @param ruleEngineSimpleRule 规则信息
     */
    void updateRuleById(RuleEngineSimpleRule ruleEngineSimpleRule);

}
