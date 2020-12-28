package cn.ruleengine.web.listener.event;

import cn.ruleengine.web.listener.body.DecisionTableMessageBody;
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
public class DecisionTableEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1628296277627810450L;

    @Getter
    private DecisionTableMessageBody decisionTableMessageBody;

    public DecisionTableEvent(DecisionTableMessageBody decisionTableMessageBody) {
        super(decisionTableMessageBody);
        this.decisionTableMessageBody = decisionTableMessageBody;
    }

}
