package cn.ruleengine.web.service.impl;

import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.HtmlTemplatesEnum;
import cn.ruleengine.web.enums.VerifyCodeType;
import cn.ruleengine.web.service.RoleService;
import cn.ruleengine.web.service.UserService;
import cn.ruleengine.web.store.entity.RuleEngineRole;
import cn.ruleengine.web.store.entity.RuleEngineUser;
import cn.ruleengine.web.store.manager.RuleEngineUserManager;
import cn.ruleengine.web.util.*;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.user.*;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.web.interceptor.AbstractTokenInterceptor;
import cn.ruleengine.web.interceptor.AuthInterceptor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author liqian
 * @date 2020/9/24
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private RuleEngineUserManager ruleEngineUserManager;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private EmailClient emailClient;
    @Resource
    private AliOSSClient aliOSSClient;
    @Resource
    private AliOSSClient.Properties properties;
    @Resource
    private RoleService roleService;

    /**
     * 注册时验证码存入redis的前缀
     */
    private static final String REGISTER_EMAIL_CODE_PRE = "rule_engine_user_register_email_code_pre:";
    /**
     * 忘记密码时验证码存入redis的前缀
     */
    private static final String FORGOT_EMAIL_CODE_PRE = "rule_engine_boot_user_forgot_email_code_pre:";

    /**
     * 用户登录
     *
     * @param loginRequest 登录信息
     * @return true表示登录成功
     */
    @Override
    public boolean login(LoginRequest loginRequest) {
        RuleEngineUser ruleEngineUser = ruleEngineUserManager.lambdaQuery()
                .and(a -> a.eq(RuleEngineUser::getUsername, loginRequest.getUsername())
                        .or().eq(RuleEngineUser::getEmail, loginRequest.getUsername())).one();
        if (ruleEngineUser == null) {
            throw new ValidationException("用户名/邮箱不存在!");
        }
        if (!(ruleEngineUser.getPassword().equals(MD5Utils.encrypt(loginRequest.getPassword())))) {
            throw new ValidationException("登录密码错误!");
        }
        String token = JWTUtils.genderToken(String.valueOf(ruleEngineUser.getId()), "boot", ruleEngineUser.getUsername());
        HttpServletResponse response = HttpServletUtils.getResponse();
        response.setHeader(HttpServletUtils.ACCESS_CONTROL_EXPOSE_HEADERS, AbstractTokenInterceptor.TOKEN);
        response.setHeader(AbstractTokenInterceptor.TOKEN, token);
        this.refreshUserData(token, ruleEngineUser);
        return true;
    }

    /**
     * 刷新存在redis中的用户数据
     *
     * @param token          token
     * @param ruleEngineUser 用户信息
     */
    private void refreshUserData(String token, RuleEngineUser ruleEngineUser) {
        UserData userData = BasicConversion.INSTANCE.convert(ruleEngineUser);
        // 重新拉取用户角色信息
        List<RuleEngineRole> bootRoles = this.roleService.listRoleByUserId(ruleEngineUser.getId());
        userData.setRoles(bootRoles.stream().map(m -> {
            UserData.Role role = new UserData.Role();
            role.setId(m.getId());
            role.setName(m.getName());
            role.setCode(m.getCode());
            return role;
        }).collect(Collectors.toList()));
        RBucket<UserData> bucket = this.redissonClient.getBucket(token);
        //保存到redis,用户访问时获取
        bucket.set(userData, JWTUtils.keepTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 用户注册
     *
     * @param registerRequest 注册信息
     * @return true表示注册成功
     */
    @Override
    public Boolean register(RegisterRequest registerRequest) {
        checkVerifyCode(registerRequest.getEmail(), registerRequest.getCode(), REGISTER_EMAIL_CODE_PRE);
        VerifyNameRequest request = new VerifyNameRequest();
        request.setUsername(registerRequest.getUsername());
        if (verifyName(request)) {
            throw new ValidationException("用户名已经存在!");
        }
        VerifyEmailRequest verifyEmailRequest = new VerifyEmailRequest();
        verifyEmailRequest.setEmail(registerRequest.getEmail());
        if (verifyEmail(verifyEmailRequest)) {
            throw new ValidationException("邮箱已经存在!");
        }
        RuleEngineUser ruleEngineUser = new RuleEngineUser();
        ruleEngineUser.setUsername(registerRequest.getUsername());
        ruleEngineUser.setPassword(MD5Utils.encrypt(registerRequest.getPassword()));
        ruleEngineUser.setEmail(registerRequest.getEmail());
        ruleEngineUserManager.save(ruleEngineUser);
        return true;
    }

    /**
     * 验证用户名是否重复
     *
     * @param verifyNameRequest verifyNameRequest
     * @return Boolean
     */
    @Override
    public Boolean verifyName(VerifyNameRequest verifyNameRequest) {
        return null != ruleEngineUserManager.lambdaQuery()
                .eq(RuleEngineUser::getUsername, verifyNameRequest.getUsername())
                .one();
    }

    /**
     * 忘记密码获取验证码
     *
     * @param verifyCodeByEmailRequest 邮箱/类型:注册,忘记密码
     * @return BaseResult
     */
    @Override
    public Boolean verifyCodeByEmail(GetVerifyCodeByEmailRequest verifyCodeByEmailRequest) {
        Integer type = verifyCodeByEmailRequest.getType();
        String email = verifyCodeByEmailRequest.getEmail();
        if (VerifyCodeType.FORGOT.getValue().equals(type)) {
            //忘记密码时检查此邮箱在本系统中是否存在
            VerifyEmailRequest verifyEmailRequest = new VerifyEmailRequest();
            verifyEmailRequest.setEmail(email);
            if (!verifyEmail(verifyEmailRequest)) {
                throw new ValidationException("你输入的邮箱账号在本系统中不存在");
            }
            //获取验证码时,把当前邮箱获取的验证码存入到redis,备用
            verifyCodeProcess(FORGOT_EMAIL_CODE_PRE, email);
        } else if (VerifyCodeType.REGISTER.getValue().equals(type)) {
            //注册获取验证码,获取验证码时,把当前邮箱获取的验证码存入到redis,备用
            verifyCodeProcess(REGISTER_EMAIL_CODE_PRE, email);
        } else {
            throw new ValidationException("不支持的类型");
        }
        return true;
    }

    /**
     * 验证邮箱是否重复
     *
     * @param verifyEmailRequest verifyEmailRequest
     * @return Boolean
     */
    @Override
    public Boolean verifyEmail(VerifyEmailRequest verifyEmailRequest) {
        return null != ruleEngineUserManager.lambdaQuery()
                .eq(RuleEngineUser::getEmail, verifyEmailRequest.getEmail())
                .one();
    }

    /**
     * 发送验证码消息
     *
     * @param pre   pre
     * @param email 邮箱
     */
    private void verifyCodeProcess(String pre, String email) {
        RBucket<Integer> rBucket = redissonClient.getBucket(pre + IPUtils.getRequestIp() + email);
        //生成验证码
        int randomCode = (int) ((Math.random() * 9 + 1) * 10000);
        //设置有效期10分钟
        rBucket.set(randomCode, 600, TimeUnit.SECONDS);
        Map<Object, Object> params = new HashMap<>(1);
        params.put("code", randomCode);
        //发送验证码邮件
        emailClient.sendSimpleMail(params, HtmlTemplatesEnum.EMAIL.getMsg(), HtmlTemplatesEnum.EMAIL.getValue(), email);
    }

    /**
     * 修改密码
     *
     * @param forgotRequest forgotRequest
     * @return Boolean
     */
    @Override
    public Boolean updatePassword(ForgotRequest forgotRequest) {
        checkVerifyCode(forgotRequest.getEmail(), forgotRequest.getCode(), FORGOT_EMAIL_CODE_PRE);
        RuleEngineUser ruleEngineUser = new RuleEngineUser();
        ruleEngineUser.setPassword(MD5Utils.encrypt(forgotRequest.getPassword()));
        return ruleEngineUserManager.lambdaUpdate()
                .eq(RuleEngineUser::getEmail, forgotRequest.getEmail())
                .update(ruleEngineUser);
    }

    /**
     * 获取登录人信息
     *
     * @return user
     */
    @Override
    public UserResponse getUserInfo() {
        UserData userData = Context.getCurrentUser();
        return BasicConversion.INSTANCE.convert(userData);
    }

    /**
     * 退出登录
     *
     * @return true
     */
    @Deprecated
    @Override
    public Boolean logout() {
        return true;
    }

    /**
     * 上传用户头像
     *
     * @param file 图片文件
     * @return 图片url
     */
    @Override
    public String uploadAvatar(MultipartFile file) throws IOException {
        String defaultFolder = this.properties.getDefaultFolder();
        return aliOSSClient.upload(file.getInputStream(), file.getOriginalFilename(), defaultFolder);
    }

    /**
     * 更新用户信息
     *
     * @param userInfoRequest 根据id更新用户信息
     * @return 用户信息
     */
    @Override
    public Boolean updateUserInfo(UpdateUserInfoRequest userInfoRequest) {
        if (!Context.getCurrentUser().getId().equals(userInfoRequest.getId())) {
            throw new ValidException("无权限修改!");
        }
        RuleEngineUser ruleEngineUser = this.ruleEngineUserManager.getById(userInfoRequest.getId());
        if (ruleEngineUser == null) {
            throw new ValidException("没有此用户!");
        }
        ruleEngineUser.setId(userInfoRequest.getId());
        ruleEngineUser.setEmail(userInfoRequest.getEmail());
        ruleEngineUser.setPhone(userInfoRequest.getPhone());
        ruleEngineUser.setAvatar(userInfoRequest.getAvatar());
        ruleEngineUser.setSex(userInfoRequest.getSex());
        this.ruleEngineUserManager.updateById(ruleEngineUser);
        // 更新用户信息
        String token = HttpServletUtils.getRequest().getHeader(AuthInterceptor.TOKEN);
        this.refreshUserData(token, ruleEngineUser);
        return true;
    }

    /**
     * 检查验证码是否有效
     *
     * @param email 邮箱
     * @param code  验证码
     * @param pre   redis key pre
     */
    private void checkVerifyCode(String email, Integer code, String pre) {
        String userEmailCodePre = pre + IPUtils.getRequestIp() + email;
        RBucket<Integer> rBucket = redissonClient.getBucket(userEmailCodePre);
        Integer getCode = rBucket.get();
        if (getCode == null) {
            throw new ValidationException("验证码已失效!");
        }
        if (!getCode.equals(code)) {
            throw new ValidationException("验证码错误!");
        }
    }
}
