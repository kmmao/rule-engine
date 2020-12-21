package cn.ruleengine.core.monitor;

import cn.ruleengine.core.rule.Rule;
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
public class RuleMonitorProxy implements MethodInterceptor {

    private Object ruleObject;

    public Rule getRuleProxy(Rule ruleObject) {
        this.ruleObject = ruleObject;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ruleObject.getClass());
        enhancer.setCallback(this);
        return (Rule) enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // TODO: 2020/12/21 待完成
        return method.invoke(this.ruleObject, args);
    }

}
