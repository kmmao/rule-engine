package cn.ruleengine.client;

import cn.ruleengine.client.fegin.GeneralRuleInterface;
import cn.ruleengine.client.result.BatchOutput;
import cn.ruleengine.client.result.Output;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/9
 * @since 1.0.0
 */
public class AppTest {

    private final RuleEngineClient ruleEngineClient = new RuleEngineClient() {
        {
            RuleEngineProperties ruleEngineProperties = new RuleEngineProperties();
            //ruleEngineProperties.setUrl("http://localhost");
            this.setGeneralRuleInterface(Feign.builder()
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .options(new Request.Options(6000, 3500))
                    .retryer(new Retryer.Default(5000, 5000, 3))
                    .target(GeneralRuleInterface.class, ruleEngineProperties.getBaseUrl()));
            this.setRuleEngineProperties(ruleEngineProperties);
        }
    };

    @Test
    public void single() {
        RuleMode ruleMode = new RuleMode();
        ruleMode.setPhone("1343493849384");
        GeneralRule generalRule = this.ruleEngineClient.generalRule();
        Output execute = generalRule.execute(ruleMode);
        System.out.println(execute);
    }

    @Test
    public void isExists() {
        GeneralRule generalRule = this.ruleEngineClient.generalRule();
        boolean exists = generalRule.isExists("phoneRuletest");
        System.out.println(exists);
    }

    @Test
    public void batch() {
        List<Object> ruleModes = new ArrayList<>();
        {
            RuleMode ruleMode = new RuleMode();
            ruleMode.setPhone("1343493849384");
            ruleModes.add(ruleMode);
        }
        {
            RuleMode ruleMode = new RuleMode();
            ruleMode.setPhone("13434938493842");
            ruleModes.add(ruleMode);
        }
        GeneralRule generalRule = this.ruleEngineClient.generalRule();
        List<BatchOutput> batchOutputs = generalRule.batchExecute(100, -1L, ruleModes);
        batchOutputs.forEach(System.out::println);
    }

}
