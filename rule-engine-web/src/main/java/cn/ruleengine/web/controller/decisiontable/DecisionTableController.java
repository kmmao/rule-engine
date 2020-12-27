package cn.ruleengine.web.controller.decisiontable;

import cn.ruleengine.web.service.decisiontable.DecisionTableService;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.decisiontable.ListDecisionTableRequest;
import cn.ruleengine.web.vo.decisiontable.ListDecisionTableResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/27
 * @since 1.0.0
 */
@Api(tags = "决策表控制器")
@RestController
@RequestMapping("ruleEngine/decisionTable")
public class DecisionTableController {

    @Resource
    private DecisionTableService decisionTableService;

    /**
     * 决策表列表
     *
     * @param pageRequest 分页查询参数
     * @return page
     */
    @PostMapping("list")
    @ApiOperation("决策表列表")
    public PageResult<ListDecisionTableResponse> list(@RequestBody PageRequest<ListDecisionTableRequest> pageRequest) {
        return this.decisionTableService.list(pageRequest);
    }

}
