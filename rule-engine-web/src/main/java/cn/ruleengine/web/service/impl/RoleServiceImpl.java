package cn.ruleengine.web.service.impl;


import cn.ruleengine.web.service.RoleService;
import cn.ruleengine.web.store.entity.RuleEngineRole;
import cn.ruleengine.web.store.manager.RuleEngineRoleManager;
import cn.ruleengine.web.store.mapper.RuleEngineRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/10/22
 * @since 1.0.0
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RuleEngineRoleManager bootRoleManager;
    @Resource
    private RuleEngineRoleMapper bootRoleMapper;

    /**
     * 根据role code查询对应的角色路径
     *
     * @param codes Codes
     * @return RolePath
     */
    @Override
    public Set<String> listRoleIdByCodes(Collection<String> codes) {
        return bootRoleManager.lambdaQuery()
                .select(RuleEngineRole::getRolePath).in(RuleEngineRole::getCode, codes)
                .list().stream().map(RuleEngineRole::getRolePath).collect(Collectors.toSet());
    }

    /**
     * 根据用户id查询此用户的角色
     *
     * @param userId 用户id
     * @return List
     */
    @Override
    public List<RuleEngineRole> listRoleByUserId(Integer userId) {
        return bootRoleMapper.listRoleByUserId(userId);
    }
}
