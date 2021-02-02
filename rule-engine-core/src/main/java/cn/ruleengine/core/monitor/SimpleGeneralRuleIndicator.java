package cn.ruleengine.core.monitor;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2021/2/1
 * @since 1.0.0
 */
@Data
public class SimpleGeneralRuleIndicator implements Indicator  {


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
    private Long minimalTime = null;
    /**
     * 最大耗时 单位ms
     */
    private Long maximumTime = 0L;
    /**
     * 平均耗时 单位ms
     */
    private Long averageTime = 0L;
    /**
     * 总耗时 单位ms
     */
    private Long totalTime = 0L;
    /**
     * 总次数
     */
    private Long totalCount = 0L;

    /**
     * 结果命中次数
     */
    private Long actionCount = 0L;

    /**
     * 默认结果命中次数
     */
    private Long defaultActionCount = 0L;

    /**
     * 未命中次数
     */
    private Long missesCount = 0L;

    @Override
    public void addTimeConsuming(Long costTime) {

    }

    @Override
    public void incrementActionCount() {

    }

    @Override
    public void incrementTotalCount() {

    }

    @Override
    public void incrementDefaultActionCount() {

    }

    @Override
    public void incrementMissesCount() {

    }

    @Override
    public void incrementErrorCount() {

    }
}
