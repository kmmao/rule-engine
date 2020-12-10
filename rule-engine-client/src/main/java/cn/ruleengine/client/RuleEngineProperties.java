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
package cn.ruleengine.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/6
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(
        prefix = "rule.engine"
)
public class RuleEngineProperties {

    /**
     * 规则引擎调用地址
     */
    private String url = "http://ruleserver.cn";
    /**
     * 工作空间code
     */
    private String workspaceCode = "default";

    private String accessKeyId = "root";

    private String accessKeySecret = "123456";

    private FeignConfig feignConfig = new FeignConfig();

    @Data
    public static class FeignConfig {

        private Request request = new Request();
        private Retryer retryer = new Retryer();

        @Data
        public static class Request {
            private int connectTimeoutMillis = 6000;
            private int readTimeoutMillis = 3500;
        }

        @Data
        public static class Retryer {
            private long period = 2000;
            private long maxPeriod = 2000;
            private int maxAttempts = 3;
        }

    }

}
