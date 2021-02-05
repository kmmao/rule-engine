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
package cn.ruleengine.web.controller.ruleset;

import cn.ruleengine.web.annotation.DataPermission;
import cn.ruleengine.web.annotation.ReSubmitLock;
import cn.ruleengine.web.annotation.SystemLog;
import cn.ruleengine.web.enums.Permission;
import cn.ruleengine.web.service.ruleset.RuleSetService;
import cn.ruleengine.web.vo.base.IdRequest;
import cn.ruleengine.web.vo.base.PageRequest;
import cn.ruleengine.web.vo.base.Param;
import cn.ruleengine.web.vo.base.BaseResult;
import cn.ruleengine.web.vo.base.PageResult;
import cn.ruleengine.web.vo.base.PlainResult;
import cn.ruleengine.web.vo.common.ViewRequest;
import cn.ruleengine.web.vo.generalrule.ListGeneralRuleRequest;
import cn.ruleengine.web.vo.ruleset.*;
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
 * @author 丁乾文
 * @create 2020/12/29
 * @since 1.0.0
 */
@Api(tags = "规则集控制器")
@RestController
@RequestMapping("ruleEngine/ruleSet")
public class RuleSetController {

    @Resource
    private RuleSetService ruleSetService;


    /**
     * 获取规则集列表
     *
     * @param pageRequest 分页查询参数
     * @return page
     */
    @PostMapping("list")
    @ApiOperation("规则集列表")
    public PageResult<ListRuleSetResponse> list(@RequestBody PageRequest<ListGeneralRuleRequest> pageRequest) {
        return this.ruleSetService.list(pageRequest);
    }

    /**
     * 保存或者更新规则集定义信息
     *
     * @param ruleSetDefinition 规则集定义信息
     * @return 规则集id
     */
    @ReSubmitLock
    @DataPermission(id = "#ruleSetDefinition.id", dataType = Permission.DataType.RULE_SET, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("saveOrUpdateRuleSetDefinition")
    @ApiOperation("保存或者更新规则集定义信息")
    public BaseResult saveOrUpdateRuleSetDefinition(@Valid @RequestBody RuleSetDefinition ruleSetDefinition) {
        PlainResult<Integer> plainResult = new PlainResult<>();
        plainResult.setData(ruleSetService.saveOrUpdateRuleSetDefinition(ruleSetDefinition));
        return plainResult;
    }

    /**
     * 获取规则集定义信息
     *
     * @param idRequest 规则集id
     * @return 规则集定义信息
     */
    @DataPermission(id = "#idRequest.id", dataType = Permission.DataType.RULE_SET, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("getRuleSetDefinition")
    @ApiOperation("查询规则集定义信息")
    public BaseResult getRuleSetDefinition(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<RuleSetDefinition> plainResult = new PlainResult<>();
        plainResult.setData(ruleSetService.getRuleSetDefinition(idRequest.getId()));
        return plainResult;
    }


    /**
     * 生成待发布版本，更新规则数据
     *
     * @param ruleSetBody 规则集配置数据
     * @return true
     */
    @ReSubmitLock
    @DataPermission(id = "#ruleSetBody.id", dataType = Permission.DataType.RULE_SET, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("generationRelease")
    @ApiOperation("生成规则集代发布")
    public BaseResult generationRelease(@Valid @RequestBody RuleSetBody ruleSetBody) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleSetService.generationRelease(ruleSetBody));
        return plainResult;
    }

    /**
     * 规则集发布
     *
     * @param idRequest 规则集id
     * @return true
     */
    @ReSubmitLock
    @SystemLog
    @DataPermission(id = "#idRequest.id", dataType = Permission.DataType.RULE_SET, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("publish")
    @ApiOperation("发布规则集")
    public BaseResult publish(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleSetService.publish(idRequest.getId()));
        return plainResult;
    }

    /**
     * 更新规则集信息
     *
     * @param ruleSetBody 规则配置数据
     * @return true执行成功
     */
    @ReSubmitLock
    @DataPermission(id = "#ruleSetBody.id", dataType = Permission.DataType.RULE_SET, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("updateRuleSet")
    @ApiOperation("更新规则集信息")
    public BaseResult updateRuleSet(@Valid @RequestBody RuleSetBody ruleSetBody) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleSetService.updateRuleSet(ruleSetBody));
        return plainResult;
    }

    /**
     * 获取规则集信息
     *
     * @param idRequest 规则集id
     * @return 规则集信息
     */
    @DataPermission(id = "#idRequest.id", dataType = Permission.DataType.RULE_SET, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("getRuleSetConfig")
    @ApiOperation("获取规则集配置信息")
    public BaseResult getRuleSetConfig(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<GetRuleSetResponse> plainResult = new PlainResult<>();
        plainResult.setData(ruleSetService.getRuleSetConfig(idRequest.getId()));
        return plainResult;
    }

    /**
     * 规则集预览
     *
     * @param viewRequest 规则集id
     * @return ViewRuleSetResponse
     */
    @DataPermission(id = "#viewRequest.id", dataType = Permission.DataType.RULE_SET, operationType = Permission.OperationType.VALID_WORKSPACE)
    @PostMapping("view")
    @ApiOperation("获取规则集信息")
    public BaseResult view(@Valid @RequestBody ViewRequest viewRequest) {
        PlainResult<ViewRuleSetResponse> plainResult = new PlainResult<>();
        plainResult.setData(ruleSetService.view(viewRequest));
        return plainResult;
    }

    /**
     * 删除规则集
     *
     * @param idRequest 规则集id
     * @return true
     */
    @DataPermission(id = "#idRequest.id", dataType = Permission.DataType.RULE_SET, operationType = Permission.OperationType.DELETE)
    @PostMapping("delete")
    @ApiOperation("删除规则集")
    public BaseResult delete(@Valid @RequestBody IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleSetService.delete(idRequest.getId()));
        return plainResult;
    }

    /**
     * 规则集Code是否存在
     *
     * @param param 规则集code
     * @return true存在
     */
    @PostMapping("codeIsExists")
    @ApiOperation("规则集Code是否存在")
    public PlainResult<Boolean> codeIsExists(@RequestBody @Valid Param<String> param) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(ruleSetService.ruleSetCodeIsExists(param.getParam()));
        return plainResult;
    }


}
