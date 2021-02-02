package cn.ruleengine.core.monitor;

import lombok.Data;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2021/2/1
 * @since 1.0.0
 */
@Data
public class Monitor implements Closeable {


    /**
     * 普通规则监控数据
     * <p>
     * 注意：目前不支持集群监控
     * 集群缓存数据同步问题待完成
     */
    private Map<Integer, Indicator> generalRuleMonitor = new HashMap<>();

    public void initGeneralRuleMonitor(Integer id, Indicator indicator) {
        this.generalRuleMonitor.put(id, indicator);
    }

    public Indicator getGeneralRuleMonitor(Integer id) {
        return this.generalRuleMonitor.get(id);
    }

    /**
     * 返回所有规则的总的 调用次数，以及平均耗时...
     *
     * @return data
     */
    public Object getAllGeneralRuleMonitor() {

        return null;
    }

    @Override
    public void close() {
        this.generalRuleMonitor.clear();
    }

}
