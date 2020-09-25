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

    Boolean verifyName(VerifyNameRequest verifyNameRequest);

    Object verifyCodeByEmail(GetVerifyCodeByEmailRequest verifyCodeByEmailRequest);

    Boolean verifyEmail(VerifyEmailRequest verifyEmailRequest);

    Boolean updatePassword(ForgotRequest forgotRequest);

    UserResponse getUserInfo();

}
