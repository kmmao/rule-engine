package com.engine.web.message;

import com.engine.web.enums.ErrorLevelEnum;
import com.engine.web.enums.HtmlTemplatesEnum;
import com.engine.web.util.DateUtils;
import com.engine.web.util.EmailClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈Exception级别未捕获异常发送邮箱警告
 * 异步处理〉
 *
 * @author 丁乾文
 * @create 2019/8/27
 * @since 1.0.0
 */
@Component
@Slf4j
public class ExceptionMessage {

    @Resource
    private EmailClient emailClient;

    @Value("${exception.message.enable}")
    private boolean enable;
    @Value("${exception.message.recipient.serious}")
    private String[] serious;
    @Value("${exception.message.recipient.other}")
    private String[] other;
    @Value("${exception.message.recipient.runRule}")
    private String[] runRule;

    @Async
    public void send(Exception e, ErrorLevelEnum levelEnum, String... toUsers) {
        this.send(HtmlTemplatesEnum.EXCEPTION.getMsg(), e, levelEnum, toUsers);
    }

    /**
     * 判断级别,查询不同级别的管理员,发送邮件
     *
     * @param title     消息标题
     * @param e         异常信息
     * @param levelEnum 异常等级
     */
    @Async
    @SneakyThrows
    public void send(String title, Exception e, ErrorLevelEnum levelEnum, String... toUsers) {
        if (!enable) {
            log.warn("系统准备发送异常消息,但是此功能被禁用,异常原因:{},异常类型:{}", e, levelEnum.getName());
            return;
        }
        String[] users = toUsers;
        if (users == null) {
            switch (levelEnum) {
                case SERIOUS:
                    users = this.serious;
                    break;
                case RUN_RULE:
                    users = this.runRule;
                    break;
                case OTHER:
                    users = this.other;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + levelEnum);
            }
        }
        String message;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintStream pout = new PrintStream(out);) {
            e.printStackTrace(pout);
            message = new String(out.toByteArray());
        }
        Map<String, Object> params = new HashMap<>(3);
        params.put("type", e.getClass().getSimpleName());
        params.put("time", DateUtils.getDateTime());
        params.put("message", message);
        StringBuilder sb = new StringBuilder("\n");
        sb.append("┏━━━━━━━━警告邮件━━━━━━━━\n");
        sb.append("┣ 原因: ").append(message).append("\n");
        sb.append("┣ 发至: ").append(Arrays.toString(users)).append("\n");
        sb.append("┗━━━━━━━━━━━━━━━━━━━━━━━");
        log.warn("{}", sb);
        this.emailClient.sendSimpleMail(params, title, HtmlTemplatesEnum.EXCEPTION.getValue(), users);
    }

}
