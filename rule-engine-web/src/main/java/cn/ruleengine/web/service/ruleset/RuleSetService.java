package cn.ruleengine.web.service.ruleset;

import cn.ruleengine.web.vo.base.PageRequest;
import cn.ruleengine.web.vo.base.PageResult;
import cn.ruleengine.web.vo.generalrule.ListGeneralRuleRequest;
import cn.ruleengine.web.vo.ruleset.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/14
 * @since 1.0.0
 */
public interface RuleSetService {

    /**
     * 获取规则集列表
     *
     * @param pageRequest 分页查询参数
     * @return page
     */
    PageResult<ListRuleSetResponse> list(PageRequest<ListGeneralRuleRequest> pageRequest);

    /**
     * 保存或者更新规则集定义信息
     *
     * @param ruleSetDefinition 规则集定义信息
     * @return 规则集id
     */
    Integer saveOrUpdateRuleSetDefinition(RuleSetDefinition ruleSetDefinition);

    /**
     * 获取规则集定义信息
     *
     * @param id 规则集id
     * @return 规则集定义信息
     */
    RuleSetDefinition getRuleSetDefinition(Integer id);

    /**
     * 生成待发布版本，更新规则数据
     *
     * @param ruleSetBody 规则集配置数据
     * @return true
     */
    Boolean generationRelease(RuleSetBody ruleSetBody);

    /**
     * 规则集发布
     *
     * @param id 规则集id
     * @return true
     */
    Boolean publish(Integer id);

    /**
     * 更新规则集信息
     *
     * @param ruleSetBody 规则配置数据
     * @return true执行成功
     */
    Boolean updateRuleSet(RuleSetBody ruleSetBody);

    /**
     * 获取规则集信息
     *
     * @param id 规则集id
     * @return 规则集信息
     */
    GetRuleSetResponse getRuleSetConfig(Integer id);

    /**
     * 规则集预览
     *
     * @param id 规则集id
     * @return GetRuleResponse
     */
    ViewRuleSetResponse getViewRuleSet(Integer id);

    /**
     * 获取预览已发布的规则集
     *
     * @param id 规则集id
     * @return ViewRuleSetResponse
     */
    ViewRuleSetResponse getPublishRuleSet(Integer id);

    /**
     * 删除规则集
     *
     * @param id 规则集id
     * @return true
     */
    Boolean delete(Integer id);

    /**
     * 规则集Code是否存在
     *
     * @param param 规则集code
     * @return true存在
     */
    Boolean ruleSetCodeIsExists(String param);
}
