package cn.ruleengine.client;

import cn.ruleengine.client.fegin.RuleInterface;
import cn.ruleengine.client.result.OutPut;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.junit.Test;


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
            RuleEngineProperties ruleEngineProperties = new RuleEngineProperties();
            this.setRuleInterface(Feign.builder()
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .options(new Request.Options(6000, 3500))
                    .retryer(new Retryer.Default(5000, 5000, 3))
                    .target(RuleInterface.class, ruleEngineProperties.getUrl()));
            this.setRuleEngineProperties(ruleEngineProperties);
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
