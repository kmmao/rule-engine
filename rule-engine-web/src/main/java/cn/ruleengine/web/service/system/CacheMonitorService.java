package cn.ruleengine.web.service.system;

import cn.ruleengine.web.vo.system.CacheMonitorResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/15
 * @since 1.0.0
 */
@Service
public class CacheMonitorService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 缓存监控信息
     *
     * @return CacheMonitorResponse
     */
    public CacheMonitorResponse getInfo() {
        CacheMonitorResponse monitorResponse = new CacheMonitorResponse();
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize);
        monitorResponse.setInfo(info);
        monitorResponse.setDbSize(dbSize);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        if (commandStats != null) {
            List<CacheMonitorResponse.CommandStats> pieList = new ArrayList<>();
            commandStats.stringPropertyNames().forEach(key -> {
                CacheMonitorResponse.CommandStats stats = new CacheMonitorResponse.CommandStats();
                String property = commandStats.getProperty(key);
                stats.setName(StringUtils.removeStart(key, "cmdstat_"));
                stats.setValue(StringUtils.substringBetween(property, "calls=", ",usec"));
                pieList.add(stats);
            });
            monitorResponse.setCommandStats(pieList);
        }
        return monitorResponse;
    }

}
