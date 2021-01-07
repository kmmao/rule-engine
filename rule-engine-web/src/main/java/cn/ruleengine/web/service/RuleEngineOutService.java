package cn.ruleengine.web.service;

import cn.ruleengine.web.vo.out.BatchExecuteRequest;
import cn.ruleengine.web.vo.out.ExecuteRequest;
import cn.ruleengine.web.vo.out.IsExistsRequest;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/22
 * @since 1.0.0
 */
public interface RuleEngineOutService {

    /**
     * 执行单个规则，获取执行结果
     *
     * @param executeRequest 执行规则入参
     * @return 规则执行结果
     */
    Object execute(ExecuteRequest executeRequest);

    /**
     * 批量执行多个规则(一次最多1000个)，获取执行结果
     *
     * @param batchExecuteRequest 执行规则入参
     * @return 规则执行结果
     */
    Object batchExecute(BatchExecuteRequest batchExecuteRequest);

    /**
     * 引擎中是否存在这个规则
     *
     * @param isExistsRequest 参数
     * @return true存在
     */
    Boolean isExists(IsExistsRequest isExistsRequest);

}
