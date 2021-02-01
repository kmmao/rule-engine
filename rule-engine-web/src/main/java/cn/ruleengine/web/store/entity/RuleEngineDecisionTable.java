package cn.ruleengine.web.store.entity;

import com.baomidou.mybatisplus.annotation.*;

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
 * @since 2020-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RuleEngineDecisionTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String code;

    private String description;

    private Integer workspaceId;

    private String workspaceCode;

    private String tableData;

    /**
     * 决策表执行策略类型
     */
    private Integer strategyType;

    private Integer createUserId;

    private String createUserName;

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

    private String referenceData;

    private String abnormalAlarm;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;


}
