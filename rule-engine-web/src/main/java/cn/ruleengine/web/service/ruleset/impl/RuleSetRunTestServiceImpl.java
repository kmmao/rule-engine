package cn.ruleengine.web.service.ruleset.impl;

import cn.ruleengine.core.*;
import cn.ruleengine.core.rule.RuleSet;
import cn.ruleengine.web.service.RunTestService;
import cn.ruleengine.web.service.ruleset.RuleSetResolveService;
import cn.ruleengine.web.vo.generalrule.RunTestRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/15
 * @since 1.0.0
 */
@Slf4j
@Service
public class RuleSetRunTestServiceImpl implements RunTestService {

    @Resource
    private RuleSetResolveService ruleSetResolveService;
    @Resource
    private RuleEngineConfiguration ruleEngineConfiguration;

    /**
     * 规则集模拟运行
     *
     * @param runTestRequest 规则集参数信息
     * @return result
     */
    @Override
    public Object run(RunTestRequest runTestRequest) {
        log.info("模拟运行规则集：{}", runTestRequest.getRuleCode());
        Input input = new DefaultInput();
        Map<String, Object> params = runTestRequest.getParam();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            input.put(param.getKey(), param.getValue());
        }
        log.info("初始化规则集引擎");
        RuleEngineConfiguration ruleEngineConfiguration = new RuleEngineConfiguration();
        RuleSetEngine engine = new RuleSetEngine(ruleEngineConfiguration);
        RuleSet rule = this.ruleSetResolveService.getRuleSetById(runTestRequest.getId());
        engine.addRuleSet(rule);
        // 加载变量
        engine.getConfiguration().setEngineVariable(this.ruleEngineConfiguration.getEngineVariable());
        return engine.execute(input, runTestRequest.getWorkspaceCode(), runTestRequest.getRuleCode());
    }

}
