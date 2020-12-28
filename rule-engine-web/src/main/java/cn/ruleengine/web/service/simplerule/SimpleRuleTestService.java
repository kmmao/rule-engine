package cn.ruleengine.web.service.simplerule;

import cn.ruleengine.web.vo.simplerule.RunTestRequest;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/26
 * @since 1.0.0
 */
public interface SimpleRuleTestService {

    /**
     * 规则模拟运行
     *
     * @param runTestRequest 规则参数信息
     * @return result
     */
    Object run(RunTestRequest runTestRequest);

}
