package cn.ruleengine.core.rule.strategy;

import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.rule.Rule;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/29
 * @since 1.0.0
 */
public interface RuleSetStrategy {

    /**
     * 计算规则结果
     *
     * @param rules         规则集
     * @param input         输入参数
     * @param configuration 配置
     * @return 结果
     */
    List<Object> compute(List<Rule> rules, Input input, RuleEngineConfiguration configuration);

}
