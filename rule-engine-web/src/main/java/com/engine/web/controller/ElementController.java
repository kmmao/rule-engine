package com.engine.web.controller;

import com.engine.web.annotation.RoleAuth;
import com.engine.web.vo.base.request.IdRequest;
import com.engine.web.vo.base.response.BaseResult;
import com.engine.web.vo.base.response.PageResult;
import com.engine.web.vo.element.*;
import com.engine.web.service.ElementService;
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
@Api(tags = "元素控制器")
@RestController
@RequestMapping("ruleEngine/element")
public class ElementController {

    @Resource
    private ElementService elementService;

    @PostMapping("add")
    @ApiOperation("添加元素")
    public PlainResult<Boolean> add(@RequestBody @Valid AddElementRequest addConditionRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(elementService.add(addConditionRequest));
        return plainResult;
    }

    @PostMapping("list")
    @ApiOperation("元素列表")
    public PageResult<ListElementResponse> list(@RequestBody PageRequest<ListElementRequest> pageRequest) {
        return elementService.list(pageRequest);
    }

    @PostMapping("get")
    @ApiOperation("根据id查询元素")
    public PlainResult<GetElementResponse> get(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<GetElementResponse> plainResult = new PlainResult<>();
        plainResult.setData(elementService.get(idRequest.getId()));
        return plainResult;
    }

    @PostMapping("update")
    @ApiOperation("根据id更新元素")
    public PlainResult<Boolean> update(@RequestBody @Valid UpdateElementRequest updateElementRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(elementService.update(updateElementRequest));
        return plainResult;
    }

    @RoleAuth
    @PostMapping("delete")
    @ApiOperation("根据id删除元素")
    public PlainResult<Boolean> delete(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(elementService.delete(idRequest.getId()));
        return plainResult;
    }

}
