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
package com.engine.web.controller;

import com.engine.web.annotation.RoleAuth;
import com.engine.web.service.RuleService;
import com.engine.web.vo.base.request.IdRequest;
import com.engine.web.vo.base.request.PageRequest;
import com.engine.web.vo.base.response.BaseResult;
import com.engine.web.vo.base.response.PageResult;
import com.engine.web.vo.base.response.PlainResult;
import com.engine.web.vo.rule.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
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
@Api(tags = "规则控制器")
@RestController
@RequestMapping("ruleEngine/rule")
public class RuleController {

    @Resource
    private RuleService ruleService;

    /**
     * 获取规则列表
     *
     * @param pageRequest 分页查询参数
     * @return page
     */
    @PostMapping("list")
    @ApiOperation("规则列表")
    public PageResult<ListRuleResponse> list(@RequestBody PageRequest<ListRuleRequest> pageRequest) {
        return ruleService.list(pageRequest);
    }

    /**
     * 保存或者更新规则定义信息
     *
     * @param ruleDefinition 规则定义信息
     * @return 规则id
     */
    @PostMapping("saveOrUpdateRuleDefinition")
    @ApiOperation("保存或者更新规则定义信息")
    public BaseResult saveOrUpdateRuleDefinition(@Valid @RequestBody RuleDefinition ruleDefinition) {
        PlainResult<Integer> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.saveOrUpdateRuleDefinition(ruleDefinition));
        return plainResult;
    }

    /**
     * 获取规则定义信息
     *
     * @param idRequest 规则id
     * @return 规则定义信息
     */
    @PostMapping("getRuleDefinition")
    @ApiOperation("查询规则定义信息")
    public BaseResult getRuleDefinition(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<RuleDefinition> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.getRuleDefinition(idRequest.getId()));
        return plainResult;
    }

    /**
     * 生成待发布版本，更新规则数据
     *
     * @param releaseRequest 规则配置数据
     * @return true
     */
    @PostMapping("generationRelease")
    @ApiOperation("生成代发布")
    public BaseResult generationRelease(@Validated @RequestBody GenerationReleaseRequest releaseRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.generationRelease(releaseRequest));
        return plainResult;
    }

    /**
     * 规则发布
     *
     * @param idRequest 规则id
     * @return true
     */
    @PostMapping("publish")
    @ApiOperation("发布规则")
    public BaseResult publish(@Validated @RequestBody IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.publish(idRequest.getId()));
        return plainResult;
    }

    /**
     * 更新规则信息
     *
     * @param updateRuleRequest 规则配置数据
     * @return true执行成功
     */
    @PostMapping("updateRule")
    @ApiOperation("更新规则信息")
    public BaseResult updateRule(@Valid @RequestBody UpdateRuleRequest updateRuleRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.updateRule(updateRuleRequest));
        return plainResult;
    }

    /**
     * 获取规则信息
     *
     * @param idRequest 规则id
     * @return 规则信息
     */
    @PostMapping("getRuleConfig")
    @ApiOperation("获取规则配置信息")
    public BaseResult getRule(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<GetRuleResponse> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.getRuleConfig(idRequest.getId()));
        return plainResult;
    }

    /**
     * 规则预览
     *
     * @param idRequest 规则id
     * @return GetRuleResponse
     */
    @PostMapping("getViewRule")
    @ApiOperation("获取规则信息")
    public BaseResult getViewRule(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<ViewRuleResponse> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.getViewRule(idRequest.getId()));
        return plainResult;
    }

    /**
     * 获取预览已发布的规则
     *
     * @param idRequest 规则id
     * @return GetRuleResponse
     */
    @PostMapping("getPublishRule")
    @ApiOperation("获取发布规则信息")
    public BaseResult getPublishRule(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<ViewRuleResponse> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.getPublishRule(idRequest.getId()));
        return plainResult;
    }


    /**
     * 删除规则
     *
     * @param idRequest 规则id
     * @return true
     */
    @RoleAuth
    @PostMapping("delete")
    @ApiOperation("删除规则")
    public BaseResult delete(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.delete(idRequest.getId()));
        return plainResult;
    }
}
