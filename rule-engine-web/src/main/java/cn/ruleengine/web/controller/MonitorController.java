package cn.ruleengine.web.controller;

import cn.ruleengine.web.service.MonitorService;
import cn.ruleengine.web.vo.base.BaseResult;
import cn.ruleengine.web.vo.base.PlainResult;
import cn.ruleengine.web.vo.monitor.CacheInfoResponse;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2021/1/19
 * @since 1.0.0
 */
@Api(tags = "监控控制器")
@RestController
@RequestMapping("monitor")
public class MonitorController {

    @Resource
    private MonitorService monitorService;

    /**
     * 缓存监控信息
     *
     * @return CacheMonitorResponse
     */
    @GetMapping("cacheInfo")
    public BaseResult cacheInfo() {
        PlainResult<CacheInfoResponse> result = new PlainResult<>();
        result.setData(monitorService.cacheInfo());
        return result;
    }

    /**
     * 服务监控
     *
     * @return info
     */
    @GetMapping("serverInfo")
    public BaseResult serverInfo() {
        PlainResult<Object> result = new PlainResult<>();
        result.setData(monitorService.serverInfo());
        return result;
    }


    /**
     * 当前工作空间规则引擎监控数据，系统中变量数量，引擎中规则数量，函数缓存数量，已发布数据引擎执行次数
     *
     * @return info
     */
    @GetMapping("engineInfo")
    public BaseResult engineInfo() {
        PlainResult<Object> result = new PlainResult<>();
        result.setData(monitorService.engineInfo());
        return result;
    }


}
