package cn.ruleengine.web.store.mapper;

import cn.ruleengine.core.annotation.Param;
import cn.ruleengine.web.store.entity.RuleEngineElement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
     * 统计发布规则引用此元素
     *
     * @param id 元素id
     * @return int
     */
    Integer countPublishRuleElement(@Param("id") Integer id);

    /**
     * 统计发布规则集引用此元素
     *
     * @param id 元素id
     * @return int
     */
    Integer countPublishRuleSetElement(@Param("id") Integer id);

}
