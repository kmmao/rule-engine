package cn.ruleengine.web.controller.system;

import cn.ruleengine.web.service.system.CacheMonitorService;
import cn.ruleengine.web.vo.base.response.BaseResult;
import cn.ruleengine.web.vo.base.response.PlainResult;
import cn.ruleengine.web.vo.system.CacheMonitorResponse;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/15
 * @since 1.0.0
 */
@Api(tags = "缓存监控监控控制器")
@RestController
@RequestMapping("cacheMonitor")
public class CacheMonitorController {

    @Resource
    private CacheMonitorService cacheMonitorService;

    /**
     * 缓存监控信息
     *
     * @return CacheMonitorResponse
     */
    @GetMapping("getInfo")
    public BaseResult getInfo() {
        PlainResult<CacheMonitorResponse> result = new PlainResult<>();
        result.setData(cacheMonitorService.getInfo());
        return result;
    }
}
