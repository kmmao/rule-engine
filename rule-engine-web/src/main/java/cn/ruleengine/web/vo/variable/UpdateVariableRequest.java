package cn.ruleengine.web.vo.variable;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/25
 * @since 1.0.0
 */
@Data
public class UpdateVariableRequest {

    @NotNull
    private Integer id;

    @NotBlank
    private String name;

    @NotNull
    private Integer type;

    private String description;

    @NotNull
    private String value;

    /**
     * 函数中所有的参数
     */
    private List<ParamValue> paramValues;

}
