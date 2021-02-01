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
public class AtomicGeneralIndicator implements Indicator  {

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
     * 总耗时 单位ms
     */
    private AtomicLong totalTime = new AtomicLong(0L);
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

    /**
     * 添加耗时
     *
     * @param costTime 耗时 单位ms
     */
    @Override
    public void addTimeConsuming(Long costTime) {
        totalTime.addAndGet(costTime);
        // 计算平均时间
        averageTime.set(totalTime.get() / totalCount.get());
        if (costTime > maximumTime.get()) {
            maximumTime.set(costTime);
        }
        if (minimalTime == null) {
            minimalTime = new AtomicLong(costTime);
        } else {
            if (costTime < minimalTime.get()) {
                minimalTime.set(costTime);
            }
        }
    }

    @Override
    public void incrementActionCount() {
        actionCount.incrementAndGet();
    }

    @Override
    public void incrementTotalCount() {
        totalCount.incrementAndGet();
    }

    @Override
    public void incrementDefaultActionCount() {
        defaultActionCount.incrementAndGet();
    }

    @Override
    public void incrementMissesCount() {
        missesCount.incrementAndGet();
    }

}
