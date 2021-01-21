/**
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ruleengine.web.controller.decisiontable;


import cn.ruleengine.web.annotation.NoAuth;
import cn.ruleengine.web.service.RuleEngineOutService;
import cn.ruleengine.web.vo.base.BaseResult;
import cn.ruleengine.web.vo.base.PlainResult;
import cn.ruleengine.web.vo.output.BatchExecuteRequest;
import cn.ruleengine.web.vo.output.ExecuteRequest;
import cn.ruleengine.web.vo.output.IsExistsRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Api(tags = "调用决策表控制器")
@RestController
@RequestMapping("ruleEngine/decisionTable")
public class DecisionTableOutController {


    @Qualifier(value = "decisionTableOutServiceImpl")
    @Resource
    private RuleEngineOutService ruleOutService;

    /**
     * 执行单个决策表，获取执行结果
     *
     * @param executeRequest 执行决策表入参
     * @return 决策表执行结果
     */
    @NoAuth
    @PostMapping("execute")
    @ApiOperation("执行单个决策表，获取执行结果")
    public BaseResult executeRule(@RequestBody @Valid ExecuteRequest executeRequest) {
        PlainResult<Object> plainResult = new PlainResult<>();
        plainResult.setData(ruleOutService.execute(executeRequest));
        return plainResult;
    }

    /**
     * 批量执行多个决策表(一次建议最多1000个)，获取执行结果
     *
     * @param batchExecuteRequest 执行决策表入参
     * @return 决策表执行结果
     */
    @NoAuth
    @PostMapping("batchExecute")
    @ApiOperation("批量执行多个决策表，获取执行结果")
    public BaseResult batchExecuteRule(@RequestBody @Valid BatchExecuteRequest batchExecuteRequest) {
        PlainResult<Object> plainResult = new PlainResult<>();
        plainResult.setData(ruleOutService.batchExecute(batchExecuteRequest));
        return plainResult;
    }

    /**
     * 引擎中是否存在这个决策表
     *
     * @param isExistsRequest 参数
     * @return true存在
     */
    @NoAuth
    @PostMapping("isExists")
    @ApiOperation("引擎中是否存在这个决策表")
    public BaseResult isExists(@RequestBody @Valid IsExistsRequest isExistsRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleOutService.isExists(isExistsRequest));
        return plainResult;
    }
}

