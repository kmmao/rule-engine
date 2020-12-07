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

@Data
@ConfigurationProperties(
        prefix = "rule.engine"
)
public class RuleEngineProperties {

    /**
     * 规则引擎调用地址
     */
    private String url;
    /**
     * 工作空间code
     */
    private String workspaceCode;

    private String accessKeyId;

    private String accessKeySecret;

}
