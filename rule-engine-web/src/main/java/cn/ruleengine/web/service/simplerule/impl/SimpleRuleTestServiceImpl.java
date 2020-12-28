package cn.ruleengine.web.service.simplerule.impl;

import cn.ruleengine.core.*;
import cn.ruleengine.core.SimpleRuleEngine;
import cn.ruleengine.core.Engine;
import cn.ruleengine.core.listener.ExecuteListener;
import cn.ruleengine.core.rule.SimpleRule;
import cn.ruleengine.web.service.simplerule.RuleResolveService;
import cn.ruleengine.web.service.simplerule.SimpleRuleTestService;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.web.vo.simplerule.RunTestRequest;
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
public class SimpleRuleTestServiceImpl implements SimpleRuleTestService {

    @Resource
    private RuleResolveService ruleResolveService;
    @Resource
    private Engine engine;

    /**
     * 规则模拟运行
     *
     * @param runTestRequest 规则参数信息
     * @return result
     */
    @Override
    public Object run(RunTestRequest runTestRequest) {
        log.info("模拟运行规则：{}", runTestRequest.getRuleCode());
        Input input = new DefaultInput();
        Map<String, Object> params = runTestRequest.getParam();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            input.put(param.getKey(), param.getValue());
        }
        log.info("初始化规则引擎");
        RuleEngineConfiguration ruleEngineConfiguration = new RuleEngineConfiguration();
        SimpleRuleEngine engine = new SimpleRuleEngine(ruleEngineConfiguration);
        SimpleRule rule = ruleResolveService.getSimpleRuleById(runTestRequest.getId());
        engine.addSimpleRule(rule);
        // 加载变量
        engine.getConfiguration().setEngineVariable(this.engine.getEngineVariable());
        // 配置监听器
        ruleEngineConfiguration.setSimpleRuleListener(new ExecuteListener<SimpleRule>() {

            @Override
            public void before(SimpleRule rule, Input input) {

            }

            @Override
            public void onException(SimpleRule rule, Input input, Exception exception) {
                log.error("规则异常", exception);
                throw new ValidException(exception.getMessage());
            }

            @Override
            public void after(SimpleRule rule, Input input, OutPut outPut) {

            }

        });
        return engine.execute(input, runTestRequest.getWorkspaceCode(), runTestRequest.getRuleCode());
    }

}
