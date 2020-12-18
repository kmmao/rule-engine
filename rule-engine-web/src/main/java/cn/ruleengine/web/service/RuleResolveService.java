package cn.ruleengine.web.service;

import cn.ruleengine.web.store.entity.RuleEngineRule;
import cn.ruleengine.core.rule.Rule;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
public interface RuleResolveService {


    /**
     * 根据规则code查询解析一个规则
     *
     * @param ruleCode 规则code
     * @return rule
     */
    Rule getRuleByCode(String ruleCode);

    /**
     * 根据规则id查询解析一个规则
     *
     * @param id 规则id
     * @return rule
     */
    Rule getRuleById(Integer id);

    /**
     * 处理引擎规则
     *
     * @param ruleEngineRule 规则引擎规则
     * @return 规则
     */
    Rule ruleProcess(RuleEngineRule ruleEngineRule);

}
