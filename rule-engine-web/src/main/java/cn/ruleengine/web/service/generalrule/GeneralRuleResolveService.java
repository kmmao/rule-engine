package cn.ruleengine.web.service.generalrule;

import cn.ruleengine.core.rule.GeneralRule;
import cn.ruleengine.web.store.entity.RuleEngineGeneralRule;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
public interface GeneralRuleResolveService {


    /**
     * 根据规则id查询解析一个规则
     *
     * @param id 规则id
     * @return rule
     */
    GeneralRule getGeneralRuleById(Integer id);

    /**
     * 处理引擎规则
     *
     * @param ruleEngineGeneralRule 规则引擎规则
     * @return 规则
     */
    GeneralRule ruleProcess(RuleEngineGeneralRule ruleEngineGeneralRule);

}
