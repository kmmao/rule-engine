package cn.ruleengine.web.vo.rule;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/24
 * @since 1.0.0
 */
@Data
public class Action {

    @NotNull(message = "结果值不能为空")
    private String value;


    private String valueName;

    private String variableValue;

    @NotNull(message = "结果类型不能为空")
    private Integer type;

    @NotBlank(message = "结果值类型不能为空")
    private String valueType;

}
