package com.engine.web.aspect;

import com.engine.web.annotation.DataPermission;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

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
     * 此注解主要防止通过url越权操作数据
     *
     * @param joinPoint      joinPoint
     * @param dataPermission 数据权限注解
     */
    @Around("@annotation(dataPermission)")
    public Object around(ProceedingJoinPoint joinPoint, DataPermission dataPermission) throws Throwable {
        // TODO: 2020/11/22
        return joinPoint.proceed();
    }
    
}
