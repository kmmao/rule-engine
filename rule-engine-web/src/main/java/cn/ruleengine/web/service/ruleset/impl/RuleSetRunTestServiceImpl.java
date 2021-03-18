package cn.ruleengine.web.service.ruleset.impl;

import cn.ruleengine.core.*;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.core.rule.RuleSet;
import cn.ruleengine.web.enums.DataStatus;
import cn.ruleengine.web.service.RunTestService;
import cn.ruleengine.web.store.entity.RuleEngineRuleSetPublish;
import cn.ruleengine.web.store.manager.RuleEngineRuleSetPublishManager;
import cn.ruleengine.web.vo.generalrule.RunTestRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    private RuleEngineConfiguration ruleEngineConfiguration;
    @Resource
    private RuleEngineRuleSetPublishManager ruleEngineRuleSetPublishManager;

    /**
     * 规则集模拟运行
     *
     * @param runTestRequest 规则集参数信息
     * @return result
     */
    @Override
    public Object run(RunTestRequest runTestRequest) {
        log.info("模拟运行规则集：{}", runTestRequest.getCode());
        RuleEngineRuleSetPublish ruleSetPublish = this.ruleEngineRuleSetPublishManager.lambdaQuery()
                .eq(RuleEngineRuleSetPublish::getStatus, runTestRequest.getStatus())
                .eq(RuleEngineRuleSetPublish::getRuleSetCode, runTestRequest.getCode())
                .eq(RuleEngineRuleSetPublish::getWorkspaceCode, runTestRequest.getWorkspaceCode())
                .one();
        if (ruleSetPublish == null) {
            // 如果待发布找不到，用已发布  此场景出现在只有一个已发布的时候
            ruleSetPublish = this.ruleEngineRuleSetPublishManager.lambdaQuery()
                    .eq(RuleEngineRuleSetPublish::getStatus, DataStatus.PUBLISHED.getStatus())
                    .eq(RuleEngineRuleSetPublish::getRuleSetCode, runTestRequest.getCode())
                    .eq(RuleEngineRuleSetPublish::getWorkspaceCode, runTestRequest.getWorkspaceCode())
                    .one();
            if (ruleSetPublish == null) {
                throw new ValidException("找不到可运行的规则集数据:{},{},{}", runTestRequest.getWorkspaceCode(), runTestRequest.getCode(), runTestRequest.getStatus());
            }
        }
        Input input = new DefaultInput(runTestRequest.getParam());
        log.info("初始化规则集引擎");
        RuleEngineConfiguration ruleEngineConfiguration = new RuleEngineConfiguration();
        RuleSetEngine engine = new RuleSetEngine(ruleEngineConfiguration);
        RuleSet rule = RuleSet.buildRuleSet(ruleSetPublish.getData());
        engine.add(rule);
        // 加载变量
        engine.getConfiguration().setEngineVariable(this.ruleEngineConfiguration.getEngineVariable());
        return engine.execute(input, runTestRequest.getWorkspaceCode(), runTestRequest.getCode());
    }

}
