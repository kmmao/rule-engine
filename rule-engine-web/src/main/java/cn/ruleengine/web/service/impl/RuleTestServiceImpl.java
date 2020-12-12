package cn.ruleengine.web.service.impl;

import cn.ruleengine.core.*;
import cn.ruleengine.web.service.RuleResolveService;
import cn.ruleengine.web.service.RuleTestService;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.core.rule.Rule;
import cn.ruleengine.core.rule.RuleListener;
import cn.ruleengine.web.vo.rule.RunTestRequest;
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
        DefaultEngine engine = new DefaultEngine();
        Rule rule = ruleResolveService.getRuleById(runTestRequest.getId());
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
        return engine.execute(input, runTestRequest.getWorkspaceCode(), runTestRequest.getRuleCode());
    }

}
