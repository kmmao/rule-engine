package cn.ruleengine.web.config;

import cn.ruleengine.web.BaseTest;
import cn.ruleengine.web.vo.workspace.Workspace;
import org.junit.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;

import javax.annotation.Resource;

import static cn.ruleengine.web.service.WorkspaceService.CURRENT_WORKSPACE;

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

    @Test
    public void test1() {
        this.redissonClient.getBucket(CURRENT_WORKSPACE + 1);
        {
            long l = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                RBucket<Workspace> bucket = this.redissonClient.getBucket(CURRENT_WORKSPACE + 1);
                if (bucket.isExists()) {
                    System.out.println(bucket.get());
                }
            }
            System.out.println("a ms:" + (System.currentTimeMillis() - l));
        }
        {
            long l = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                RBucket<Workspace> bucket = this.redissonClient.getBucket(CURRENT_WORKSPACE + 1);
                Workspace workspace = bucket.get();
                if (workspace != null) {
                    System.out.println(workspace);
                }
            }
            System.out.println("b ms:" + (System.currentTimeMillis() - l));
        }
    }

}
