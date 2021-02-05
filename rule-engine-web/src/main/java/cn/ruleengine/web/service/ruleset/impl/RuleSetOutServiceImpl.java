package cn.ruleengine.web.service.ruleset.impl;

import cn.ruleengine.core.RuleSetEngine;
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
 * @author dingqianwen
 * @date 2021/1/15
 * @since 1.0.0
 */
@Slf4j
@Service
public class RuleSetOutServiceImpl extends RuleEngineOutService {


    public RuleSetOutServiceImpl(@Qualifier("ruleSetEngine") RuleSetEngine ruleSetEngine, ThreadPoolTaskExecutor threadPoolTaskExecutor, WorkspaceService workspaceService) {
        super(ruleSetEngine, threadPoolTaskExecutor, workspaceService);
    }


    /**
     * 执行单个规则集，获取执行结果
     *
     * @param executeRequest 执行规则集入参
     * @return 规则集执行结果
     */
    @Override
    public Object execute(ExecuteRequest executeRequest) {
        return super.execute(executeRequest);
    }

    /**
     * 批量执行多个规则集(一次建议最多1000个)，获取执行结果
     *
     * @param batchExecuteRequest 执行规则集入参
     * @return 规则集执行结果
     */
    @Override
    public Object batchExecute(BatchExecuteRequest batchExecuteRequest) {
        return super.batchExecute(batchExecuteRequest);
    }

    /**
     * 引擎中是否存在这个规则集
     *
     * @param isExistsRequest 参数
     * @return true存在
     */
    @Override
    public Boolean isExists(IsExistsRequest isExistsRequest) {
        return super.isExists(isExistsRequest);
    }

}
