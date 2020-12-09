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
package com.engine.client;

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
@ConfigurationProperties(
        prefix = "rule.engine"
)
public class RuleEngineProperties {

    /**
     * 规则引擎调用地址
     */
    private String serverUrl = "http://ruleserver.cn";
    /**
     * 工作空间code
     */
    private String workspaceCode = "default";

    private String accessKeyId;

    private String accessKeySecret;


    public String getExecuteRuleUrl() {
        return this.getServerUrl().concat("/ruleEngine/execute");
    }

    public String getBatchExecuteRuleUrl() {
        return this.getServerUrl().concat("/ruleEngine/batchExecute");
    }

    public String getIsExistsRuleUrl() {
        return this.getServerUrl().concat("/ruleEngine/isExists");
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getWorkspaceCode() {
        return workspaceCode;
    }

    public void setWorkspaceCode(String workspaceCode) {
        this.workspaceCode = workspaceCode;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
