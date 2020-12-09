package com.engine.client;

import com.engine.client.result.OutPut;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/9
 * @since 1.0.0
 */
public class AppTest {

    private RuleEngineClient ruleEngineClient = new RuleEngineClient() {
        {
            this.setRestTemplate(new RestTemplate());
            this.setRuleEngineProperties(new RuleEngineProperties());
        }
    };

    @Test
    public void test() {
        RuleMode ruleMode = new RuleMode();
        ruleMode.setPhone("1343493849384");
        OutPut execute = this.ruleEngineClient.execute(ruleMode);
        System.out.println(execute);
    }

}
