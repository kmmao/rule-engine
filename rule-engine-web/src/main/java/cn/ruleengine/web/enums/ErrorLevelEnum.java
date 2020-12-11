package cn.ruleengine.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 〈一句话功能简述〉<br>
 * 〈根据不同的错误级别,通知不同级别的管理人员〉
 *
 * @author 丁乾文
 * @create 2019/8/27
 * @since 1.0.0
 */
@AllArgsConstructor
public enum ErrorLevelEnum {

    /**
     * 严重问题
     */
    SERIOUS(1, "严重问题"),
    /**
     * 运行规则时发生的异常信息
     */
    RUN_RULE(2, "规则执行异常"),
    /**
     * 其他问题
     */
    OTHER(3, "其他问题");

    @Getter
    private int value;
    @Getter
    private String name;
}
