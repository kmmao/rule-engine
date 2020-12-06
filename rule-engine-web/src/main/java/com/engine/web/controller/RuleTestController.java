package com.engine.web.controller;

import com.engine.web.service.RuleTestService;
import com.engine.web.vo.base.response.BaseResult;
import com.engine.web.vo.base.response.PlainResult;
import com.engine.web.vo.rule.*;
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
 * @date 2020/8/24
 * @since 1.0.0
 */
@Api(tags = "规则测试控制器")
@RestController
@RequestMapping("ruleEngine/ruleTest")
public class RuleTestController {

    @Resource
    private RuleTestService ruleTestService;

    /**
     * 规则模拟运行
     *
     * @param executeRuleRequest 规则参数信息
     * @return result
     */
    @PostMapping("run")
    @ApiOperation("模拟运行")
    public BaseResult fun(@RequestBody @Valid ExecuteRuleRequest executeRuleRequest) {
        PlainResult<Object> plainResult = new PlainResult<>();
        plainResult.setData(ruleTestService.run(executeRuleRequest));
        return plainResult;
    }

}
