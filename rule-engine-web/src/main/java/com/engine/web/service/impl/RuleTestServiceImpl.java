package com.engine.web.service.impl;

import com.engine.core.*;
import com.engine.core.exception.ValidException;
import com.engine.core.rule.Rule;
import com.engine.core.rule.RuleListener;
import com.engine.web.service.RuleResolveService;
import com.engine.web.service.RuleTestService;
import com.engine.web.vo.rule.ExecuteRuleRequest;
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
