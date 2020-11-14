package com.engine.web.service;


import com.engine.web.vo.base.response.PageResult;
import com.engine.web.vo.variable.*;
import com.engine.web.vo.base.request.PageRequest;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
public interface VariableService {
    Boolean add(AddVariableRequest addConditionRequest);

    /**
     * 变量名称是否存在
     *
     * @param name 变量名称
     * @return true存在
     */
    Boolean varNameIsExists(String name);

    PageResult<ListVariableResponse> list(PageRequest<ListVariableRequest> pageRequest);

    GetVariableResponse get(Integer id);

    Boolean update(UpdateVariableRequest updateVariableRequest);

    Boolean delete(Integer id);
}
