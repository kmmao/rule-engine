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

    @PostMapping("add")
    @ApiOperation("添加条件")
    public PlainResult<Boolean> add(@RequestBody @Valid AddConditionRequest addConditionRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(conditionService.save(addConditionRequest));
        return plainResult;
    }

    @PostMapping("get")
    @ApiOperation("根据id查询条件")
    public PlainResult<ConditionResponse> getById(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<ConditionResponse> plainResult = new PlainResult<>();
        plainResult.setData(conditionService.getById(idRequest.getId()));
        return plainResult;
    }

    @PostMapping("update")
    @ApiOperation("根据id更新条件")
    public PlainResult<Boolean> update(@RequestBody @Valid UpdateConditionRequest updateConditionRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(conditionService.update(updateConditionRequest));
        return plainResult;
    }

    @PostMapping("list")
    @ApiOperation("条件列表")
    public PageResult<ListConditionResponse> list(@RequestBody PageRequest<ListConditionRequest> pageRequest) {
        return conditionService.list(pageRequest);
    }

    @RoleAuth
    @PostMapping("delete")
    @ApiOperation("根据id删除条件")
    public PlainResult<Boolean> delete(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(conditionService.delete(idRequest.getId()));
        return plainResult;
    }
}
