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

    private final GeneralRule generalRule;

    private final RuleEngineConfiguration configuration;

    public GeneralRuleMonitorProxy(RuleEngineConfiguration configuration, GeneralRule generalRule) {
        this.generalRule = generalRule;
        this.configuration = configuration;
        Monitor monitor = configuration.getMonitor();
        // 默认使用AtomicGeneralIndicator
        AtomicGeneralIndicator atomicGeneralIndicator = new AtomicGeneralIndicator();
        atomicGeneralIndicator.setId(generalRule.getId());
        atomicGeneralIndicator.setCode(generalRule.getCode());
        monitor.initGeneralRuleMonitor(generalRule.getId(), atomicGeneralIndicator);
    }

    public GeneralRule getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(generalRule.getClass());
        enhancer.setCallback(this);
        return (GeneralRule) enhancer.create();
    }

    /**
     * 普通规则监控统计
     * <p>
     * 还需要优化
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Indicator indicator = configuration.getMonitor().getGeneralRuleMonitor(this.generalRule.getId());
        switch (method.getName()) {
            case "getActionValue":
                indicator.incrementActionCount();
                break;
            case "getDefaultActionValue":
                indicator.incrementDefaultActionCount();
                break;
            case "execute":
                indicator.incrementTotalCount();
                long startTime = System.currentTimeMillis();
                Object invoke;
                try {
                    invoke = method.invoke(this.generalRule, args);
                } catch (Exception e) {
                    indicator.incrementErrorCount();
                    throw e;
                } finally {
                    long cost = System.currentTimeMillis() - startTime;
                    indicator.addTimeConsuming(cost);
                }
                if (invoke == null) {
                    indicator.incrementMissesCount();
                }
                return invoke;
            default:
                break;
        }
        return method.invoke(this.generalRule, args);
    }

}
