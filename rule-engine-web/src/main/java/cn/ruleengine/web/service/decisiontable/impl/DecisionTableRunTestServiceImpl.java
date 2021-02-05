package cn.ruleengine.web.service.decisiontable.impl;

import cn.ruleengine.core.*;
import cn.ruleengine.core.decisiontable.DecisionTable;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.web.enums.DataStatus;
import cn.ruleengine.web.service.RunTestService;
import cn.ruleengine.web.store.entity.RuleEngineDecisionTablePublish;
import cn.ruleengine.web.store.manager.RuleEngineDecisionTablePublishManager;
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
    private RuleEngineConfiguration ruleEngineConfiguration;
    @Resource
    private RuleEngineDecisionTablePublishManager ruleEngineDecisionTablePublishManager;

    /**
     * 规则模拟运行
     *
     * @param runTestRequest 规则参数信息
     * @return result
     */
    @Override
    public Object run(RunTestRequest runTestRequest) {
        log.info("模拟运行决策表：{}", runTestRequest.getCode());
        RuleEngineDecisionTablePublish rulePublish = this.ruleEngineDecisionTablePublishManager.lambdaQuery()
                .eq(RuleEngineDecisionTablePublish::getStatus, runTestRequest.getStatus())
                .eq(RuleEngineDecisionTablePublish::getDecisionTableCode, runTestRequest.getCode())
                .eq(RuleEngineDecisionTablePublish::getWorkspaceCode, runTestRequest.getWorkspaceCode())
                .one();
        if (rulePublish == null) {
            // 如果待发布找不到，用已发布  此场景出现在只有一个已发布的时候
            rulePublish = this.ruleEngineDecisionTablePublishManager.lambdaQuery()
                    .eq(RuleEngineDecisionTablePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                    .eq(RuleEngineDecisionTablePublish::getDecisionTableCode, runTestRequest.getCode())
                    .eq(RuleEngineDecisionTablePublish::getWorkspaceCode, runTestRequest.getWorkspaceCode())
                    .one();
            if (rulePublish == null) {
                throw new ValidException("找不到可运行的决策表数据:{},{},{}", runTestRequest.getWorkspaceCode(), runTestRequest.getCode(), runTestRequest.getStatus());
            }
        }
        Input input = new DefaultInput();
        Map<String, Object> params = runTestRequest.getParam();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            input.put(param.getKey(), param.getValue());
        }
        log.info("初始化决策表引擎");
        RuleEngineConfiguration ruleEngineConfiguration = new RuleEngineConfiguration();
        DecisionTableEngine engine = new DecisionTableEngine(ruleEngineConfiguration);
        DecisionTable decisionTable = DecisionTable.buildDecisionTable(rulePublish.getData());
        engine.add(decisionTable);
        // 加载变量
        engine.getConfiguration().setEngineVariable(this.ruleEngineConfiguration.getEngineVariable());
        return engine.execute(input, runTestRequest.getWorkspaceCode(), runTestRequest.getCode());
    }

}
