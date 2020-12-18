package cn.ruleengine.web.service;

import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.workspace.AccessKey;
import cn.ruleengine.web.vo.workspace.ListWorkspaceRequest;
import cn.ruleengine.web.vo.workspace.ListWorkspaceResponse;
import cn.ruleengine.web.vo.workspace.Workspace;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/11/21
 * @since 1.0.0
 */
public interface WorkspaceService {

    String CURRENT_WORKSPACE = "rule_engine_current_workspace:";

    /**
     * 用户有权限的工作空间
     *
     * @param pageRequest 模糊查询参数
     * @return list
     */
    PageResult<ListWorkspaceResponse> list(PageRequest<ListWorkspaceRequest> pageRequest);


    /**
     * 普通用户是否有这个工作空间权限
     *
     * @param workspaceId 工作空间id
     * @param userId      用户id
     * @return true有权限
     */
    boolean hasWorkspacePermission(Integer workspaceId, Integer userId);

    /**
     * 获取当前工作空间
     *
     * @return Workspace
     */
    Workspace currentWorkspace();

    /**
     * 切换工作空间
     *
     * @param id 工作空间id
     * @return true
     */
    Boolean change(Integer id);

    /**
     * 当前工作空间AccessKey
     *
     * @param code 工作空间code
     * @return accessKey
     */
    AccessKey accessKey(String code);


}
