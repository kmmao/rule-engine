package com.engine.web.service;

import com.engine.web.vo.base.request.PageRequest;
import com.engine.web.vo.base.response.PageResult;
import com.engine.web.vo.function.*;
import lombok.SneakyThrows;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/27
 * @since 1.0.0
 */
public interface FunctionService {

    /**
     * 函数列表
     *
     * @param pageRequest param
     * @return list
     */
    PageResult<ListFunctionResponse> list(PageRequest<ListFunctionRequest> pageRequest);

    /**
     * 查询函数详情
     *
     * @param id 函数id
     * @return 函数信息
     */
    GetFunctionResponse get(Integer id);

    /**
     * 函数模拟测试
     *
     * @param runFunction 函数入参值
     * @return result
     */
    Object run(RunFunction runFunction);
}
