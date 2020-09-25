package com.engine.web.vo.rule;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/28
 * @since 1.0.0
 */
@Data
public class RuleDefinition {

    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private String description;


}
