package cn.ruleengine.core.monitor;

import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.rule.GeneralRule;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/12/21
 * @since 1.0.0
 */
public class GeneralRuleMonitorProxy implements MethodInterceptor {

    private GeneralRule generalRule;

    private RuleEngineConfiguration configuration;

    public GeneralRuleMonitorProxy(RuleEngineConfiguration configuration, GeneralRule generalRule) {
        this.generalRule = generalRule;
        this.configuration = configuration;
        Monitor monitor = configuration.getMonitor();
        // 默认使用AtomicGeneralIndicator
        monitor.initGeneralRuleMonitor(generalRule.getId(), new AtomicGeneralIndicator());
    }

    public GeneralRule getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(generalRule.getClass());
        enhancer.setCallback(this);
        return (GeneralRule) enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // TODO: 2020/12/21 待完成 待过滤掉Object中的方法等等
        Indicator indicator = configuration.getMonitor().getGeneralRuleMonitor(this.generalRule.getId());
        long startTime = System.currentTimeMillis();
        Object invoke = method.invoke(this.generalRule, args);
        long cost = System.currentTimeMillis() - startTime;
        switch (method.getName()) {
            case "getActionValue":
                indicator.incrementActionCount();
                break;
            case "getDefaultActionValue":
                indicator.incrementDefaultActionCount();
                break;
            case "execute":
                indicator.incrementTotalCount();
                indicator.addTimeConsuming(cost);
                break;
            default:
                indicator.incrementMissesCount();
                break;
        }
        return invoke;
    }

}
