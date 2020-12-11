package cn.ruleengine.web.service;

import cn.ruleengine.web.store.entity.RuleEngineRole;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/10/22
 * @since 1.0.0
 */
public interface RoleService {
    /**
     * 根据role code查询对应的角色路径
     *
     * @param codes Codes
     * @return RolePath
     */
    Set<String> listRoleIdByCodes(Collection<String> codes);

    /**
     * 根据用户id查询此用户的角色
     *
     * @param userId 用户id
     * @return List
     */
    List<RuleEngineRole> listRoleByUserId(Integer userId);
}
