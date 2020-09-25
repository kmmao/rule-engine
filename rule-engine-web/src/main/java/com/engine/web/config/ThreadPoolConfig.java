package com.engine.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 〈一句话功能简述〉<br>
 * 〈线程池配置〉
 *
 * @author 丁乾文
 * @create 2019/8/13
 * @since 1.0.0
 */
@Component
public class ThreadPoolConfig {

    /**
     * 线程池配置
     *
     * @return ThreadPoolTaskExecutor
     */
    @Primary
    @Bean(name = "taskExecutor", initMethod = "initialize", destroyMethod = "destroy")
    @ConfigurationProperties(prefix = "thread.pool")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
