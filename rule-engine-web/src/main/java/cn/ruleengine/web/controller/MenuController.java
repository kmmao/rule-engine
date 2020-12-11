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

import cn.ruleengine.web.service.MenuService;
import cn.ruleengine.web.vo.base.request.IdRequest;
import cn.ruleengine.web.vo.base.response.BaseResult;
import cn.ruleengine.web.vo.base.response.PlainResult;
import cn.ruleengine.web.vo.menu.ListMenuResponse;
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
