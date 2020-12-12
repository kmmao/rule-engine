package cn.ruleengine.core.event;

import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/12
 * @since 1.0.0
 */
public enum EventFeature {

    /**
     * 事件模型实时生成的数据 持久化类型
     * <p>
     * Snapshot 快照 存在丢数据问题，数据一致性不能保证
     * RealTime 实时保存 性能稍微下降
     * 可同时开启，建议只开启其中一个，对与时效性没有要求的可以选择Snapshot
     */
    ENDURANCE_SNAPSHOT, ENDURANCE_REAL_TIME;

    @Getter
    private int mark;

    EventFeature() {
        this.mark = (1 << this.ordinal());
    }

}
