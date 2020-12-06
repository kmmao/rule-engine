package com.engine.web.controller;

import com.engine.web.annotation.NoAuth;
import com.engine.web.annotation.RateLimit;
import com.engine.web.enums.RateLimitEnum;
import com.engine.web.service.UserService;
import com.engine.web.util.HttpServletUtils;
import com.engine.web.vo.base.response.BaseResult;
import com.engine.web.vo.base.response.PlainResult;
import com.engine.web.vo.user.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/31
 * @since 1.0.0
 */
@Api(tags = "用户控制器")
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param registerRequest 注册信息
     * @return true表示注册成功
     */
    @NoAuth
    @PostMapping("register")
    @ApiOperation("用户注册")
    public PlainResult<Boolean> register(@RequestBody @Valid RegisterRequest registerRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(userService.register(registerRequest));
        return plainResult;
    }

    /**
     * 用户登录
     *
     * @param loginRequest 登录信息
     * @return true表示登录成功
     */
    @NoAuth
    @RateLimit(limit = 3)
    @PostMapping("login")
    @ApiOperation("用户登录")
    public PlainResult<Boolean> login(@Valid @RequestBody LoginRequest loginRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(userService.login(loginRequest));
        return plainResult;
    }

    /**
     * 获取登录人信息
     *
     * @return user
     */
    @PostMapping("getUserInfo")
    @ApiOperation("获取登录人信息")
    public PlainResult<UserResponse> getUserInfo() {
        PlainResult<UserResponse> plainResult = new PlainResult<>();
        plainResult.setData(userService.getUserInfo());
        return plainResult;
    }

    /**
     * 退出登录
     *
     * @return true
     */
    @Deprecated
    @PostMapping("logout")
    @ApiOperation("退出登录")
    public PlainResult<?> logout() {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(userService.logout());
        return plainResult;
    }

    /**
     * 根据邮箱获取验证码
     *
     * @param verifyCodeByEmailRequest 邮箱/类型:注册,忘记密码
     * @return BaseResult
     */
    @NoAuth
    @RateLimit(type = RateLimitEnum.URL_IP)
    @PostMapping("verifyCodeByEmail")
    @ApiOperation("根据邮箱获取验证码!")
    public BaseResult verifyCodeByEmail(@Valid @RequestBody GetVerifyCodeByEmailRequest verifyCodeByEmailRequest) {
        val result = new PlainResult<>();
        result.setData(userService.verifyCodeByEmail(verifyCodeByEmailRequest));
        return result;
    }

    /**
     * 上传用户头像
     *
     * @param file 图片文件
     * @return 图片url
     */
    @PostMapping("uploadAvatar")
    @ApiOperation("上传用户头像")
    public BaseResult uploadAvatar(MultipartFile file) throws IOException {
        val result = new PlainResult<>();
        result.setData(userService.uploadAvatar(file));
        return result;
    }

    /**
     * 更新用户信息
     *
     * @param updateUserInfoRequest 根据id更新用户信息
     * @return 用户信息
     */
    @ApiOperation("更新用户信息")
    @PostMapping("/updateUserInfo")
    public BaseResult updateUserInfo(@RequestBody @Valid UpdateUserInfoRequest updateUserInfoRequest) {
        val result = new PlainResult<>();
        result.setData(userService.updateUserInfo(updateUserInfoRequest));
        return result;
    }
}
