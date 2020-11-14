package com.engine.web.service;


import com.engine.web.vo.base.response.PageResult;
import com.engine.web.vo.element.*;
import com.engine.web.vo.base.request.PageRequest;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
public interface ElementService {
    Boolean add(AddElementRequest addConditionRequest);

    /**
     * 元素code是否存在
     *
     * @param code 元素code
     * @return true存在
     */
    Boolean elementCodeIsExists(String code);

    PageResult<ListElementResponse> list(PageRequest<ListElementRequest> pageRequest);

    GetElementResponse get(Integer id);

    Boolean update(UpdateElementRequest updateElementRequest);

    Boolean delete(Integer id);
}
