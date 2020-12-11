package cn.ruleengine.web.vo.condition;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Data
public class AddConditionRequest {

    @NotBlank(message = "条件名称不能为空")
    private String name;

    private String description;

    @NotNull(message = "条件配置不能为空")
    private ConfigBean config;

}
