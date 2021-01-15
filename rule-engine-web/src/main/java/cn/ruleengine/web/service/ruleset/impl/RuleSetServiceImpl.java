package cn.ruleengine.web.service.ruleset.impl;

import cn.ruleengine.web.service.ruleset.RuleSetService;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.generalrule.ListGeneralRuleRequest;
import cn.ruleengine.web.vo.ruleset.*;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/14
 * @since 1.0.0
 */
@Service
public class RuleSetServiceImpl implements RuleSetService {

    /**
     * 获取规则集列表
     *
     * @param pageRequest 分页查询参数
     * @return page
     */
    @Override
    public PageResult<ListRuleSetResponse> list(PageRequest<ListGeneralRuleRequest> pageRequest) {
        return null;
    }

    /**
     * 保存或者更新规则集定义信息
     *
     * @param ruleSetDefinition 规则集定义信息
     * @return 规则集id
     */
    @Override
    public Integer saveOrUpdateRuleSetDefinition(RuleSetDefinition ruleSetDefinition) {
        return null;
    }

    /**
     * 获取规则集定义信息
     *
     * @param id 规则集id
     * @return 规则集定义信息
     */
    @Override
    public RuleSetDefinition getRuleSetDefinition(Integer id) {
        return null;
    }

    /**
     * 生成待发布版本，更新规则数据
     *
     * @param releaseRequest 规则集配置数据
     * @return true
     */
    @Override
    public Boolean generationRelease(GenerationReleaseRequest releaseRequest) {
        return null;
    }

    /**
     * 规则集发布
     *
     * @param id 规则集id
     * @return true
     */
    @Override
    public Boolean publish(Integer id) {
        return null;
    }

    /**
     * 更新规则集信息
     *
     * @param updateRuleSetRequest 规则配置数据
     * @return true执行成功
     */
    @Override
    public Boolean updateRuleSet(UpdateRuleSetRequest updateRuleSetRequest) {
        return null;
    }

    /**
     * 获取规则集信息
     *
     * @param id 规则集id
     * @return 规则集信息
     */
    @Override
    public GetRuleSetResponse getRuleSetConfig(Integer id) {
        return null;
    }

    /**
     * 规则集预览
     *
     * @param id 规则集id
     * @return GetRuleResponse
     */
    @Override
    public ViewRuleSetResponse getViewRuleSet(Integer id) {
        return null;
    }

    /**
     * 获取预览已发布的规则集
     *
     * @param id 规则集id
     * @return ViewRuleSetResponse
     */
    @Override
    public ViewRuleSetResponse getPublishRuleSet(Integer id) {
        return null;
    }

    /**
     * 删除规则集
     *
     * @param id 规则集id
     * @return true
     */
    @Override
    public Boolean delete(Integer id) {
        return null;
    }

    /**
     * 规则集Code是否存在
     *
     * @param param 规则集code
     * @return true存在
     */
    @Override
    public Boolean ruleSetCodeIsExists(String param) {
        return null;
    }
}
