package com.engine.web.controller;


import com.engine.web.service.SymbolService;
import com.engine.web.vo.base.request.Param;
import com.engine.web.vo.base.response.PlainResult;
import com.engine.web.vo.symbol.SymbolResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Api(tags = "运算符控制器")
@RestController
@RequestMapping("ruleEngine/symbol")
public class SymbolController {

    @Resource
    private SymbolService symbolService;

    /**
     * 获取规则引擎运算符
     *
     * @param param 例如：CONTROLLER
     * @return >,<,=..
     */
    @PostMapping("getByType")
    @ApiOperation("变量列表")
    public PlainResult<List<SymbolResponse>> getByType(@RequestBody @Valid Param<String> param) {
        PlainResult<List<SymbolResponse>> listPlainResult = new PlainResult<>();
        listPlainResult.setData(symbolService.getByType(param.getParam()));
        return listPlainResult;
    }

}
