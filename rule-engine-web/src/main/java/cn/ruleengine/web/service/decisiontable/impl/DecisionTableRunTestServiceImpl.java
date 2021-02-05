package cn.ruleengine.web.service.decisiontable.impl;

import cn.ruleengine.core.*;
import cn.ruleengine.core.decisiontable.DecisionTable;
import cn.ruleengine.web.service.RunTestService;
import cn.ruleengine.web.service.decisiontable.DecisionTableResolveService;
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
 * @date 2020/8/26
 * @since 1.0.0
 */
@Slf4j
@Service
public class DecisionTableRunTestServiceImpl implements RunTestService {

    @Resource
    private DecisionTableResolveService decisionTableResolveService;
    @Resource
    private RuleEngineConfiguration ruleEngineConfiguration;

    /**
     * 规则模拟运行
     *
     * @param runTestRequest 规则参数信息
     * @return result
     */
    @Override
    public Object run(RunTestRequest runTestRequest) {
        log.info("模拟运行决策表：{}", runTestRequest.getRuleCode());
        Input input = new DefaultInput();
        Map<String, Object> params = runTestRequest.getParam();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            input.put(param.getKey(), param.getValue());
        }
        log.info("初始化决策表引擎");
        RuleEngineConfiguration ruleEngineConfiguration = new RuleEngineConfiguration();
        DecisionTableEngine engine = new DecisionTableEngine(ruleEngineConfiguration);
        DecisionTable decisionTable = this.decisionTableResolveService.getDecisionTableById(runTestRequest.getId());
        engine.add(decisionTable);
        // 加载变量
        engine.getConfiguration().setEngineVariable(this.ruleEngineConfiguration.getEngineVariable());
        return engine.execute(input, runTestRequest.getWorkspaceCode(), runTestRequest.getRuleCode());
    }

}
