package cn.ruleengine.web.rule;

import cn.ruleengine.client.EnableRuleEngine;
import cn.ruleengine.client.RuleEngineClient;
import cn.ruleengine.client.result.OutPut;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/9
 * @since 1.0.0
 */
@EnableRuleEngine
@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleTest {

    @Resource
    private RuleEngineClient ruleEngineClient;

    @Test
    public void test() {
        RuleMode ruleMode = new RuleMode();
        ruleMode.setPhone("1343493849384");
        OutPut execute = this.ruleEngineClient.execute(ruleMode);
        System.out.println(execute);
    }

}
