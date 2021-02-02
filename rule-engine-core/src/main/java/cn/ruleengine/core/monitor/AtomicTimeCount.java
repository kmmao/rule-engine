package cn.ruleengine.core.monitor;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/2/2
 * @since 1.0.0
 */
@Data
public class AtomicTimeCount {

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
     * 平均耗时 单位ms
     */
    private AtomicLong averageTime = new AtomicLong(0L);

    /**
     * 总耗时 单位ms
     */
    private AtomicLong totalTime = new AtomicLong(0L);

}
