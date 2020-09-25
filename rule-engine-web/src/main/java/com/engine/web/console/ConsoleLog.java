package com.engine.web.console;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 页面控制台日志实体
 *
 * @author Caratacus
 * @link https://cloud.tencent.com/developer/article/1096792
 */
@Setter
@Getter
public class ConsoleLog {
    /**
     * 日志内容
     */
    private String body;

    private String loggerName;
    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 线程名
     */
    private String threadName;
    /**
     * 日志等级
     */
    private String level;

    private Long currentPID;
}
