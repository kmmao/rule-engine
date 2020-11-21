package com.engine.web.controller;

import com.engine.web.service.MenuService;
import com.engine.web.vo.base.request.IdRequest;
import com.engine.web.vo.base.response.BaseResult;
import com.engine.web.vo.base.response.PlainResult;
import com.engine.web.vo.menu.ListMenuResponse;
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
 * @author 丁乾文
 * @create 2019/10/24
 * @since 1.0.0
 */
@Api(tags = "菜单控制器")
@RestController
@RequestMapping("menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 根据用户id查询他的菜单
     *
     * @param idRequest userId
     * @return BaseResult
     */
    @PostMapping("listMenuByUserId")
    @ApiOperation("根据用户Id查询他的菜单!")
    public BaseResult listMenuByUserId(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<List<ListMenuResponse>> plainResult = new PlainResult<>();
        plainResult.setData(menuService.listMenuByUserId(idRequest.getId()));
        return plainResult;
    }

    /**
     * 当前用户菜单树
     *
     * @return BaseResult
     */
    @PostMapping("menuTree")
    @ApiOperation("当前用户菜单树!")
    public BaseResult menuTree() {
        PlainResult<List<ListMenuResponse>> plainResult = new PlainResult<>();
        plainResult.setData(menuService.menuTree());
        return plainResult;
    }
}
