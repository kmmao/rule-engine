package cn.ruleengine.web.store.entity;

import com.baomidou.mybatisplus.annotation.*;
import cn.ruleengine.core.rule.Rule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author dqw
 * @since 2020-07-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RuleEngineRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String code;

    private String description;

    private Integer workspaceId;

    private String workspaceCode;

    private Integer createUserId;

    private String createUserName;

    private Integer status;

    private Integer actionType;

    private String actionValueType;

    private String actionValue;
    /**
     * 0启用 1不启用
     */
    private Integer enableDefaultAction;

    private String defaultActionValue;

    private String defaultActionValueType;

    private Integer defaultActionType;

    /**
     * 注意，规则模拟运行不会触发
     * <p>
     * {@link Rule.AbnormalAlarm}
     */
    private String abnormalAlarm;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;


}
