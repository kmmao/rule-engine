package cn.ruleengine.web.vo.element;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/25
 * @since 1.0.0
 */
@Data
public class UpdateElementRequest {

    @NotNull
    private Integer id;

    @NotBlank(message = "元素名称不能为空")
    @Length(min = 1, max = 25, message = "元素名称长度在 1 到 25 个字符")
    private String name;

    private String description;

}
