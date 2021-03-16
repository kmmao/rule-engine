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

import cn.hutool.core.util.ArrayUtil;
import cn.ruleengine.web.util.HttpServletUtils;
import com.alibaba.fastjson.JSON;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * 〈一句话功能简述〉<br>
 * 〈请求日志〉
 *
 * @author 丁乾文
 * @create 2019/8/13
 * @since 1.0.0
 */
@Component
@Aspect
@Slf4j
public class RequestLogAspect {

    /**
     * 打印请求日志
     *
     * @param joinPoint 连接点
     * @return 被代理类方法执行结果
     * @throws Throwable .
     */
    @Around("execution(* cn.ruleengine.web.controller..*.*(..))&&!execution(* cn.ruleengine.web.controller.exception.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder sb = new StringBuilder("\n");
        long start = System.currentTimeMillis();
        try {
            sb.append("┏━━━━━━━━请求日志━━━━━━━━\n");
            sb.append("┣ 链接: ").append(HttpServletUtils.getRequest().getRequestURL()).append("\n");
            /*
             * 以下代码就是测试用玩的，性能影响其实也微乎其微，生产环境可以选择去除
             * <br>
             * 如果有什么更好更快的实现方式，欢迎提出宝贵建议
             * <br>
             * 其实也没啥用，想去就去掉吧！
             */
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            // 获取调用的方法所在行号
            int methodLineNumber = this.getMethodLineNumber(method, joinPoint.getArgs());
            Class<?> declaringClass = method.getDeclaringClass();
            sb.append("┣ 地址: ").append(declaringClass.getName()).append("(").append(declaringClass.getSimpleName()).append(".java:").append(methodLineNumber).append(")").append("\n");
            // end
            sb.append("┣ 参数: ").append(JSON.toJSONString(argsExcludeClass(joinPoint.getArgs()))).append("\n");
            Object proceed = joinPoint.proceed();
            sb.append("┣ 结果: ").append(JSON.toJSONString(proceed)).append("\n");
            return proceed;
        } catch (Throwable throwable) {
            sb.append("┣ 异常: ").append(throwable).append("\n");
            throw throwable;
        } finally {
            sb.append("┣ 时间: ").append(System.currentTimeMillis() - start).append("ms\n");
            sb.append("┗━━━━━━━━━━━━━━━━━━━━━━━");
            log.info("{}", sb);
        }
    }

    /**
     * 参数过滤调一部分类,否则引起问题
     *
     * @param args 参数
     * @return Object[]
     */
    private static Object[] argsExcludeClass(Object[] args) {
        return Stream.of(args)
                .filter(f -> !(f instanceof HttpServletResponse))
                .filter(f -> !(f instanceof HttpServletRequest))
                .filter(f -> !(f instanceof MultipartFile))
                .filter(f -> !(f instanceof Exception)).toArray();
    }

    /**
     * 获取调用的方法所在行号
     *
     * @param method 方法
     * @param args   方法参数，如果不传，则默认调用若干个重载方法中的第一个
     * @return 行号
     * @throws NotFoundException NotFoundException
     */
    private int getMethodLineNumber(Method method, Object[] args) throws NotFoundException {
        ClassPool classPool = ClassPool.getDefault();
        Class<?> declaringClass = method.getDeclaringClass();
        CtClass ctClass = classPool.get(declaringClass.getName());
        CtMethod declaredMethod;
        if (ArrayUtil.isEmpty(args)) {
            declaredMethod = ctClass.getDeclaredMethod(method.getName());
        } else {
            CtClass[] ctClasses = new CtClass[args.length];
            for (int i = 0; i < args.length; i++) {
                ctClasses[i] = classPool.get(args[i].getClass().getName());
            }
            declaredMethod = ctClass.getDeclaredMethod(method.getName(), ctClasses);
        }
        return declaredMethod.getMethodInfo().getLineNumber(0) - 1;
    }


}
