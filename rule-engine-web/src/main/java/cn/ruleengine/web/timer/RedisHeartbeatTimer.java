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
package cn.ruleengine.web.timer;

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
    @Scheduled(cron = "*/20 * * * * ?")
    public void executor() {
        log.debug("connection redis heartbeat");
        RBucket<Boolean> bucket = redissonClient.getBucket(HEARTBEAT);
        bucket.set(true);
    }

}
