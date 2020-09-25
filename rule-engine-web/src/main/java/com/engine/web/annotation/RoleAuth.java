package com.engine.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 〈一句话功能简述〉<br>
 * 加上此注解的类或者方法需要验证角色权限
 * <p>
 * 执行优先级:方法级的RoleAuth大于类,如果执行的类上以及方法上有此注解,则走方法上的角色code验证,方法上没有则默认走类上的
 * <p>
 * 如果没有此注解,默认需要登录后可以访问,前提是没有NoAuth存在
 *
 * @author 丁乾文
 * @create 2019/8/13
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleAuth {

    /**
     * 默认可以访问的角色code
     */
    String DEFAULT_CODE = "admin";

    /**
     * 哪些角色可以访问此类/方法
     *
     * @return String
     */
    String[] code() default DEFAULT_CODE;


    /**
     * 高权限是否往下传递,例如有admin角色,普通用户角色,我在一个方法上设置了只有普通用户可以访问
     * 是否允许上级角色admin也可以访问此方法
     *
     * @return boolean
     */
    boolean transfer() default true;
}
