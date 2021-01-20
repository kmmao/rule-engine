package cn.ruleengine.web.service;

import cn.ruleengine.core.DecisionTableEngine;
import cn.ruleengine.core.GeneralRuleEngine;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.RuleSetEngine;
import cn.ruleengine.web.vo.monitor.CacheInfoResponse;
import cn.ruleengine.web.vo.monitor.EngineInfoResponse;
import cn.ruleengine.web.vo.monitor.ServerInfoResponse;
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
public class MonitorService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private RuleEngineConfiguration ruleEngineConfiguration;
    @Resource
    private GeneralRuleEngine generalRuleEngine;
    @Resource
    private RuleSetEngine ruleSetEngine;
    @Resource
    private DecisionTableEngine decisionTableEngine;

    /**
     * 缓存监控信息
     *
     * @return CacheMonitorResponse
     */
    public CacheInfoResponse cacheInfo() {
        CacheInfoResponse monitorResponse = new CacheInfoResponse();
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize);
        monitorResponse.setInfo(info);
        monitorResponse.setDbSize(dbSize);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        if (commandStats != null) {
            List<CacheInfoResponse.CommandStats> pieList = new ArrayList<>();
            commandStats.stringPropertyNames().forEach(key -> {
                CacheInfoResponse.CommandStats stats = new CacheInfoResponse.CommandStats();
                String property = commandStats.getProperty(key);
                stats.setName(StringUtils.removeStart(key, "cmdstat_"));
                stats.setValue(StringUtils.substringBetween(property, "calls=", ",usec"));
                pieList.add(stats);
            });
            monitorResponse.setCommandStats(pieList);
        }
        return monitorResponse;
    }

    /**
     * 服务监控
     *
     * @return info
     */
    public ServerInfoResponse serverInfo() {
        return null;
    }

    /**
     * 当前工作空间规则引擎监控数据，系统中变量数量，引擎中规则数量，函数缓存数量，已发布数据引擎执行次数
     *
     * @return info
     */
    public EngineInfoResponse engineInfo() {
        return null;
    }


}
