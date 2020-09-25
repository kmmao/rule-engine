package com.engine.web.function;

import cn.hutool.core.collection.CollUtil;
import com.engine.core.annotation.Executor;
import com.engine.core.annotation.Function;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 获取集合中第几个元素,index从0开始
 *
 * @author dingqianwen
 * @date 2020/8/29
 * @since 1.0.0
 */
@Slf4j
@Function
public class GetCollectionElementsFunction {

    @Executor
    public String executor(@Valid Params params) {
        List<String> list = params.getList();
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        if (list.size() - 1 < params.getIndex()) {
            return null;
        }
        return list.get(params.getIndex());
    }

    @Data
    public static class Params {
        private List<String> list;
        @NotNull
        private Integer index;
    }

}
