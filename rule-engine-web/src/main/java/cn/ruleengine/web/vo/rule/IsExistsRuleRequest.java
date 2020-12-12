package cn.ruleengine.web.vo.rule;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/13
 * @since 1.0.0
 */
@Data
public class IsExistsRuleRequest {

    @NotBlank
    private String ruleCode;
    @NotBlank
    private String workspaceCode;
    @NotBlank
    private String accessKeyId;
    @NotBlank
    private String accessKeySecret;

}
