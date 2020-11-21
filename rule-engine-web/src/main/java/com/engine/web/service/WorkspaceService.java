package com.engine.web.service;

import com.engine.web.vo.workspace.Workspace;

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
     * 所有用户组
     *
     * @return list
     */
    List<Workspace> list();

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
}
