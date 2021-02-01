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
     */
    private Map<Integer, Indicator> generalRuleMonitor = new HashMap<>();

    public void initGeneralRuleMonitor(Integer id, Indicator indicator) {
        this.generalRuleMonitor.put(id, indicator);
    }

    public Indicator getGeneralRuleMonitor(Integer id) {
        return this.generalRuleMonitor.get(id);
    }

    @Override
    public void close() {
        this.generalRuleMonitor.clear();
    }

}
