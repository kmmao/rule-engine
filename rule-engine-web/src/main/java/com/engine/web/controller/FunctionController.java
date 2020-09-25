package com.engine.web.controller;

import com.engine.web.annotation.RoleAuth;
import com.engine.web.service.FunctionService;
import com.engine.web.vo.base.request.IdRequest;
import com.engine.web.vo.base.request.PageRequest;
import com.engine.web.vo.base.response.PageResult;
import com.engine.web.vo.base.response.PlainResult;
import com.engine.web.vo.function.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.ApplicationContext;
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
@Api(tags = "函数控制器")
@RestController
@RequestMapping("ruleEngine/function")
public class FunctionController {

    @Resource
    private FunctionService functionService;

    @PostMapping("list")
    @ApiOperation("函数列表")
    public PageResult<ListFunctionResponse> list(@RequestBody PageRequest<ListFunctionRequest> pageRequest) {
        return functionService.list(pageRequest);
    }

    @PostMapping("get")
    @ApiOperation("查询函数详情")
    public PlainResult<GetFunctionResponse> get(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<GetFunctionResponse> plainResult = new PlainResult<>();
        plainResult.setData(functionService.get(idRequest.getId()));
        return plainResult;
    }

    @RoleAuth
    @PostMapping("add")
    @ApiOperation("添加函数")
    public PlainResult<Boolean> add(@Valid AddFunction addFunction) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(functionService.add(addFunction));
        return plainResult;
    }

    @RoleAuth
    @PostMapping("update")
    @ApiOperation("更新函数")
    public PlainResult<Boolean> update(@Valid UpdateFunction updateFunction) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(functionService.update(updateFunction));
        return plainResult;
    }


    @PostMapping("run")
    @ApiOperation("函数模拟测试")
    public PlainResult<Object> run(@Valid @RequestBody RunFunction runFunction) {
        PlainResult<Object> plainResult = new PlainResult<>();
        plainResult.setData(functionService.run(runFunction));
        return plainResult;
    }

}
