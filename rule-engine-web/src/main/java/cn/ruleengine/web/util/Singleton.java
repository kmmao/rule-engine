package cn.ruleengine.web.util;

import java.util.function.Supplier;

/**
 * 〈一句话功能简述〉<br>
 * 〈单例工具〉
 *
 * @author v-dingqianwen.ea
 * @create 2019/8/11
 * @since 1.0.0
 */
public final class Singleton<R> implements Supplier<R> {

    private boolean initialized = false;

    private volatile Supplier<R> instanceSupplier;

    public Singleton(final Supplier<R> original) {
        synchronized (original) {
            this.instanceSupplier = () -> {
                if (!initialized) {
                    final R singletonInstance = original.get();
                    instanceSupplier = () -> singletonInstance;
                    initialized = true;
                }
                return instanceSupplier.get();
            };
        }
    }

    @Override
    public R get() {
        return instanceSupplier.get();
    }

}
