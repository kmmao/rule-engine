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
import com.engine.web.vo.base.response.PageResult;
import com.engine.web.vo.condition.*;
import com.engine.web.service.ConditionService;
import com.engine.web.vo.base.request.IdRequest;
import com.engine.web.vo.base.request.PageRequest;
import com.engine.web.vo.base.response.PlainResult;
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
@Api(tags = "条件控制器")
@RestController
@RequestMapping("ruleEngine/condition")
public class ConditionController {

    @Resource
    private ConditionService conditionService;

    /**
     * 添加条件
     *
     * @param addConditionRequest 条件信息
     * @return true
     */
    @PostMapping("add")
    @ApiOperation("添加条件")
    public PlainResult<Boolean> add(@RequestBody @Valid AddConditionRequest addConditionRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(conditionService.save(addConditionRequest));
        return plainResult;
    }

    /**
     * 根据id查询条件
     *
     * @param idRequest 条件id
     * @return ConditionResponse
     */
    @PostMapping("get")
    @ApiOperation("根据id查询条件")
    public PlainResult<ConditionResponse> getById(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<ConditionResponse> plainResult = new PlainResult<>();
        plainResult.setData(conditionService.getById(idRequest.getId()));
        return plainResult;
    }

    /**
     * 根据id更新条件
     *
     * @param updateConditionRequest 条件信息
     * @return true
     */
    @PostMapping("update")
    @ApiOperation("根据id更新条件")
    public PlainResult<Boolean> update(@RequestBody @Valid UpdateConditionRequest updateConditionRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(conditionService.update(updateConditionRequest));
        return plainResult;
    }

    /**
     * 条件列表
     *
     * @param pageRequest param
     * @return ListConditionResponse
     */
    @PostMapping("list")
    @ApiOperation("条件列表")
    public PageResult<ListConditionResponse> list(@RequestBody PageRequest<ListConditionRequest> pageRequest) {
        return conditionService.list(pageRequest);
    }

    /**
     * 删除条件
     *
     * @param idRequest 条件id
     * @return true：删除成功
     */
    @RoleAuth
    @PostMapping("delete")
    @ApiOperation("根据id删除条件")
    public PlainResult<Boolean> delete(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(conditionService.delete(idRequest.getId()));
        return plainResult;
    }
}
