package cn.ruleengine.web.controller.system;

import cn.ruleengine.web.vo.base.response.BaseResult;
import cn.ruleengine.web.vo.base.response.PlainResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/15
 * @since 1.0.0
 */
@Api(tags = "服务监控控制器")
@RestController
@RequestMapping("serverMonitor")
public class ServerMonitorController {

    @GetMapping("getInfo")
    public BaseResult getInfo() {
        PlainResult<Object> result = new PlainResult<>();
        result.setData(null);
        return result;
    }
}
