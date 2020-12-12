package cn.ruleengine.web.vo.variable;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Data
public class AddVariableRequest {

    @NotBlank
    @Length(min = 1, max = 15, message = "变量名称长度在 1 到 15 个字符")
    private String name;

    @NotNull
    private Integer type;

    private String description;

    @NotBlank
    private String valueType;

    @NotNull
    private String value;

    /**
     * 函数中所有的参数
     */
    @Valid
    private List<ParamValue> paramValues;

}
