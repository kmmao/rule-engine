package cn.ruleengine.web;


import lombok.Getter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/8/16
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@ActiveProfiles(value = "test")
public class BaseTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @Getter
    private MockMvc mockMvc;
    @Resource
    private WebApplicationContext webApplicationContext;
    @Resource
    public ApplicationContext applicationContext;

    @Before
    public void create() {
        log.info("开始单元测试");
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @After
    public void after() throws Exception {
        log.info("退出单元测试");
    }

    @Test
    public void temp() {
    }
}
