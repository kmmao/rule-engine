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
public class RuleSetMessageBody implements Serializable {

    private static final long serialVersionUID = 1L;

    private RuleSetMessageBody.Type type;

    private String workspaceCode;

    private Integer workspaceId;

    private String ruleSetCode;

    public enum Type {
        /**
         * 规则加载，以及移除
         */
        LOAD, UPDATE, REMOVE
    }
}
