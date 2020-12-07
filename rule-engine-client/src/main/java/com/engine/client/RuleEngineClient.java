package com.engine.client;


import javax.annotation.Resource;

import org.springframework.web.client.RestTemplate;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/6
 * @since 1.0.0
 */
public class RuleEngineClient {

    @Resource
    private RuleEngineProperties ruleEngineProperties;
    @Resource
    private RestTemplate restTemplate;

    public RuleEngineClient() {

    }
    // TODO: 2020/12/6 ...

}
