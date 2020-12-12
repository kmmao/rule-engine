package cn.ruleengine.web.service;

import cn.ruleengine.web.vo.rule.BatchExecuteRuleRequest;
import cn.ruleengine.web.vo.rule.ExecuteRuleRequest;
import cn.ruleengine.web.vo.rule.IsExistsRuleRequest;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/22
 * @since 1.0.0
 */
public interface RuleOutService {

    /**
     * 执行单个规则，获取执行结果
     *
     * @param executeRuleRequest 执行规则入参
     * @return 规则执行结果
     */
    Object executeRule(ExecuteRuleRequest executeRuleRequest);

    /**
     * 批量执行多个规则(一次最多1000个)，获取执行结果
     *
     * @param batchExecuteRuleRequest 执行规则入参
     * @return 规则执行结果
     */
    Object batchExecuteRule(BatchExecuteRuleRequest batchExecuteRuleRequest);

    /**
     * 引擎中是否存在这个规则
     *
     * @param isExistsRuleRequest 参数
     * @return true存在
     */
    Boolean isExists(IsExistsRuleRequest isExistsRuleRequest);

}
