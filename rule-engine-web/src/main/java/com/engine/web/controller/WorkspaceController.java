package com.engine.web.controller;

import com.engine.web.service.WorkspaceService;
import com.engine.web.vo.base.request.IdRequest;
import com.engine.web.vo.base.response.BaseResult;
import com.engine.web.vo.base.response.PlainResult;
import com.engine.web.vo.workspace.Workspace;
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
 * @create 2020/11/21
 * @since 1.0.0
 */
@Api(tags = "工作空间控制器")
@RestController
@RequestMapping("workspace")
public class WorkspaceController {

    @Resource
    private WorkspaceService workspaceService;

    @PostMapping("list")
    @ApiOperation("工作空间列表")
    public BaseResult list() {
        PlainResult<List<Workspace>> plainResult = new PlainResult<>();
        plainResult.setData(workspaceService.list());
        return plainResult;
    }

    @PostMapping("change")
    @ApiOperation("切换工作空间")
    public BaseResult change(@RequestBody @Valid IdRequest idRequest) {
        PlainResult<Boolean> plainResult = new PlainResult<>();
        plainResult.setData(workspaceService.change(idRequest.getId()));
        return plainResult;
    }

    @PostMapping("currentWorkspace")
    @ApiOperation("当前工作空间")
    public BaseResult currentWorkspace() {
        PlainResult<Workspace> plainResult = new PlainResult<>();
        plainResult.setData(workspaceService.currentWorkspace());
        return plainResult;
    }
}
