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
package com.engine.web.aspect;

import com.engine.web.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/8/13
 * @since 1.0.0
 */
@Component
@Aspect
@Slf4j
@Order(-10)
public class LockAspect {

    @Resource
    private RedissonClient redissonClient;

    private static final String LOCK_KEY_PRE = "boot_lock_key_pre";

    @Around("@annotation(lock)")
    public Object around(ProceedingJoinPoint joinPoint, Lock lock) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        long time = lock.timeOut();
        TimeUnit timeUnit = lock.timeUnit();
        String className = joinPoint.getTarget().getClass().getName();
        // TODO: 2020/9/13 待优化，可以加指定的参数
        String lockKey = LOCK_KEY_PRE + className + "_" + method.getName();
        RLock rLock = redissonClient.getLock(lockKey);
        rLock.lock(time, timeUnit);
        log.info("{}方法加锁成功，Lock Key:{}", method.getName(), lockKey);
        try {
            return joinPoint.proceed();
        } finally {
            rLock.unlock();
            log.info("{}方法锁已经移除，Lock Key:{}", method.getName(), lockKey);
        }
    }
}
