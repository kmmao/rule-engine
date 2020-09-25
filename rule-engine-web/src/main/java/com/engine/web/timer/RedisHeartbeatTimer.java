package com.engine.web.timer;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/13
 * @since 1.0.0
 */
@Slf4j
@Component
public class RedisHeartbeatTimer {

    private static final String HEARTBEAT = "rule_engine_heartbeat";

    @Resource
    private RedissonClient redissonClient;

    /**
     * 解决长时间未使用redis导致连接中断
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void executor() {
        log.debug("connection redis heartbeat");
        RBucket<Boolean> bucket = redissonClient.getBucket(HEARTBEAT);
        bucket.set(true);
    }

}
