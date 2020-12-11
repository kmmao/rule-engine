package cn.ruleengine.web.service.impl;

import cn.ruleengine.core.*;
import cn.ruleengine.web.service.RuleResolveService;
import cn.ruleengine.web.service.RuleTestService;
import cn.ruleengine.web.vo.rule.ExecuteRuleRequest;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.core.rule.Rule;
import cn.ruleengine.core.rule.RuleListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/26
 * @since 1.0.0
 */
@Slf4j
@Service
public class RuleTestServiceImpl implements RuleTestService {

    @Resource
    private RuleResolveService ruleResolveService;
    @Resource
    private Engine engine;

    @Override
    public Object run(ExecuteRuleRequest executeRule) {
        log.info("模拟运行规则：{}", executeRule.getRuleCode());
        Input input = new DefaultInput();
        Map<String, Object> params = executeRule.getParam();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            input.put(param.getKey(), param.getValue());
        }
        log.info("初始化规则引擎");
        DefaultEngine engine = new DefaultEngine();
        Rule rule = ruleResolveService.getRuleByCode(executeRule.getRuleCode());
        engine.addRule(rule);
        // 加载变量
        engine.getConfiguration().setEngineVariable(this.engine.getEngineVariable());
        // 配置监听器
        engine.setRuleListener(new RuleListener() {

            @Override
            public void before(Rule rule, Input input) {

            }

            @Override
            public void onException(Rule rule, Input input, Exception exception) {
                log.error("规则异常", exception);
                throw new ValidException(exception.getMessage());
            }

            @Override
            public void after(Rule rule, Input input, OutPut outPut) {

            }

        });
        return engine.execute(input, executeRule.getWorkspaceCode(), executeRule.getRuleCode());
    }

}