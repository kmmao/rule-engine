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
