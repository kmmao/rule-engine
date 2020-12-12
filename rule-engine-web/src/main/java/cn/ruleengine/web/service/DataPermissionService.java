package cn.ruleengine.web.service;

import cn.ruleengine.web.annotation.DataPermission;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/13
 * @since 1.0.0
 */
public interface DataPermissionService {

    /**
     * 校验数据权限
     *
     * @param id             数据id
     * @param dataPermission dataPermission
     * @return true有权限
     */
    Boolean validDataPermission(Serializable id, DataPermission dataPermission);

}
