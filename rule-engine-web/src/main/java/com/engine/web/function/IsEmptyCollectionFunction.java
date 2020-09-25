package com.engine.web.function;

import cn.hutool.core.collection.CollUtil;
import com.engine.core.annotation.FunctionCacheable;
import com.engine.core.annotation.Executor;
import com.engine.core.annotation.FailureStrategy;
import com.engine.core.annotation.Function;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/19
 * @since 1.0.0
 */
@Slf4j
@Function
public class IsEmptyCollectionFunction {

    @Executor
    public Boolean executor(List<String> list) {
        return CollUtil.isEmpty(list);
    }

    @FailureStrategy
    public Boolean failureStrategy() {
        return true;
    }
}
