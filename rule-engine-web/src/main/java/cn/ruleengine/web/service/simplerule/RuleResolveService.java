package cn.ruleengine.web.service.simplerule;

import cn.ruleengine.core.rule.SimpleRule;
import cn.ruleengine.web.store.entity.RuleEngineSimpleRule;

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
    SimpleRule getSimpleRuleByCode(String ruleCode);

    /**
     * 根据规则id查询解析一个规则
     *
     * @param id 规则id
     * @return rule
     */
    SimpleRule getSimpleRuleById(Integer id);

    /**
     * 处理引擎规则
     *
     * @param ruleEngineRule 规则引擎规则
     * @return 规则
     */
    SimpleRule ruleProcess(RuleEngineSimpleRule ruleEngineRule);

}
