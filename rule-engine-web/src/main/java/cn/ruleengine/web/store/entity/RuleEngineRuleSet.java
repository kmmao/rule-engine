package cn.ruleengine.web.store.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author dqw
 * @since 2021-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RuleEngineRuleSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String code;

    private String description;

    private Integer workspaceId;

    private String workspaceCode;

    /**
     * 当前规则的最新状态
     */
    private Integer status;
    /**
     * 当前最新的版本号
     */
    private String currentVersion;
    /**
     * 当前已发布的版本号
     */
    private String publishVersion;


    /**
     * 执行策略类型
     */
    private Integer strategyType;

    private Integer createUserId;

    private String createUserName;

    private Integer enableDefaultRule;

    private Integer defaultRuleId;

    private String referenceData;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;


}
