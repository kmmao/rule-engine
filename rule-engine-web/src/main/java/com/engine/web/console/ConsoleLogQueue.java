/**
 * Copyright @2020 dingqianwen
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

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 创建一个阻塞队列，作为日志系统输出的日志的一个临时载体
 *
 * @author dingqianwen
 */
@Slf4j
public class ConsoleLogQueue {

    /**
     * 阻塞队列
     */
    private final BlockingQueue<ConsoleLog> blockingQueue = new LinkedBlockingQueue<>(10000);

    private static final ConsoleLogQueue CONSOLE_LOG_QUEUE = new ConsoleLogQueue();

    private ConsoleLogQueue() {
    }

    public static ConsoleLogQueue getInstance() {
        return CONSOLE_LOG_QUEUE;
    }

    /**
     * 消息入队
     */
    public void push(ConsoleLog log) {
        this.blockingQueue.add(log);
    }

    /**
     * 消息出队
     */
    public ConsoleLog poll() {
        ConsoleLog result = null;
        try {
            result = this.blockingQueue.take();
        } catch (InterruptedException e) {
            log.warn("消息出列异常:{}", e.getMessage());
        }
        return result;
    }
}
