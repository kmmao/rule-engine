package com.engine.web.aspect;

import com.engine.core.exception.ValidException;
import com.engine.web.annotation.DataPermission;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

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
@Component
@Aspect
public class DataPermissionAspect {

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
        //获取参数对象数组
        Object[] args = joinPoint.getArgs();
        //获取方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //获取方法参数名
        String[] params = this.discoverer.getParameterNames(method);
        if (params == null || params.length == 0) {
            throw new ValidException("没有获取到任何参数");
        }
        //将参数纳入Spring管理
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }
        Expression expression = this.parser.parseExpression(dataPermission.id());
        Serializable id = expression.getValue(context, Serializable.class);
        System.out.println("============>" + id);
        // TODO: 2020/11/22  ...
        return joinPoint.proceed();
    }

}
