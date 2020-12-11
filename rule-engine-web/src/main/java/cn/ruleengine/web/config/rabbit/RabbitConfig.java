package cn.ruleengine.web.config.rabbit;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈消息队列配置〉
 *
 * @author 丁乾文
 * @create 2019/8/14
 * @since 1.0.0
 */
@Component
public class RabbitConfig {

    /**
     * 使用json传输,即使没有实现序列化接口也可以
     *
     * @return MessageConverter
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
