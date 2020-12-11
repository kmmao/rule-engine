/**
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ruleengine.web.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.web.service.RoleService;
import cn.ruleengine.web.vo.user.UserData;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
     * @param userData 用户数据
     * @return true
     */
    @Override
    public boolean auth(String[] code, boolean transfer, UserData userData) {
        Boolean isAdmin = userData.getIsAdmin();
        if (isAdmin) {
            return true;
        }
        List<UserData.Role> bootRoles = userData.getRoles();
        //如果当前用户没有任何角色,返回false,无权限
        if (CollUtil.isEmpty(bootRoles)) {
            return false;
        }
        //如果当前角色与方法@RoleAuth设置的匹配,返回true
        Set<String> roleCodeSet = new HashSet<>(Arrays.asList(code));
        for (UserData.Role role : bootRoles) {
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
                for (UserData.Role role : bootRoles) {
                    if (rolePathSplit.contains(String.valueOf(role.getId()))) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

}
