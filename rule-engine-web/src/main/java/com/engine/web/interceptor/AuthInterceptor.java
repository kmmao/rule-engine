package com.engine.web.interceptor;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.engine.web.annotation.RoleAuth;
import com.engine.web.service.RoleService;
import com.engine.web.store.entity.RuleEngineRole;
import com.engine.web.store.entity.RuleEngineUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈Token认证
 * 如果不需要token认证,则在controller的方法上加上NoAuth注解〉
 *
 * @author 丁乾文
 * @create 2019/8/13
 * @since 1.0.0
 */
@Slf4j
@Component
public class AuthInterceptor extends AbstractTokenInterceptor {

    @Resource
    private RoleService roleService;
    /**
     * 是否开启鉴权
     */
    @Value("${auth.enable}")
    private boolean enable;

    /**
     * 是否开启鉴权
     *
     * @return boolean
     */
    @Override
    public boolean enable() {
        return enable;
    }

    /**
     * 权限鉴定 注解方式 对访问人员工code进行验证
     *
     * @param code     权限code
     * @param transfer transfer
     * @param userId   用户id
     * @return true
     */
    @Override
    public boolean auth(String[] code, boolean transfer, Integer userId) {
        List<RuleEngineRole> bootRoles = roleService.listRoleByUserId(userId);
        //如果当前用户没有任何角色,返回false,无权限
        if (CollUtil.isEmpty(bootRoles)) {
            return false;
        }
        //如果当前角色与方法@RoleAuth设置的匹配,返回true
        Set<String> roleCodeSet = new HashSet<>(Arrays.asList(code));
        for (RuleEngineRole role : bootRoles) {
            if (roleCodeSet.contains(role.getCode())) {
                return true;
            }
        }
        //高权限的是否可以访问低级权限code,如果为true时可以访问
        if (transfer) {
            //应该实时查询角色变化信息
            Set<String> roleIdSet = roleService.listRoleIdByCodes(roleCodeSet);
            //如果根据角色code没有查询到任何数据
            if (CollUtil.isEmpty(roleIdSet)) {
                throw new RuntimeException(String.format("根据角色Code:[%s]没有查询到任何数据", roleIdSet));
            }
            for (String roleId : roleIdSet) {
                Set<String> rolePathSplit = new HashSet<>(Arrays.asList(roleId.split(StringPool.AT)));
                for (RuleEngineRole role : bootRoles) {
                    if (rolePathSplit.contains(String.valueOf(role.getId()))) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

}
