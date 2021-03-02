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
package cn.ruleengine.web.controller.generalrule;

import cn.ruleengine.web.annotation.DataPermission;
import cn.ruleengine.web.annotation.ReSubmitLock;
import cn.ruleengine.web.annotation.SystemLog;
import cn.ruleengine.web.enums.Permission;
import cn.ruleengine.web.service.generalrule.GeneralRuleService;
import cn.ruleengine.common.vo.*;
import cn.ruleengine.web.vo.common.ViewRequest;
import cn.ruleengine.web.vo.generalrule.*;
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
@Api(tags = "普通规则控制器")
@RestController
@RequestMapping("ruleEngine/generalRule")
public class GeneralRuleController {

    @Resource
    private GeneralRuleService ruleService;

    /**
     * 获取规则列表
     *
     * @param pageRequest 分页查询参数
     * @return page
     */
    @PostMapping("list")
    @ApiOperation("规则列表")
    public PageResult<ListGeneralRuleResponse> list(@RequestBody PageRequest<ListGeneralRuleRequest> pageRequest) {
        return this.ruleService.list(pageRequest);
    }

    /**
     * 保存或者更新规则定义信息
     *
     * @param ruleDefinition 规则定义信息
     * @return 规则id
     */
    @ReSubmitLock
    @DataPermission(id = "#ruleDefinition.id", dataType = Permission.DataType.GENERAL_RULE, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("saveOrUpdateRuleDefinition")
    @ApiOperation("保存或者更新规则定义信息")
    public BaseResult saveOrUpdateRuleDefinition(@Valid @RequestBody GeneralRuleDefinition ruleDefinition) {
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
    @DataPermission(id = "#idRequest.id", dataType = Permission.DataType.GENERAL_RULE, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("getRuleDefinition")
    @ApiOperation("查询规则定义信息")
    public BaseResult getRuleDefinition(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<GeneralRuleDefinition> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.getRuleDefinition(idRequest.getId()));
        return plainResult;
    }

    /**
     * 生成待发布版本，更新规则数据
     *
     * @param generalRuleBody 规则配置数据
     * @return true
     */
    @ReSubmitLock
    @DataPermission(id = "#generalRuleBody.id", dataType = Permission.DataType.GENERAL_RULE, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("generationRelease")
    @ApiOperation("生成普通规则代发布")
    public BaseResult generationRelease(@Valid @RequestBody GeneralRuleBody generalRuleBody) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.generationRelease(generalRuleBody));
        return plainResult;
    }

    /**
     * 规则发布
     *
     * @param idRequest 规则id
     * @return true
     */
    @ReSubmitLock
    @SystemLog(tag = "普通规则发布")
    @DataPermission(id = "#idRequest.id", dataType = Permission.DataType.GENERAL_RULE, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("publish")
    @ApiOperation("发布规则")
    public BaseResult publish(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.publish(idRequest.getId()));
        return plainResult;
    }

    /**
     * 更新规则信息
     *
     * @param generalRuleBody 规则配置数据
     * @return true执行成功
     */
    @ReSubmitLock
    @DataPermission(id = "#generalRuleBody.id", dataType = Permission.DataType.GENERAL_RULE, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("updateRule")
    @ApiOperation("更新规则信息")
    public BaseResult updateRule(@Valid @RequestBody GeneralRuleBody generalRuleBody) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.updateRule(generalRuleBody));
        return plainResult;
    }

    /**
     * 获取规则信息
     *
     * @param idRequest 规则id
     * @return 规则信息
     */
    @DataPermission(id = "#idRequest.id", dataType = Permission.DataType.GENERAL_RULE, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("getRuleConfig")
    @ApiOperation("获取规则配置信息")
    public BaseResult getRuleConfig(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<GetGeneralRuleResponse> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.getRuleConfig(idRequest.getId()));
        return plainResult;
    }

    /**
     * 规则预览
     *
     * @param viewRequest 规则id
     * @return GetRuleResponse
     */
    @DataPermission(id = "#viewRequest.id", dataType = Permission.DataType.GENERAL_RULE, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("view")
    @ApiOperation("预览规则")
    public BaseResult view(@Valid @RequestBody ViewRequest viewRequest) {
        PlainResult<ViewGeneralRuleResponse> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.view(viewRequest));
        return plainResult;
    }


    /**
     * 删除规则
     *
     * @param idRequest 规则id
     * @return true
     */
    @DataPermission(id = "#idRequest.id", dataType = Permission.DataType.GENERAL_RULE, operationType = Permission.OperationType.DELETE)
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
    @ApiOperation("规则Code是否存在")
    public PlainResult<Boolean> codeIsExists(@RequestBody @Valid Param<String> param) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleService.ruleCodeIsExists(param.getParam()));
        return plainResult;
    }

}
