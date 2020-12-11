package cn.ruleengine.web.service;


import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.element.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
public interface ElementService {

    /**
     * 添加元素
     *
     * @param addConditionRequest 元素信息
     * @return true
     */
    Boolean add(AddElementRequest addConditionRequest);

    /**
     * 元素code是否存在
     *
     * @param code 元素code
     * @return true存在
     */
    Boolean elementCodeIsExists(String code);

    /**
     * 元素列表
     *
     * @param pageRequest param
     * @return ListElementResponse
     */
    PageResult<ListElementResponse> list(PageRequest<ListElementRequest> pageRequest);

    /**
     * 根据id查询元素
     *
     * @param id 元素id
     * @return GetElementResponse
     */
    GetElementResponse get(Integer id);

    /**
     * 根据元素id更新元素
     *
     * @param updateElementRequest 元素信息
     * @return true
     */
    Boolean update(UpdateElementRequest updateElementRequest);

    /**
     * 根据id删除元素
     *
     * @param id 元素id
     * @return true
     */
    Boolean delete(Integer id);
}
