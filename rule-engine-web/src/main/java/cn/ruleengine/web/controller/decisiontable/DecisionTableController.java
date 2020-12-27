package cn.ruleengine.web.controller.decisiontable;

import cn.ruleengine.web.service.decisiontable.DecisionTableService;
import io.swagger.annotations.Api;
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

}
