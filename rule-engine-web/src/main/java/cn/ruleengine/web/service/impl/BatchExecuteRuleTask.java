package cn.ruleengine.web.service.impl;

import cn.ruleengine.web.vo.rule.BatchExecuteRuleRequest;
import cn.ruleengine.web.vo.rule.BatchExecuteRuleResponse;
import cn.ruleengine.core.DefaultInput;
import cn.ruleengine.core.Engine;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.OutPut;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/23
 * @since 1.0.0
 */
@Slf4j
public class BatchExecuteRuleTask implements Runnable {

    private List<BatchExecuteRuleRequest.ExecuteInfo> infoList;
    private Engine engine;
    private List<BatchExecuteRuleResponse> outPuts;
    private CountDownLatch countDownLatch;
    private String workspaceCode;

    public BatchExecuteRuleTask(String workspaceCode, CountDownLatch countDownLatch, List<BatchExecuteRuleResponse> outPuts, Engine engine, List<BatchExecuteRuleRequest.ExecuteInfo> infoList) {
        this.workspaceCode = workspaceCode;
        this.countDownLatch = countDownLatch;
        this.outPuts = outPuts;
        this.engine = engine;
        this.infoList = infoList;
    }

    @Override
    public void run() {
        for (BatchExecuteRuleRequest.ExecuteInfo executeInfo : this.infoList) {
            Input input = new DefaultInput();
            Map<String, Object> params = executeInfo.getParam();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                input.put(param.getKey(), param.getValue());
            }
            // 封装规则执行结果
            BatchExecuteRuleResponse ruleResponse = new BatchExecuteRuleResponse();
            ruleResponse.setSymbol(executeInfo.getSymbol());
            try {
                OutPut outPut = this.engine.execute(input, this.workspaceCode, executeInfo.getRuleCode());
                ruleResponse.setOutPut(outPut);
            } catch (Exception e) {
                log.error("执行规则异常", e);
                ruleResponse.setMessage(e.getMessage());
                ruleResponse.setIsDone(false);
            }
            this.outPuts.add(ruleResponse);
        }
        this.countDownLatch.countDown();
    }

}
