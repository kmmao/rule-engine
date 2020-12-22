/**
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ruleengine.web.interceptor;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.web.annotation.NoAuth;
import cn.ruleengine.web.annotation.RoleAuth;
import cn.ruleengine.web.enums.ErrorCodeEnum;
import cn.ruleengine.web.util.JWTUtils;
import cn.ruleengine.web.util.ResponseUtils;
import cn.ruleengine.web.vo.base.response.BaseResult;
import cn.ruleengine.web.vo.user.UserData;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * 〈一句话功能简述〉<br>
 * 〈Token认证
 * 如果不需要token认证,则在controller的方法上加上NoAuth注解〉
 *
 * @author 丁乾文
 * @create 2019/8/13
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractTokenInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private RedissonClient redissonClient;

    public static final String TOKEN = "token";

    public static final ThreadLocal<UserData> USER = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //如果配置文件中开启了验证权限
        if (!enable()) {
            return true;
        }
        //如果Controller有NoAuth注解的方法,不需要验证权限
        if (isHaveAccess(handler)) {
            log.debug("此{}接口不需要认证权限", request.getRequestURI());
            return true;
        }
        //获取Header中的token
        String token = request.getHeader(TOKEN);
        if (Validator.isEmpty(token)) {
            log.warn("Token为空");
            ResponseUtils.responseJson(BaseResult.err(ErrorCodeEnum.RULE10010004.getCode(), ErrorCodeEnum.RULE10010004.getMsg()));
            return false;
        }
        try {
            // 对token进行验证
            JWTUtils.verifyToken(token);
        } catch (Exception e) {
            log.warn("Token验证不通过,Token:{}", token);
            ResponseUtils.responseJson(BaseResult.err(ErrorCodeEnum.RULE10011039.getCode(), ErrorCodeEnum.RULE10011039.getMsg()));
            return false;
        }
        // 从redis获取到用户信息保存到本地
        RBucket<UserData> bucket = this.redissonClient.getBucket(token);
        // 获取redis中存的用户信息
        UserData userData = bucket.get();
        if (userData == null) {
            log.warn("验证信息失效!");
            ResponseUtils.responseJson(BaseResult.err(ErrorCodeEnum.RULE99990402.getCode(), ErrorCodeEnum.RULE99990402.getMsg()));
            return false;
        }
        //更新过期时间
        bucket.expire(JWTUtils.keepTime, TimeUnit.MILLISECONDS);
        //校验类上获取方法上的注解值roleCode是否匹配
        RoleAuth roleAuth = this.getRoleAuth(handler);
        //如果存在需要验证权限,并且权限没有验证通过时,提示无权限访问
        if (roleAuth != null && !this.auth(roleAuth.code(), roleAuth.transfer(), userData)) {
            //Token验证通过,但是用户无权限访问
            log.warn("无权限访问,User:{}", userData);
            ResponseUtils.responseJson(BaseResult.err(ErrorCodeEnum.RULE99990401.getCode(), ErrorCodeEnum.RULE99990401.getMsg()));
            return false;
        }
        log.debug("权限验证通过,User:{}", userData);
        USER.set(userData);
        return true;
    }

    private RoleAuth getRoleAuth(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return null;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Class<?> beanType = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();
        boolean beanRoleAuth = beanType.isAnnotationPresent(RoleAuth.class);
        boolean methodRoleAuth = method.isAnnotationPresent(RoleAuth.class);
        if (!(beanRoleAuth || methodRoleAuth)) {
            return null;
        }
        RoleAuth roleAuth;
        //如果方法上有RoleAuth注解,获取方法上注解的roleCode,否则执行类上的roleCode校验
        if (methodRoleAuth) {
            log.info("{}方法上存在RoleAuth注解", method.getName());
            roleAuth = method.getAnnotation(RoleAuth.class);
        } else {
            log.info("{}类上存在RoleAuth注解", beanType.getSimpleName());
            roleAuth = beanType.getAnnotation(RoleAuth.class);
        }
        return roleAuth;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        USER.remove();
    }

    /**
     * 是否需要验证权限
     *
     * @param handler handler
     * @return 返回true时不鉴权
     */
    private boolean isHaveAccess(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        NoAuth responseBody = AnnotationUtils.findAnnotation(method, NoAuth.class);
        return responseBody != null;
    }

    /**
     * 是否开启鉴权
     *
     * @return 返回true时, 启用
     */
    public abstract boolean enable();


    /**
     * 权限鉴定 注解方式 对访问人员工code进行验证
     *
     * @param code     权限code
     * @param transfer transfer
     * @param userData 用户数据
     * @return true
     */
    public abstract boolean auth(String[] code, boolean transfer, UserData userData);

}

