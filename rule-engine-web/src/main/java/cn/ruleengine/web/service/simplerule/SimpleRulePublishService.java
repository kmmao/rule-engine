package cn.ruleengine.web.service.simplerule;

import cn.ruleengine.core.rule.SimpleRule;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/4
 * @since 1.0.0
 */
public interface SimpleRulePublishService {

    /**
     * 获取所有的已发布规则
     *
     * @return rule
     */
    List<SimpleRule> getAllPublishSimpleRule();

    /**
     * 根据规则code，查询发布规则
     *
     * @param workspaceCode 工作空间code
     * @param ruleCode      规则code
     * @return 规则
     */
    SimpleRule getPublishSimpleRule(String workspaceCode, String ruleCode);

}
