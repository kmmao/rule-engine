package cn.ruleengine.web.service.decisiontable.impl;

import cn.ruleengine.core.DecisionTableEngine;
import cn.ruleengine.core.DefaultInput;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.web.service.RuleEngineOutService;
import cn.ruleengine.web.service.WorkspaceService;
import cn.ruleengine.web.vo.output.BatchExecuteRequest;
import cn.ruleengine.web.vo.output.ExecuteRequest;
import cn.ruleengine.web.vo.output.IsExistsRequest;
import cn.ruleengine.web.vo.workspace.AccessKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
public class DecisionTableOutServiceImpl implements RuleEngineOutService {

    @Resource
    private DecisionTableEngine decisionTableEngine;
    @Resource
    private WorkspaceService workspaceService;

    /**
     * 执行单个决策表，获取执行结果
     *
     * @param executeRequest 执行决策表入参
     * @return 决策表执行结果
     */
    @Override
    public Object execute(ExecuteRequest executeRequest) {
        log.info("开始执行决策表，入参：{}", executeRequest);
        long currentTimeMillis = System.currentTimeMillis();
        AccessKey accessKey = this.workspaceService.accessKey(executeRequest.getWorkspaceCode());
        if (!accessKey.equals(executeRequest.getAccessKeyId(), executeRequest.getAccessKeySecret())) {
            throw new ValidException("AccessKey Verification failed");
        }
        log.info("校验AccessKey耗时：{}", (System.currentTimeMillis() - currentTimeMillis));
        Input input = new DefaultInput();
        input.putAll(executeRequest.getParam());
        return this.decisionTableEngine.execute(input, executeRequest.getWorkspaceCode(), executeRequest.getCode());
    }

    /**
     * 批量执行多个决策表(一次建议最多1000个)，获取执行结果
     *
     * @param batchExecuteRequest 执行决策表入参
     * @return 决策表执行结果
     */
    @Override
    public Object batchExecute(BatchExecuteRequest batchExecuteRequest) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    /**
     * 引擎中是否存在这个决策表
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
        return this.decisionTableEngine.isExists(isExistsRequest.getWorkspaceCode(), isExistsRequest.getCode());
    }

}
