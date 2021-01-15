package cn.ruleengine.web.service.ruleset.impl;

import cn.ruleengine.core.DefaultInput;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleSetEngine;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.web.service.RuleEngineOutService;
import cn.ruleengine.web.service.WorkspaceService;
import cn.ruleengine.web.vo.out.BatchExecuteRequest;
import cn.ruleengine.web.vo.out.ExecuteRequest;
import cn.ruleengine.web.vo.out.IsExistsRequest;
import cn.ruleengine.web.vo.workspace.AccessKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
public class RuleSetOutServiceImpl implements RuleEngineOutService {

    @Resource
    private RuleSetEngine engine;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private WorkspaceService workspaceService;

    /**
     * 执行单个规则集，获取执行结果
     *
     * @param executeRequest 执行规则集入参
     * @return 规则集执行结果
     */
    @Override
    public Object execute(ExecuteRequest executeRequest) {
        log.info("开始执行规则集，入参：{}", executeRequest);
        String workspaceCode = executeRequest.getWorkspaceCode();
        AccessKey accessKey = this.workspaceService.accessKey(workspaceCode);
        if (!accessKey.equals(executeRequest.getAccessKeyId(), executeRequest.getAccessKeySecret())) {
            throw new ValidException("AccessKey Verification failed");
        }
        Input input = new DefaultInput();
        input.putAll(executeRequest.getParam());
        return this.engine.execute(input, workspaceCode, executeRequest.getCode());
    }

    /**
     * 批量执行多个规则集(一次建议最多1000个)，获取执行结果
     *
     * @param batchExecuteRequest 执行规则集入参
     * @return 规则集执行结果
     */
    @Override
    public Object batchExecute(BatchExecuteRequest batchExecuteRequest) {
        throw new UnsupportedOperationException();
    }

    /**
     * 引擎中是否存在这个规则集
     *
     * @param isExistsRequest 参数
     * @return true存在
     */
    @Override
    public Boolean isExists(IsExistsRequest isExistsRequest) {
        String workspaceCode = isExistsRequest.getWorkspaceCode();
        AccessKey accessKey = this.workspaceService.accessKey(workspaceCode);
        if (!accessKey.equals(isExistsRequest.getAccessKeyId(), isExistsRequest.getAccessKeySecret())) {
            throw new ValidException("AccessKey Verification failed");
        }
        return this.engine.isExists(isExistsRequest.getWorkspaceCode(), isExistsRequest.getCode());
    }

}
