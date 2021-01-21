package cn.ruleengine.web.message;

public interface ISenderSpi<T> {

    boolean verify(T condition);
    /**
     * 排序，数字越小，优先级越高
     * @return
     */
    default int order() {
        return 10;
    }
}
