package cn.ruleengine.web.service.ruleset;

import cn.ruleengine.core.rule.RuleSet;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/27
 * @since 1.0.0
 */
public interface RuleSetPublishService {

    /**
     * 获取所有的已发布规则集
     *
     * @return list DecisionTable
     */
    List<RuleSet> getAllPublishRuleSet();

    /**
     * 根据规则集code，查询发布规则
     *
     * @param workspaceCode 工作空间code
     * @param code          规则集code
     * @return DecisionTable
     */
    RuleSet getPublishRuleSet(String workspaceCode, String code);

}
