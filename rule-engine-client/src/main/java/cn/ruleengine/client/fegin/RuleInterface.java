package cn.ruleengine.client.fegin;

import cn.ruleengine.client.param.ExecuteParam;
import cn.ruleengine.client.param.IsExistsParam;
import cn.ruleengine.client.result.BatchOutPut;
import cn.ruleengine.client.result.ExecuteRuleResult;
import cn.ruleengine.client.result.IsExistsResult;
import feign.Headers;
import feign.RequestLine;

import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/10
 * @since 1.0.0
 */
@Headers({"Content-Type: application/json", "Accept: application/json"})
public interface RuleInterface {

    /**
     * 调用规则引擎中的规则
     *
     * @param executeParam 入参
     * @return ExecuteRuleResult
     */
    @RequestLine("POST /ruleEngine/execute")
    ExecuteRuleResult execute(ExecuteParam executeParam);

    /**
     * 引擎中是否存在此规则
     *
     * @param existsParam 入参
     * @return IsExistsResult
     */
    @RequestLine("POST /ruleEngine/isExists")
    IsExistsResult isExists(IsExistsParam existsParam);


    /**
     * 批量执行规则
     *
     * @param param 批量参数
     * @return list
     */
    @RequestLine("POST /ruleEngine/batchExecute")
    List<BatchOutPut> batchExecute(Map<String, Object> param);

}
