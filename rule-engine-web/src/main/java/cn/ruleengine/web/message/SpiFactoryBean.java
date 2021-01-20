package cn.ruleengine.web.message;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * @param <T>
 */
public class SpiFactoryBean<T> implements FactoryBean<T> {
    private Class<? extends ISenderSpi> spiClz;
    private List<ISenderSpi> list;

    public SpiFactoryBean(ApplicationContext applicationContext, Class<? extends ISenderSpi> clz) {
        this.spiClz = clz;
        Map<String, ? extends ISenderSpi> map = applicationContext.getBeansOfType(spiClz);
        list = new ArrayList<>(map.values());
        list.sort(Comparator.comparingInt(ISenderSpi::order));
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() {
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                for (ISenderSpi spi : list) {
                    if (spi.verify(args[0])) {
                        return method.invoke(spi, args);
                    }
                }
                throw new RuntimeException("No Message-Sender-Spi can execute! spiList: " + list);
            }
        };
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{spiClz},
                invocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return spiClz;
    }
}