/**
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
