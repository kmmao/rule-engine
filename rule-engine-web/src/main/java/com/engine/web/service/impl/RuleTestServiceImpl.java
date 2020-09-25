package com.engine.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.engine.core.*;
import com.engine.core.exception.ValidException;
import com.engine.core.rule.Rule;
import com.engine.core.rule.RuleListener;
import com.engine.core.value.Value;
import com.engine.web.enums.ErrorLevelEnum;
import com.engine.web.service.RuleResolveService;
import com.engine.web.service.RuleTestService;
import com.engine.web.service.VariableResolveService;
import com.engine.web.store.entity.RuleEngineRule;
import com.engine.web.store.manager.RuleEngineRuleManager;
import com.engine.web.vo.rule.ExecuteRuleRequest;
import com.engine.web.vo.rule.RuleCountInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private VariableResolveService variableResolveService;

    @Override
    public Object run(ExecuteRuleRequest executeRuleRequest) {
        log.info("模拟运行规则：{}", executeRuleRequest.getRuleCode());
        Input input = new DefaultInput();
        Map<String, Object> params = executeRuleRequest.getParam();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            input.put(param.getKey(), param.getValue());
        }
        log.info("初始化规则引擎");
        DefaultEngine engine = new DefaultEngine();
        Rule rule = ruleResolveService.getRuleByCode(executeRuleRequest.getRuleCode());
        engine.addRule(rule);
        RuleEngineRule ruleEngineRule = ruleEngineRuleManager.getById(rule.getId());
        // 加载变量
        EngineVariable engineVariable = engine.getEngineVariable();
        String countInfo = ruleEngineRule.getCountInfo();
        if (countInfo != null) {
            RuleCountInfo ruleCountInfo = JSONObject.parseObject(countInfo, RuleCountInfo.class);
            Set<Integer> variableIds = ruleCountInfo.getVariableIds();
            for (Integer variableId : variableIds) {
                Value value = variableResolveService.getVarById(variableId);
                engineVariable.addVariable(variableId, value);
            }
        }
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
        return engine.execute(input, executeRuleRequest.getRuleCode());
    }

}
