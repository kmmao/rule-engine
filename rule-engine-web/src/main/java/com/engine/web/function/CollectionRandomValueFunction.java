package com.engine.web.function;

import com.engine.core.annotation.Executor;
import com.engine.core.annotation.Function;
import com.engine.core.annotation.Param;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 获取集合随机值
 *
 * @author dingqianwen
 * @date 2020/9/8
 * @since 1.0.0
 */
@Slf4j
@Function
public class CollectionRandomValueFunction {

    private ThreadLocalRandom randomUtil = ThreadLocalRandom.current();

    @Executor
    public String executor(@Param(value = "list") List<String> list) {
        int index = randomUtil.nextInt(list.size());
        return list.get(index);
    }

}
