package cn.ruleengine.web.vo.rule;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
@Data
public class ExecuteRuleRequest {

    @NotEmpty(message = "工作空间编码不能为空")
    private String workspaceCode;

    @NotEmpty(message = "工作空间AccessKeyId不能为空")
    private String accessKeyId;

    @NotEmpty(message = "工作空间AccessKeySecret不能为空")
    private String accessKeySecret;

    @NotEmpty(message = "规则编码不能为空")
    private String ruleCode;

    private Map<String, Object> param = new HashMap<>();

}
