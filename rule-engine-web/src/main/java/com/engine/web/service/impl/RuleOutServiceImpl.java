package com.engine.web.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.engine.core.DefaultInput;
import com.engine.core.Engine;
import com.engine.core.Input;
import com.engine.core.OutPut;
import com.engine.core.exception.EngineException;
import com.engine.web.service.RuleOutService;
import com.engine.web.vo.rule.BatchExecuteRuleRequest;
import com.engine.web.vo.rule.BatchExecuteRuleResponse;
import com.engine.web.vo.rule.ExecuteRuleRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/22
 * @since 1.0.0
 */
@Slf4j
@Service
public class RuleOutServiceImpl implements RuleOutService {

    @Resource
    private Engine engine;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 执行单个规则，获取执行结果
     *
     * @param executeRule 执行规则入参
     * @return 规则执行结果
     */
    @Override
    public Object executeRule(ExecuteRuleRequest executeRule) {
        Input input = new DefaultInput();
        Map<String, Object> params = executeRule.getParam();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            input.put(param.getKey(), param.getValue());
        }
        return engine.execute(input, executeRule.getWorkspaceCode(), executeRule.getRuleCode());
    }

    /**
     * 批量执行多个规则(一次最多2000个)，获取执行结果
     *
     * @param batchExecuteRuleRequest 执行规则入参
     * @return 规则执行结果
     */
    @Override
    public Object batchExecuteRule(BatchExecuteRuleRequest batchExecuteRuleRequest) {
        List<BatchExecuteRuleRequest.ExecuteInfo> executeInfos = batchExecuteRuleRequest.getExecuteInfos();
        Integer threadSegNumber = batchExecuteRuleRequest.getThreadSegNumber();
        log.info("批量执行规则数量：{},单个线程执行{}条规则", executeInfos.size(), threadSegNumber);
        List<BatchExecuteRuleResponse> outPuts = new CopyOnWriteArrayList<>();
        int countDownNumber = executeInfos.size() % threadSegNumber == 0 ? executeInfos.size() / threadSegNumber : (executeInfos.size() / threadSegNumber) + 1;
        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(countDownNumber);
        // 批量插入，执行一次批量
        for (int fromIndex = 0; fromIndex < executeInfos.size(); fromIndex += threadSegNumber) {
            int toIndex = Math.min(fromIndex + threadSegNumber, executeInfos.size());
            List<BatchExecuteRuleRequest.ExecuteInfo> infoList = executeInfos.subList(fromIndex, toIndex);
            BatchExecuteRuleTask batchExecuteRuleTask = new BatchExecuteRuleTask(countDownLatch, outPuts, engine, infoList);
            this.threadPoolTaskExecutor.execute(batchExecuteRuleTask);
        }
        // 等待线程处理完毕
        try {
            Long timeout = batchExecuteRuleRequest.getTimeout();
            if (timeout.equals(-1L)) {
                countDownLatch.await();
            } else {
                if (!countDownLatch.await(timeout, TimeUnit.MILLISECONDS)) {
                    throw new EngineException("Execution timeout:{}ms", timeout);
                }
            }
        } catch (InterruptedException e) {
            throw new EngineException("Execution failed, rule execution thread was interrupted", e);
        }
        return outPuts;
    }

}
