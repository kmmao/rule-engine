package cn.ruleengine.client.fegin;

import cn.ruleengine.client.param.BatchParam;
import cn.ruleengine.client.param.ExecuteParam;
import cn.ruleengine.client.param.IsExistsParam;
import cn.ruleengine.client.result.BatchExecuteResult;
import cn.ruleengine.client.result.ExecuteResult;
import cn.ruleengine.client.result.IsExistsResult;
import feign.RequestLine;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020-12-29 15:37
 * @since 1.0.0
 */
public interface DecisionTableInterface extends BaseInterface{

    /**
     * 调用规则引擎中的决策表
     *
     * @param executeParam 入参
     * @return ExecuteRuleResult
     */
    @Override
    @RequestLine("POST /ruleEngine/decisionTable/execute")
    ExecuteResult execute(ExecuteParam executeParam);

    /**
     * 引擎中是否存在此决策表
     *
     * @param existsParam 入参
     * @return IsExistsResult
     */
    @Override
    @RequestLine("POST /ruleEngine/decisionTable/isExists")
    IsExistsResult isExists(IsExistsParam existsParam);


    /**
     * 批量执行决策表
     *
     * @param batchParam 批量参数
     * @return list
     */
    @Override
    @RequestLine("POST /ruleEngine/decisionTable/batchExecute")
    BatchExecuteResult batchExecute(BatchParam batchParam);

}
