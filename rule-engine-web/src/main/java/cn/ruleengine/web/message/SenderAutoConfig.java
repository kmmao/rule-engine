package cn.ruleengine.web.message;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @ClassName SenderAutoConfig
 * @Description
 * @Author AaronPiUC
 * @Date 2020/12/10 11:38
 */
@Configuration
public class SenderAutoConfig {
    @Bean
    public SpiFactoryBean senderSpiPoxy(ApplicationContext applicationContext) {
        return new SpiFactoryBean(applicationContext, ISender.class);
    }

    @Bean
    @Primary
    public ISender senderProxy(SpiFactoryBean spiFactoryBean) throws Exception {
        return (ISender) spiFactoryBean.getObject();
    }
}
