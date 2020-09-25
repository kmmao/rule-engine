package com.engine.web.vo.rule;

import com.engine.core.rule.Rule;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/25
 * @since 1.0.0
 */
@Data
public class RuleInterfaceDescriptionResponse {

    private String requestUrl;

    private Set<Rule.Parameter> parameters;

}
