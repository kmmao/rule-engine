package cn.ruleengine.web.service.ruleset.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.RuleSetEngine;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.DataStatus;
import cn.ruleengine.web.listener.body.RuleSetMessageBody;
import cn.ruleengine.web.listener.event.RuleSetEvent;
import cn.ruleengine.web.service.RuleEngineConditionGroupService;
import cn.ruleengine.web.service.ruleset.RuleSetService;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageBase;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.condition.ConditionGroupConfig;
import cn.ruleengine.web.vo.condition.ConfigValue;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.generalrule.ListGeneralRuleRequest;
import cn.ruleengine.web.vo.ruleset.*;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.Workspace;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/14
 * @since 1.0.0
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class RuleSetServiceImpl implements RuleSetService {

    @Resource
    private RuleSetEngine ruleSetEngine;
    @Resource
    private RuleEngineRuleSetManager ruleEngineRuleSetManager;
    @Resource
    private RuleEngineRuleSetPublishManager ruleEngineRuleSetPublishManager;
    @Resource
    private ApplicationEventPublisher eventPublisher;
    @Resource
    private RuleEngineConditionGroupService ruleEngineConditionGroupService;
    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private RuleEngineRuleSetRuleManager ruleEngineRuleSetRuleManager;
    @Resource
    private RuleEngineConditionGroupManager ruleEngineConditionGroupManager;
    @Resource
    private RuleEngineConditionGroupConditionManager ruleEngineConditionGroupConditionManager;

    /**
     * 获取规则集列表
     *
     * @param pageRequest 分页查询参数
     * @return page
     */
    @Override
    public PageResult<ListRuleSetResponse> list(PageRequest<ListGeneralRuleRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        Workspace workspace = Context.getCurrentWorkspace();
        return PageUtils.page(this.ruleEngineRuleSetManager, page, () -> {
            QueryWrapper<RuleEngineRuleSet> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(RuleEngineRuleSet::getWorkspaceId, workspace.getId());
            ListGeneralRuleRequest query = pageRequest.getQuery();
            if (Validator.isNotEmpty(query.getCode())) {
                wrapper.lambda().like(RuleEngineRuleSet::getCode, query.getCode());
            }
            if (Validator.isNotEmpty(query.getName())) {
                wrapper.lambda().like(RuleEngineRuleSet::getName, query.getName());
            }
            if (Validator.isNotEmpty(query.getStatus())) {
                wrapper.lambda().eq(RuleEngineRuleSet::getStatus, query.getStatus());
            }
            PageUtils.defaultOrder(orders, wrapper);
            return wrapper;
        }, m -> {
            ListRuleSetResponse listRuleResponse = new ListRuleSetResponse();
            listRuleResponse.setId(m.getId());
            listRuleResponse.setName(m.getName());
            listRuleResponse.setCode(m.getCode());
            listRuleResponse.setIsPublish(this.ruleSetEngine.isExists(m.getWorkspaceCode(), m.getCode()));
            listRuleResponse.setCreateUserName(m.getCreateUserName());
            listRuleResponse.setStatus(m.getStatus());
            listRuleResponse.setCreateTime(m.getCreateTime());
            return listRuleResponse;
        });
    }

    /**
     * 保存或者更新规则集定义信息
     *
     * @param ruleSetDefinition 规则集定义信息
     * @return 规则集id
     */
    @Override
    public Integer saveOrUpdateRuleSetDefinition(RuleSetDefinition ruleSetDefinition) {
        // 创建规则
        RuleEngineRuleSet ruleEngineGeneralRule = new RuleEngineRuleSet();
        if (ruleSetDefinition.getId() == null) {
            if (this.ruleSetCodeIsExists(ruleSetDefinition.getCode())) {
                throw new ValidException("规则集Code：{}已经存在", ruleSetDefinition.getCode());
            }
            Workspace workspace = Context.getCurrentWorkspace();
            UserData userData = Context.getCurrentUser();
            ruleEngineGeneralRule.setCreateUserId(userData.getId());
            ruleEngineGeneralRule.setCreateUserName(userData.getUsername());
            ruleEngineGeneralRule.setWorkspaceId(workspace.getId());
            ruleEngineGeneralRule.setWorkspaceCode(workspace.getCode());
        } else {
            ruleEngineGeneralRule = this.ruleEngineRuleSetManager.lambdaQuery()
                    .eq(RuleEngineRuleSet::getId, ruleSetDefinition.getId())
                    .one();
            if (ruleEngineGeneralRule == null) {
                throw new ValidException("不存在规则集:{}", ruleSetDefinition.getId());
            } else {
                if (Objects.equals(ruleEngineGeneralRule.getStatus(), DataStatus.WAIT_PUBLISH.getStatus())) {
                    // 删除原有待发布规则
                    this.ruleEngineRuleSetPublishManager.lambdaUpdate()
                            .eq(RuleEngineRuleSetPublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                            .eq(RuleEngineRuleSetPublish::getRuleSetId, ruleEngineGeneralRule.getId())
                            .remove();
                }
            }
        }
        ruleEngineGeneralRule.setId(ruleSetDefinition.getId());
        ruleEngineGeneralRule.setName(ruleSetDefinition.getName());
        ruleEngineGeneralRule.setCode(ruleSetDefinition.getCode());
        ruleEngineGeneralRule.setDescription(ruleSetDefinition.getDescription());
        ruleEngineGeneralRule.setStatus(DataStatus.EDIT.getStatus());
        this.ruleEngineRuleSetManager.saveOrUpdate(ruleEngineGeneralRule);
        return ruleEngineGeneralRule.getId();
    }

    /**
     * 获取规则集定义信息
     *
     * @param id 规则集id
     * @return 规则集定义信息
     */
    @Override
    public RuleSetDefinition getRuleSetDefinition(Integer id) {
        RuleEngineRuleSet ruleEngineRuleSet = this.ruleEngineRuleSetManager.lambdaQuery()
                .eq(RuleEngineRuleSet::getId, id)
                .one();
        if (ruleEngineRuleSet == null) {
            return null;
        }
        return BasicConversion.INSTANCE.convert(ruleEngineRuleSet);
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
        RuleEngineRuleSet ruleEngineRuleSet = this.ruleEngineRuleSetManager.getById(updateRuleSetRequest.getId());
        if (ruleEngineRuleSet == null) {
            throw new ValidException("不存在规则集:{}", updateRuleSetRequest.getId());
        }
        // 如果之前是待发布，则删除原有待发布数据
        if (Objects.equals(ruleEngineRuleSet.getStatus(), DataStatus.WAIT_PUBLISH.getStatus())) {
            this.ruleEngineRuleSetPublishManager.lambdaUpdate()
                    .eq(RuleEngineRuleSetPublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                    .eq(RuleEngineRuleSetPublish::getRuleSetId, updateRuleSetRequest.getId())
                    .remove();
        }
        ruleEngineRuleSet.setStrategyType(updateRuleSetRequest.getStrategyType());
        ruleEngineRuleSet.setStatus(DataStatus.EDIT.getStatus());
        ruleEngineRuleSet.setEnableDefaultRule(updateRuleSetRequest.getEnableDefaultRule());
        List<RuleBody> ruleSet = updateRuleSetRequest.getRuleSet();
        // 以下代码性能可优化
        this.deleteRuleSetRule(ruleEngineRuleSet);
        // 绑定新的
        ArrayList<RuleEngineRuleSetRule> ruleEngineRuleSetRules = new ArrayList<>();
        for (RuleBody ruleBody : ruleSet) {
            Integer ruleId = this.saveRule(ruleBody);
            Integer ruleSetId = ruleEngineRuleSet.getId();
            Integer orderNo = ruleBody.getOrderNo();
            RuleEngineRuleSetRule ruleEngineRuleSetRule = new RuleEngineRuleSetRule();
            ruleEngineRuleSetRule.setRuleSetId(ruleSetId);
            ruleEngineRuleSetRule.setRuleId(ruleId);
            ruleEngineRuleSetRule.setOrderNo(orderNo);
        }
        this.ruleEngineRuleSetRuleManager.saveBatch(ruleEngineRuleSetRules);
        RuleBody defaultRule = updateRuleSetRequest.getDefaultRule();
        if (defaultRule != null) {
            Integer defaultRuleId = this.saveRule(defaultRule);
            ruleEngineRuleSet.setDefaultRuleId(defaultRuleId);
        }
        this.ruleEngineRuleSetManager.updateById(ruleEngineRuleSet);
        return true;
    }

    /**
     * 删除老的规则集规则关系
     *
     * @param ruleEngineRuleSet ruleEngineRuleSet
     */
    public void deleteRuleSetRule(RuleEngineRuleSet ruleEngineRuleSet) {
        // 删除老的规则集规则关系
        List<RuleEngineRuleSetRule> engineRuleSetRules = this.ruleEngineRuleSetRuleManager.lambdaQuery()
                .eq(RuleEngineRuleSetRule::getRuleSetId, ruleEngineRuleSet.getId())
                .list();
        if (CollUtil.isNotEmpty(engineRuleSetRules)) {
            this.ruleEngineRuleSetRuleManager.removeByIds(engineRuleSetRules.stream().map(RuleEngineRuleSetRule::getId).collect(Collectors.toList()));
            List<Integer> ruleIds = engineRuleSetRules.stream().map(RuleEngineRuleSetRule::getRuleId).collect(Collectors.toList());
            ruleIds.add(ruleEngineRuleSet.getDefaultRuleId());
            this.ruleEngineRuleManager.removeByIds(ruleIds);
            // 删除规则集条件组
            this.removeConditionGroupByRuleIds(ruleIds);
        }
    }

    /**
     * 删除规则条件组信息
     *
     * @param ruleIds 规则ids
     */
    public void removeConditionGroupByRuleIds(List<Integer> ruleIds) {
        List<RuleEngineConditionGroup> engineConditionGroups = ruleEngineConditionGroupManager.lambdaQuery()
                .in(RuleEngineConditionGroup::getRuleId, ruleIds)
                .list();
        if (CollUtil.isNotEmpty(engineConditionGroups)) {
            List<Integer> engineConditionGroupIds = engineConditionGroups.stream().map(RuleEngineConditionGroup::getId).collect(Collectors.toList());
            if (this.ruleEngineConditionGroupManager.removeByIds(engineConditionGroupIds)) {
                // 删除条件组条件
                this.ruleEngineConditionGroupConditionManager.lambdaUpdate()
                        .in(RuleEngineConditionGroupCondition::getConditionGroupId, engineConditionGroupIds)
                        .remove();
            }
        }
    }

    /**
     * 保存规则并返回规则id
     *
     * @param ruleBody 规则体
     * @return 规则id
     */
    private Integer saveRule(RuleBody ruleBody) {
        RuleEngineRule ruleEngineRule = new RuleEngineRule();
        ruleEngineRule.setName(ruleBody.getName());
        ConfigValue action = ruleBody.getAction();
        ruleEngineRule.setActionType(action.getType());
        ruleEngineRule.setActionValueType(action.getValueType());
        ruleEngineRule.setActionValue(action.getValue());
        this.ruleEngineRuleManager.save(ruleEngineRule);
        List<ConditionGroupConfig> conditionGroup = ruleBody.getConditionGroup();
        if (CollUtil.isNotEmpty(conditionGroup)) {
            this.ruleEngineConditionGroupService.saveConditionGroup(ruleEngineRule.getId(), conditionGroup);
        }
        return ruleEngineRule.getId();
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
        RuleEngineRuleSet ruleEngineRuleSet = this.ruleEngineRuleSetManager.getById(id);
        if (ruleEngineRuleSet == null) {
            return false;
        }
        // 从引擎中移除规则
        if (this.ruleSetEngine.isExists(ruleEngineRuleSet.getWorkspaceCode(), ruleEngineRuleSet.getCode())) {
            RuleSetMessageBody ruleSetMessageBody = new RuleSetMessageBody();
            ruleSetMessageBody.setType(RuleSetMessageBody.Type.REMOVE);
            ruleSetMessageBody.setWorkspaceId(ruleEngineRuleSet.getWorkspaceId());
            ruleSetMessageBody.setWorkspaceCode(ruleEngineRuleSet.getWorkspaceCode());
            ruleSetMessageBody.setRuleSetCode(ruleEngineRuleSet.getCode());
            this.eventPublisher.publishEvent(new RuleSetEvent(ruleSetMessageBody));
        }
        // 删除规则集规则
        this.deleteRuleSetRule(ruleEngineRuleSet);
        // 删除规则发布记录
        this.ruleEngineRuleSetPublishManager.lambdaUpdate().eq(RuleEngineRuleSetPublish::getRuleSetId, id).remove();
        // 删除规则
        return this.ruleEngineRuleSetManager.removeById(id);
    }

    /**
     * 规则集Code是否存在
     *
     * @param code 规则集code
     * @return true存在
     */
    @Override
    public Boolean ruleSetCodeIsExists(String code) {
        Workspace workspace = Context.getCurrentWorkspace();
        Integer count = this.ruleEngineRuleSetManager.lambdaQuery()
                .eq(RuleEngineRuleSet::getWorkspaceId, workspace.getId())
                .eq(RuleEngineRuleSet::getCode, code)
                .count();
        return count != null && count >= 1;
    }

}
