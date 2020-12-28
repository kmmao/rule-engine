package cn.ruleengine.core.rule;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/28
 * @since 1.0.0
 */
@Data
public class AbnormalAlarm {
    /**
     * 是否启用
     */
    private Boolean enable = false;
    /**
     * 邮件接收人
     */
    private String[] email;

    /**
     * 规则执行超时阈值，默认3秒
     */
    private long timeOutThreshold = 3000;
}
