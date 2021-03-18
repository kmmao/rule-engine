package cn.ruleengine.web.service;

import cn.ruleengine.core.*;
import cn.ruleengine.web.vo.output.BatchExecuteRequest;
import cn.ruleengine.web.vo.output.BatchExecuteResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
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
public class BatchExecuteTask implements Runnable {

    private final List<BatchExecuteRequest.ExecuteInfo> infoList;
    private final Engine<?> engine;
    private final List<BatchExecuteResponse> outputs;
    private final CountDownLatch countDownLatch;
    private final String workspaceCode;

    public BatchExecuteTask(String workspaceCode,
                            CountDownLatch countDownLatch,
                            List<BatchExecuteResponse> outputs,
                            Engine<?> engine, List<BatchExecuteRequest.ExecuteInfo> infoList) {
        this.workspaceCode = workspaceCode;
        this.countDownLatch = countDownLatch;
        this.outputs = outputs;
        this.engine = engine;
        this.infoList = infoList;
    }

    @Override
    public void run() {
        for (BatchExecuteRequest.ExecuteInfo executeInfo : this.infoList) {
            Input input = new DefaultInput(executeInfo.getParam());
            // 封装规则执行结果
            BatchExecuteResponse ruleResponse = new BatchExecuteResponse();
            ruleResponse.setSymbol(executeInfo.getSymbol());
            try {
                Output output = this.engine.execute(input, this.workspaceCode, executeInfo.getCode());
                ruleResponse.setOutput(output);
            } catch (Exception e) {
                log.error("Execution exception", e);
                ruleResponse.setMessage(e.getMessage());
                ruleResponse.setIsDone(false);
            }
            this.outputs.add(ruleResponse);
        }
        this.countDownLatch.countDown();
    }

}
