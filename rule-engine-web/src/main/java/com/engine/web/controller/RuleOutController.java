package com.engine.web.controller;


import com.engine.web.annotation.NoAuth;
import com.engine.web.annotation.RateLimit;
import com.engine.web.enums.RateLimitEnum;
import com.engine.web.service.RuleOutService;
import com.engine.web.vo.base.response.BaseResult;
import com.engine.web.annotation.SystemLog;
import com.engine.web.vo.base.response.PlainResult;
import com.engine.web.vo.rule.BatchExecuteRuleRequest;
import com.engine.web.vo.rule.ExecuteRuleRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/16
 * @since 1.0.0
 */
@Api(tags = "调用规则控制器")
@RestController
@RequestMapping("ruleEngine")
public class RuleOutController {


    @Resource
    private RuleOutService ruleOutService;

    /**
     * 执行单个规则，获取执行结果
     *
     * @param executeRuleRequest 执行规则入参
     * @return 规则执行结果
     */
    @NoAuth
    @SystemLog
    @RateLimit(limit = 60, type = RateLimitEnum.URL_IP)
    @PostMapping("execute")
    @ApiOperation("执行单个规则，获取执行结果")
    public BaseResult executeRule(@RequestBody @Valid ExecuteRuleRequest executeRuleRequest) {
        PlainResult<Object> plainResult = new PlainResult<>();
        plainResult.setData(ruleOutService.executeRule(executeRuleRequest));
        return plainResult;
    }

    /**
     * 批量执行多个规则(一次建议最多1000个)，获取执行结果
     *
     * @param batchExecuteRuleRequest 执行规则入参
     * @return 规则执行结果
     */
    @NoAuth
    @SystemLog
    @RateLimit(limit = 6, type = RateLimitEnum.URL_IP)
    @PostMapping("batchExecute")
    @ApiOperation("批量执行多个规则，获取执行结果")
    public BaseResult batchExecuteRule(@RequestBody @Valid BatchExecuteRuleRequest batchExecuteRuleRequest) {
        PlainResult<Object> plainResult = new PlainResult<>();
        plainResult.setData(ruleOutService.batchExecuteRule(batchExecuteRuleRequest));
        return plainResult;
    }

}
