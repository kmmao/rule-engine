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
package cn.ruleengine.web.controller;

import cn.ruleengine.web.annotation.DataPermission;
import cn.ruleengine.web.annotation.ReSubmitLock;
import cn.ruleengine.web.annotation.RoleAuth;
import cn.ruleengine.web.annotation.SystemLog;
import cn.ruleengine.web.enums.DataPermissionType;
import cn.ruleengine.web.enums.PermissionType;
import cn.ruleengine.web.service.RuleService;
import cn.ruleengine.web.vo.base.request.IdRequest;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.request.Param;
import cn.ruleengine.web.vo.base.response.BaseResult;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.base.response.PlainResult;
import cn.ruleengine.web.vo.rule.*;
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
    @ReSubmitLock
    @DataPermission(id = "#ruleDefinition.id", dataType = DataPermissionType.RULE, type = PermissionType.VALID_WORKSPACE)
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
    @DataPermission(id = "#idRequest.id", dataType = DataPermissionType.RULE, type = PermissionType.VALID_WORKSPACE)
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
    @ReSubmitLock
    @DataPermission(id = "#releaseRequest.id", dataType = DataPermissionType.RULE, type = PermissionType.VALID_WORKSPACE)
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
    @ReSubmitLock
    @SystemLog
    @DataPermission(id = "#idRequest.id", dataType = DataPermissionType.RULE, type = PermissionType.VALID_WORKSPACE)
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
    @ReSubmitLock
    @DataPermission(id = "#updateRuleRequest.id", dataType = DataPermissionType.RULE, type = PermissionType.VALID_WORKSPACE)
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
    @DataPermission(id = "#idRequest.id", dataType = DataPermissionType.RULE, type = PermissionType.VALID_WORKSPACE)
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
    @DataPermission(id = "#idRequest.id", dataType = DataPermissionType.RULE, type = PermissionType.VALID_WORKSPACE)
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
    @DataPermission(id = "#idRequest.id", dataType = DataPermissionType.RULE, type = PermissionType.VALID_WORKSPACE)
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
    @DataPermission(id = "#idRequest.id", dataType = DataPermissionType.RULE, type = PermissionType.DELETE)
    @PostMapping("delete")
    @ApiOperation("删除规则")
    public BaseResult delete(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.delete(idRequest.getId()));
        return plainResult;
    }

    /**
     * 规则code是否存在
     *
     * @param param 规则code
     * @return true存在
     */
    @PostMapping("codeIsExists")
    @ApiOperation("规则code是否存在")
    public PlainResult<Boolean> codeIsExists(@RequestBody @Valid Param<String> param) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.ruleCodeIsExists(param.getParam()));
        return plainResult;
    }

}
