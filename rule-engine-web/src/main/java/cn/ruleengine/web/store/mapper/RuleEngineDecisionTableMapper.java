package cn.ruleengine.web.store.mapper;

import cn.ruleengine.web.store.entity.RuleEngineDecisionTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2020-12-27
 */
public interface RuleEngineDecisionTableMapper extends BaseMapper<RuleEngineDecisionTable> {

    /**
     * 统计决策表引用此元素的数量
     *
     * @param id id
     * @return int
     */
    int countReferenceByElementId(@Param("id") Integer id);

    /**
     * 统计决策表引用此变量的数量
     *
     * @param id id
     * @return int
     */
    int countReferenceByVariableId(@Param("id") Integer id);

}
