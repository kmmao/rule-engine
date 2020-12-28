package cn.ruleengine.web.service.simplerule;

import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.simplerule.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/24
 * @since 1.0.0
 */
public interface SimpleRuleService {

    /**
     * 规则列表
     *
     * @param pageRequest 分页查询数据
     * @return page
     */
    PageResult<ListSimpleRuleResponse> list(PageRequest<ListSimpleRuleRequest> pageRequest);

    /**
     * 获取规则信息
     *
     * @param id 规则id
     * @return 规则信息
     */
    GetSimpleRuleResponse getRuleConfig(Integer id);

    /**
     * 规则code是否存在
     *
     * @param code 规则code
     * @return true存在
     */
    Boolean ruleCodeIsExists(String code);

    /**
     * 更新规则信息
     *
     * @param updateRuleRequest 规则配置数据
     * @return true执行成功
     */
    Boolean updateRule(UpdateSimpleRuleRequest updateRuleRequest);

    /**
     * 删除规则
     *
     * @param id 规则id
     * @return true
     */
    Boolean delete(Integer id);

    /**
     * 保存或者更新规则定义信息
     *
     * @param ruleDefinition 规则定义信息
     * @return 规则id
     */
    Integer saveOrUpdateRuleDefinition(SimpleRuleDefinition ruleDefinition);

    /**
     * 获取规则定义信息
     *
     * @param id 规则id
     * @return 规则定义信息
     */
    SimpleRuleDefinition getRuleDefinition(Integer id);

    /**
     * 生成待发布版本，更新规则数据
     *
     * @param generationReleaseRequest 规则配置数据
     * @return true
     */
    Boolean generationRelease(GenerationReleaseRequest generationReleaseRequest);

    /**
     * 规则发布
     *
     * @param id 规则id
     * @return true
     */
    Boolean publish(Integer id);

    /**
     * 获取预览已发布的规则
     *
     * @param id 规则id
     * @return GetRuleResponse
     */
    ViewSimpleRuleResponse getPublishRule(Integer id);

    /**
     * 规则预览
     *
     * @param id 规则id
     * @return GetRuleResponse
     */
    ViewSimpleRuleResponse getViewRule(Integer id);
}
