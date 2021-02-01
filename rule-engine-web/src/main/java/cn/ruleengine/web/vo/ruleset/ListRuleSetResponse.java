package cn.ruleengine.web.vo.ruleset;

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
public class ListRuleSetResponse {

    private Integer id;

    private String name;

    private String code;

    private String createUserName;

    private String currentVersion;

    private String publishVersion;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
