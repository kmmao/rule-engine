package com.engine.web.vo.rule;

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

    @NotEmpty
    private String workspaceCode;

    @NotEmpty
    private String ruleCode;

    private Map<String, Object> param = new HashMap<>();

}
