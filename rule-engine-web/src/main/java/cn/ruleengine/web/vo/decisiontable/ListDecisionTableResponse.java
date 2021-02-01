package cn.ruleengine.web.vo.decisiontable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author liqian
 * @date 2020/12/27
 */
@Data
public class ListDecisionTableResponse {

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
