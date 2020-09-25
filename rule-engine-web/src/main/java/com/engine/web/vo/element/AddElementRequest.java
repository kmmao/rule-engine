package com.engine.web.vo.element;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Data
public class AddElementRequest {

    @NotBlank(message = "元素名称不能为空")
    private String name;

    @Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "只能由英文数字下划线组成。")
    @NotBlank(message = "元素code不能为空")
    private String code;

    @NotBlank(message = "元素类型不能为空")
    private String valueType;

    private String description;

}
