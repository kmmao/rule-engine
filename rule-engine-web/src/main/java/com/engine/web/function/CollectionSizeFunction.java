package com.engine.web.function;

import com.engine.core.annotation.Executor;
import com.engine.core.annotation.FailureStrategy;
import com.engine.core.annotation.Function;
import com.engine.core.annotation.Param;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 函数说明：求集合大小
 * 函数参数：
 * <blockquote>
 * <pre>
 *    List<String>  list
 * </pre>
 * </blockquote>
 * 函数返回值：{@link Integer}
 *
 * @author dingqianwen
 * @date 2020/7/19
 * @since 1.0.0
 */
@Slf4j
@Function
public class CollectionSizeFunction {

    @Executor
    public Integer executor(@Param("list") List<String> list) {
        return list.size();
    }

    @FailureStrategy
    public Integer failureStrategy() {
        return 0;
    }
}
