package com.engine.web.service;

import com.engine.web.vo.user.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    /**
     * 上传用户头像
     *
     * @param file 图片文件
     * @return 图片url
     */
    Object uploadAvatar(MultipartFile file) throws IOException;

    /**
     * 更新用户信息
     *
     * @param updateUserInfoRequest 根据id更新用户信息
     * @return 用户信息
     */
    Boolean updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest);
}
