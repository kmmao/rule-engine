package cn.ruleengine.web.config;

import cn.ruleengine.web.interceptor.AuthInterceptor;
import cn.ruleengine.web.service.WorkspaceService;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.Workspace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/12/22
 * @since 1.0.0
 */
@Slf4j
@Component
public class Context implements ApplicationContextAware {


    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Context.applicationContext = applicationContext;
    }

    /**
     * 获取当前登录用户
     *
     * @return UserData
     */
    public static UserData getCurrentUser() {
        return AuthInterceptor.USER.get();
    }

    /**
     * 获取当前用户用户所在工作空间
     *
     * @return Workspace
     */
    public static Workspace getCurrentWorkspace() {
        WorkspaceService workspaceService = applicationContext.getBean(WorkspaceService.class);
        return workspaceService.currentWorkspace();
    }

}
