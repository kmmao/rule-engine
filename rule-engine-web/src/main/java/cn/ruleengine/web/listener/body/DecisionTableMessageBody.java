package cn.ruleengine.web.listener.body;

import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
@Data
public class DecisionTableMessageBody implements Serializable {

    private static final long serialVersionUID = 1L;

    private DecisionTableMessageBody.Type type;

    private String workspaceCode;

    private Integer workspaceId;

    private String decisionTableCode;

    public enum Type {
        /**
         * 决策表加载，以及移除
         */
        LOAD, UPDATE, REMOVE
    }
}
