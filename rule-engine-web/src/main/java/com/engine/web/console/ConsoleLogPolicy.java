package com.engine.web.console;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import cn.hutool.system.SystemUtil;
import com.engine.web.util.DateUtils;


import java.util.Date;

/**
 * 控制台日志代理
 *
 * @author dingqianwen
 */
public class ConsoleLogPolicy<E> extends ConsoleAppender<E> {

    @Override
    protected void append(E eventObject) {
        if (!isStarted()) {
            return;
        }
        LoggingEvent loggingEvent = (LoggingEvent) eventObject;

        ConsoleLog consoleLog = new ConsoleLog();
        consoleLog.setLoggerName(loggingEvent.getLoggerName());
        consoleLog.setBody(loggingEvent.getFormattedMessage());
        consoleLog.setTimestamp(DateUtils.formatDateTime(new Date(loggingEvent.getTimeStamp())));
        consoleLog.setThreadName(loggingEvent.getThreadName());
        consoleLog.setLevel(loggingEvent.getLevel().toString());
        consoleLog.setCurrentPID(SystemUtil.getCurrentPID());

        ConsoleLogQueue.getInstance().push(consoleLog);

        this.subAppend(eventObject);
    }

}
