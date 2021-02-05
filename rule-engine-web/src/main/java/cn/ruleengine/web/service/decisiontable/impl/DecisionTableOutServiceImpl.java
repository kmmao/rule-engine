package cn.ruleengine.web.service.decisiontable.impl;

import cn.ruleengine.core.DecisionTableEngine;
import cn.ruleengine.web.service.RuleEngineOutService;
import cn.ruleengine.web.service.WorkspaceService;
import cn.ruleengine.web.vo.output.BatchExecuteRequest;
import cn.ruleengine.web.vo.output.ExecuteRequest;
import cn.ruleengine.web.vo.output.IsExistsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2021/1/7
 * @since 1.0.0
 */
@Slf4j
@Service
public class DecisionTableOutServiceImpl extends RuleEngineOutService {

    public DecisionTableOutServiceImpl(@Qualifier("decisionTableEngine") DecisionTableEngine decisionTableEngine, ThreadPoolTaskExecutor threadPoolTaskExecutor, WorkspaceService workspaceService) {
        super(decisionTableEngine, threadPoolTaskExecutor, workspaceService);
    }

    /**
     * 执行单个决策表，获取执行结果
     *
     * @param executeRequest 执行决策表入参
     * @return 决策表执行结果
     */
    @Override
    public Object execute(ExecuteRequest executeRequest) {
        return super.execute(executeRequest);
    }

    /**
     * 批量执行多个决策表(一次建议最多1000个)，获取执行结果
     *
     * @param batchExecuteRequest 执行决策表入参
     * @return 决策表执行结果
     */
    @Override
    public Object batchExecute(BatchExecuteRequest batchExecuteRequest) {
        return super.batchExecute(batchExecuteRequest);
    }

    /**
     * 引擎中是否存在这个决策表
     *
     * @param isExistsRequest 参数
     * @return true存在
     */
    @Override
    public Boolean isExists(IsExistsRequest isExistsRequest) {
        return super.isExists(isExistsRequest);
    }


}
