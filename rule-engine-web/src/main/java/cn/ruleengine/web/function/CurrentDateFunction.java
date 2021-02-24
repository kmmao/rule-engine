package cn.ruleengine.web.function;

import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;

import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/11/19
 * @since 1.0.0
 */
@Function
public class CurrentDateFunction {

    @Executor
    public Date executor() {
        return new Date();
    }

}
