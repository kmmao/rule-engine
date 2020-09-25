package com.engine.web.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @since 2020-09-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RuleEngineMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private String name;

    private String description;

    private Integer parentId;

    /**
     * 1导航栏菜单,2侧边栏菜单
     */
    private Boolean type;

    private String icon;

    private String url;

    private Integer sort;

    /**
     * 菜单路径
     */
    private String menuPath;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Boolean deleted;


}
