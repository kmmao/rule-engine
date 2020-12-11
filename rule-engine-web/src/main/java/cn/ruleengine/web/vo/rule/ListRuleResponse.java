package cn.ruleengine.web.vo.rule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/24
 * @since 1.0.0
 */
@Data
public class ListRuleResponse {

    private Integer id;

    private String name;

    private String code;

    private String createUserName;

    /**
     * 是否发布
     */
    private Boolean isPublish;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
