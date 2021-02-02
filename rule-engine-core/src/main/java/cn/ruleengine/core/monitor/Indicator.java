package cn.ruleengine.core.monitor;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/2/1
 * @since 1.0.0
 */
public interface Indicator {
    /**
     * 添加耗时
     *
     * @param costTime 耗时 单位ms
     */
    void addTimeConsuming(Long costTime);

    /**
     * 添加结果执行次数
     */
    void incrementActionCount();

    /**
     * 添加执行总数
     */
    void incrementTotalCount();

    /**
     * 添加默认执行次数
     */
    void incrementDefaultActionCount();

    /**
     * 添加未命中执行次数
     */
    void incrementMissesCount();

    /**
     * 失败异常数
     */
    void incrementErrorCount();
}
