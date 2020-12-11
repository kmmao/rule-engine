package cn.ruleengine.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/26
 * @since 1.0.0
 */
@AllArgsConstructor
public enum RuleStatus {

    /**
     * 规则的各种状态
     */
    EDIT(0), WAIT_PUBLISH(1), PUBLISHED(2);

    @Getter
    private Integer status;

}
