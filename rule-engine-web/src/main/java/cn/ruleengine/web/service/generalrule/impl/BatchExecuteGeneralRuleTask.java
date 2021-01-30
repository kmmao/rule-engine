package cn.ruleengine.web.service.generalrule.impl;

import cn.ruleengine.web.vo.output.BatchExecuteRequest;
import cn.ruleengine.web.vo.output.BatchExecuteResponse;
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
public class BatchExecuteGeneralRuleTask implements Runnable {

    private List<BatchExecuteRequest.ExecuteInfo> infoList;
    private Engine engine;
    private List<BatchExecuteResponse> outPuts;
    private CountDownLatch countDownLatch;
    private String workspaceCode;

    public BatchExecuteGeneralRuleTask(String workspaceCode, CountDownLatch countDownLatch, List<BatchExecuteResponse> outPuts, Engine engine, List<BatchExecuteRequest.ExecuteInfo> infoList) {
        this.workspaceCode = workspaceCode;
        this.countDownLatch = countDownLatch;
        this.outPuts = outPuts;
        this.engine = engine;
        this.infoList = infoList;
    }

    @Override
    public void run() {
        for (BatchExecuteRequest.ExecuteInfo executeInfo : this.infoList) {
            Input input = new DefaultInput();
            Map<String, Object> params = executeInfo.getParam();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                input.put(param.getKey(), param.getValue());
            }
            // 封装规则执行结果
            BatchExecuteResponse ruleResponse = new BatchExecuteResponse();
            ruleResponse.setSymbol(executeInfo.getSymbol());
            try {
                OutPut outPut = this.engine.execute(input, this.workspaceCode, executeInfo.getCode());
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
