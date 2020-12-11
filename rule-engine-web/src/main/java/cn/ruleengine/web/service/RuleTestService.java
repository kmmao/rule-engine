package cn.ruleengine.web.service;

import cn.ruleengine.web.vo.rule.RunTestRequest;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/26
 * @since 1.0.0
 */
public interface RuleTestService {

    /**
     * 规则模拟运行
     *
     * @param runTestRequest 规则参数信息
     * @return result
     */
    Object run(RunTestRequest runTestRequest);

}
