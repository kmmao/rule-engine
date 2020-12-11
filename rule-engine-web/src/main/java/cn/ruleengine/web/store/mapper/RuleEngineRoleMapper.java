package cn.ruleengine.web.store.mapper;

import cn.ruleengine.web.store.entity.RuleEngineRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2020-09-24
 */
public interface RuleEngineRoleMapper extends BaseMapper<RuleEngineRole> {


    /**
     * 根据用户id查询此用户的所有角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    List<RuleEngineRole> listRoleByUserId(@Param("userId") Integer userId);

}
