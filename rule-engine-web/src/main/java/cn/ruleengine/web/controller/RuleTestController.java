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
import cn.ruleengine.web.enums.DataPermissionType;
import cn.ruleengine.web.enums.PermissionType;
import cn.ruleengine.web.service.RuleTestService;
import cn.ruleengine.web.vo.base.response.BaseResult;
import cn.ruleengine.web.vo.base.response.PlainResult;

import cn.ruleengine.web.vo.rule.RunTestRequest;
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
@Api(tags = "规则测试控制器")
@RestController
@RequestMapping("ruleEngine/ruleTest")
public class RuleTestController {

    @Resource
    private RuleTestService ruleTestService;

    /**
     * 规则模拟运行
     *
     * @param runTestRequest 规则参数信息
     * @return result
     */
    @DataPermission(id = "#runTestRequest.id", dataType = DataPermissionType.RULE, type = PermissionType.VALID_WORKSPACE)
    @PostMapping("run")
    @ApiOperation("模拟运行")
    public BaseResult fun(@RequestBody @Valid RunTestRequest runTestRequest) {
        PlainResult<Object> plainResult = new PlainResult<>();
        plainResult.setData(ruleTestService.run(runTestRequest));
        return plainResult;
    }

}
