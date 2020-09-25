package com.engine.web.service;

import com.engine.web.vo.rule.ExecuteRuleRequest;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/26
 * @since 1.0.0
 */
public interface RuleTestService {
    Object run(ExecuteRuleRequest executeRuleRequest);
}
