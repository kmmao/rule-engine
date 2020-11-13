package com.engine.web.service;

import com.engine.web.vo.user.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author liqian
 * @date 2020/9/24
 */
public interface UserService {

    boolean login(LoginRequest loginRequest);

    Boolean register(RegisterRequest registerRequest);

    /**
     * 验证用户名是否重复
     *
     * @param verifyNameRequest verifyNameRequest
     * @return Boolean
     */
    Boolean verifyName(VerifyNameRequest verifyNameRequest);

    /**
     * 忘记密码获取验证码
     *
     * @param verifyCodeByEmailRequest 邮箱/类型:注册,忘记密码
     * @return BaseResult
     */
    Object verifyCodeByEmail(GetVerifyCodeByEmailRequest verifyCodeByEmailRequest);

    /**
     * 验证邮箱是否重复
     *
     * @param verifyEmailRequest verifyEmailRequest
     * @return Boolean
     */
    Boolean verifyEmail(VerifyEmailRequest verifyEmailRequest);

    /**
     * 修改密码
     *
     * @param forgotRequest forgotRequest
     * @return Boolean
     */
    Boolean updatePassword(ForgotRequest forgotRequest);

    UserResponse getUserInfo();

    /**
     * 退出登录
     *
     * @return true
     */
    Boolean logout();
}
