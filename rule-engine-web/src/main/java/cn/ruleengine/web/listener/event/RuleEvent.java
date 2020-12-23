package cn.ruleengine.web.listener.event;

import cn.ruleengine.web.listener.body.RuleMessageBody;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020-12-23 17:55
 * @since 1.0.0
 */
public class RuleEvent extends ApplicationEvent {

    @Getter
    private RuleMessageBody ruleMessageBody;

    public RuleEvent(RuleMessageBody ruleMessageBody) {
        super(ruleMessageBody);
        this.ruleMessageBody = ruleMessageBody;
    }

}
