package cn.ruleengine.core.monitor;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/2/1
 * @since 1.0.0
 */
@Data
public class GeneralIndicator implements Indicator {

    /**
     * 普通规则id
     */
    private Integer id;

    /**
     * 普通规则code
     */
    private String code;

    /**
     * 最小耗时 单位ms
     */
    private AtomicLong minimalTime = null;
    /**
     * 最大耗时 单位ms
     */
    private AtomicLong maximumTime = new AtomicLong(0L);
    /**
     * 平均耗时 单位ms
     */
    private AtomicLong averageTime = new AtomicLong(0L);
    /**
     * 总次数
     */
    private AtomicLong totalCount = new AtomicLong(0L);

    /**
     * 结果命中次数
     */
    private AtomicLong actionCount = new AtomicLong(0L);

    /**
     * 默认结果命中次数
     */
    private AtomicLong defaultActionCount = new AtomicLong(0L);

    /**
     * 未命中次数
     */
    private AtomicLong missesCount = new AtomicLong(0L);

    public void incrementActionCount() {
        actionCount.incrementAndGet();
    }

    public void incrementTotalCount() {
        totalCount.incrementAndGet();
    }

    public void incrementDefaultActionCount() {
        defaultActionCount.incrementAndGet();
    }

    public void incrementMissesCount() {
        missesCount.incrementAndGet();
    }

}
