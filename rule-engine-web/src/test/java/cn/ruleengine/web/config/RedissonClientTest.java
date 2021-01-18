package cn.ruleengine.web.config;

import cn.ruleengine.web.BaseTest;
import org.junit.Test;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/18
 * @since 1.0.0
 */
public class RedissonClientTest extends BaseTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void test() throws InterruptedException {
        RTopic topic1 = redissonClient.getTopic("testT");
        topic1.addListener(String.class, new MessageListener<String>() {
            @Override
            public void onMessage(CharSequence charSequence, String s) {
                System.out.println("-->" + charSequence + "  " + s);
            }
        });

        RTopic topic2 = redissonClient.getTopic("testT");
        topic2.addListener(String.class, new MessageListener<String>() {
            @Override
            public void onMessage(CharSequence charSequence, String s) {
                System.out.println("-->" + charSequence + "  " + s);
            }
        });

        RTopic testT = redissonClient.getTopic("testT");
        testT.publish("test--123");
        Thread.sleep(5000);
    }
}
