package cn.ruleengine.web.store.mapper;

import cn.ruleengine.web.store.entity.RuleEngineWorkspace;
import cn.ruleengine.web.vo.workspace.Workspace;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工作空间 Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2020-11-21
 */
public interface RuleEngineWorkspaceMapper extends BaseMapper<RuleEngineWorkspace> {

    /**
     * 根据用户id获取用户有权限的工作空间
     *
     * @param userId 用户id
     * @return list
     */
    List<Workspace> listWorkspaceByUserId(@Param("userId") Integer userId);

    /**
     * 是否存在此用户这个工作空间权限
     *
     * @param workspaceId 工作空间id
     * @param userId      用户id
     * @return count
     */
    Integer countWorkspace(Integer workspaceId, Integer userId);
}
