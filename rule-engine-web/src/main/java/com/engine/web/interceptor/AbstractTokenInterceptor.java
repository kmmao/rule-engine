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
package com.engine.web.interceptor;

import cn.hutool.core.lang.Validator;
import com.engine.web.annotation.NoAuth;
import com.engine.web.annotation.RoleAuth;
import com.engine.web.store.entity.RuleEngineUser;
import com.engine.web.util.CookieUtils;
import com.engine.web.util.JWTUtils;
import com.engine.web.util.ResponseUtils;
import com.engine.web.vo.base.response.BaseResult;
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

import static com.engine.web.enums.ErrorCodeEnum.*;


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

    public static final ThreadLocal<RuleEngineUser> USER = new ThreadLocal<>();

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
        //获取浏览器Cookie中的token
        String token = CookieUtils.get(TOKEN);
        if (Validator.isEmpty(token)) {
            log.warn("Token为空");
            ResponseUtils.responseJson(BaseResult.err(BOOT10010004.getCode(), BOOT10010004.getMsg()));
            return false;
        }
        String tokenNew;
        try {
            // 对token进行验证与更新
            tokenNew = JWTUtils.updateToken(token);
        } catch (Exception e) {
            log.warn("Token验证不通过!Token:{}", token);
            ResponseUtils.responseJson(BaseResult.err(BOOT10011039.getCode(), BOOT10011039.getMsg()));
            return false;
        }
        //从redis获取到用户信息保存到本地
        RBucket<RuleEngineUser> bucket = redissonClient.getBucket(token);
        if (bucket.get() == null) {
            log.warn("验证信息失效!");
            ResponseUtils.responseJson(BaseResult.err(BOOT99990402.getCode(), BOOT99990402.getMsg()));
            return false;
        }
        //更新过期时间
        bucket.expire(JWTUtils.keepTime, TimeUnit.MILLISECONDS);
        //获取redis中存的用户信息
        RuleEngineUser ruleEngineUser = bucket.get();
        //保存新的token名称
        bucket.rename(tokenNew);
        //把更新的TOKEN响应到浏览器Cookie
        CookieUtils.set(TOKEN, tokenNew);
        //校验类上获取方法上的注解值roleCode是否匹配
        RoleAuth roleAuth = getRoleAuth(handler);
        //如果存在需要验证权限,并且权限没有验证通过时,提示无权限访问
        if (roleAuth != null && !auth(roleAuth.code(), roleAuth.transfer(), ruleEngineUser.getId())) {
            //Token验证通过,但是用户无权限访问
            log.warn("无权限访问,User:{}", ruleEngineUser);
            ResponseUtils.responseJson(BaseResult.err(BOOT99990401.getCode(), BOOT99990401.getMsg()));
            return false;
        }
        log.debug("权限验证通过,User:{}", ruleEngineUser);
        USER.set(ruleEngineUser);
        return true;
    }

    private RoleAuth getRoleAuth(Object handler) {
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
     * @param userId   用户id
     * @return true
     */
    public abstract boolean auth(String[] code, boolean transfer, Integer userId);

}

