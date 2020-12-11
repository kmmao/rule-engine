package cn.ruleengine.web.vo.user;

import cn.ruleengine.web.annotation.RoleAuth;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/11/22
 * @since 1.0.0
 */
@Data
public class UserData implements Serializable {

    private static final long serialVersionUID = -5944149026431724373L;

    private Integer id;

    private String username;

    private String email;

    private Long phone;

    private String avatar;

    private String sex;

    private Boolean isAdmin = false;

    private List<Role> roles;

    public void setRoles(List<Role> roles) {
        this.roles = roles;
        for (UserData.Role role : roles) {
            if (RoleAuth.ADMIN.equals(role.getCode())) {
                this.setIsAdmin(true);
                return;
            }
        }
        this.setIsAdmin(false);
    }

    @Data
    public static class Role implements Serializable {

        private static final long serialVersionUID = -2403951641761610849L;

        private Integer id;

        /**
         * 名称
         */
        private String name;

        private String code;
    }
}
