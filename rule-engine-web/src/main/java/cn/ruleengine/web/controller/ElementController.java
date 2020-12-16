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
import cn.ruleengine.web.enums.DataPermissionType;
import cn.ruleengine.web.enums.PermissionType;
import cn.ruleengine.web.service.ElementService;
import cn.ruleengine.web.vo.base.request.IdRequest;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.request.Param;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.base.response.PlainResult;
import cn.ruleengine.web.vo.element.*;
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
 * @date 2020/7/14
 * @since 1.0.0
 */
@Api(tags = "元素控制器")
@RestController
@RequestMapping("ruleEngine/element")
public class ElementController {

    @Resource
    private ElementService elementService;

    /**
     * 添加元素
     *
     * @param addConditionRequest 元素信息
     * @return true
     */
    @ReSubmitLock
    @PostMapping("add")
    @ApiOperation("添加元素")
    public PlainResult<Boolean> add(@RequestBody @Valid AddElementRequest addConditionRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(elementService.add(addConditionRequest));
        return plainResult;
    }

    /**
     * 元素列表
     *
     * @param pageRequest param
     * @return ListElementResponse
     */
    @PostMapping("list")
    @ApiOperation("元素列表")
    public PageResult<ListElementResponse> list(@RequestBody PageRequest<ListElementRequest> pageRequest) {
        return elementService.list(pageRequest);
    }

    /**
     * 根据id查询元素
     *
     * @param idRequest 元素id
     * @return GetElementResponse
     */
    @DataPermission(id = "#idRequest.id", dataType = DataPermissionType.ELEMENT, type = PermissionType.VALID_WORKSPACE)
    @PostMapping("get")
    @ApiOperation("根据id查询元素")
    public PlainResult<GetElementResponse> get(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<GetElementResponse> plainResult = new PlainResult<>();
        plainResult.setData(elementService.get(idRequest.getId()));
        return plainResult;
    }

    /**
     * 根据元素id更新元素
     *
     * @param updateElementRequest 元素信息
     * @return true
     */
    @ReSubmitLock
    @DataPermission(id = "#updateElementRequest.id", dataType = DataPermissionType.ELEMENT, type = PermissionType.VALID_WORKSPACE)
    @PostMapping("update")
    @ApiOperation("根据id更新元素")
    public PlainResult<Boolean> update(@RequestBody @Valid UpdateElementRequest updateElementRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(elementService.update(updateElementRequest));
        return plainResult;
    }

    /**
     * 根据id删除元素
     *
     * @param idRequest 元素id
     * @return true
     */
    @DataPermission(id = "#idRequest.id", dataType = DataPermissionType.ELEMENT, type = PermissionType.DELETE)
    @PostMapping("delete")
    @ApiOperation("根据id删除元素")
    public PlainResult<Boolean> delete(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(elementService.delete(idRequest.getId()));
        return plainResult;
    }

    /**
     * 元素code是否存在
     *
     * @param param 元素code
     * @return true存在
     */
    @PostMapping("codeIsExists")
    @ApiOperation("元素code是否存在")
    public PlainResult<Boolean> codeIsExists(@RequestBody @Valid Param<String> param) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(elementService.elementCodeIsExists(param.getParam()));
        return plainResult;
    }

}
