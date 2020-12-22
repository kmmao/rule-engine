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
package cn.ruleengine.web.aspect;

import cn.ruleengine.web.annotation.DataPermission;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.exception.DataPermissionException;
import cn.ruleengine.web.service.DataPermissionService;
import cn.ruleengine.web.vo.user.UserData;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/11/22
 * @since 1.0.0
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "auth", havingValue = "${auth.enable}")
@Aspect
public class DataPermissionAspect {

    @Resource
    private DataPermissionService dataPermissionService;

    /**
     * 解析spel表达式
     */
    private ExpressionParser parser = new SpelExpressionParser();
    /**
     * 将方法参数纳入Spring管理
     */
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 此注解主要防止通过url越权操作数据
     *
     * @param joinPoint      joinPoint
     * @param dataPermission 数据权限注解
     */
    @Around("@annotation(dataPermission)")
    public Object around(ProceedingJoinPoint joinPoint, DataPermission dataPermission) throws Throwable {
        UserData userData = Context.getCurrentUser();
        log.debug("开始校验数据权限,用户Id:{},是否为管理员：{}，注解信息：{}", userData.getId(), userData.getIsAdmin(), dataPermission);
        if (userData.getIsAdmin()) {
            return joinPoint.proceed();
        }
        //获取参数对象数组
        Object[] args = joinPoint.getArgs();
        //获取方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //获取方法参数名
        String[] params = this.discoverer.getParameterNames(method);
        if (params == null || params.length == 0) {
            throw new ValidationException("没有获取到任何参数");
        }
        // 将参数纳入Spring管理
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }
        Expression expression = this.parser.parseExpression(dataPermission.id());
        Serializable id = expression.getValue(context, Serializable.class);
        // 不影响后续逻辑
        if (id == null) {
            log.info("校验数据权限，当前Id为null，跳过");
            return joinPoint.proceed();
        }
        Boolean result = this.dataPermissionService.validDataPermission(id, dataPermission);
        if (result) {
            return joinPoint.proceed();
        } else {
            throw new DataPermissionException("你没有权限执行此操作");
        }
    }

}
