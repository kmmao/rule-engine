package com.engine.web.service.impl;

import com.engine.core.DefaultInput;
import com.engine.core.Engine;
import com.engine.core.Input;
import com.engine.core.OutPut;
import com.engine.web.vo.rule.BatchExecuteRuleRequest;
import com.engine.web.vo.rule.BatchExecuteRuleResponse;
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

    public BatchExecuteRuleTask(CountDownLatch countDownLatch, List<BatchExecuteRuleResponse> outPuts, Engine engine, List<BatchExecuteRuleRequest.ExecuteInfo> infoList) {
        this.countDownLatch = countDownLatch;
        this.outPuts = outPuts;
        this.engine = engine;
        this.infoList = infoList;
    }

    @Override
    public void run() {
        for (BatchExecuteRuleRequest.ExecuteInfo executeInfo : infoList) {
            Input input = new DefaultInput();
            Map<String, Object> params = executeInfo.getParam();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                input.put(param.getKey(), param.getValue());
            }
            // 封装规则执行结果
            BatchExecuteRuleResponse ruleResponse = new BatchExecuteRuleResponse();
            ruleResponse.setSymbol(executeInfo.getSymbol());
            try {
                OutPut outPut = engine.execute(input, executeInfo.getWorkspaceCode(), executeInfo.getRuleCode());
                ruleResponse.setResult(outPut);
            } catch (Exception e) {
                log.error("执行规则异常", e);
                ruleResponse.setMessage(e.getMessage());
                ruleResponse.setIsDone(false);
            }
            outPuts.add(ruleResponse);
        }
        countDownLatch.countDown();
    }

}
