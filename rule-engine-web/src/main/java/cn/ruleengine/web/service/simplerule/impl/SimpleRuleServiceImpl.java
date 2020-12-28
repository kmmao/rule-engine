package cn.ruleengine.web.service.simplerule.impl;


import cn.ruleengine.core.rule.AbnormalAlarm;
import cn.ruleengine.core.rule.SimpleRule;
import cn.ruleengine.core.value.*;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.EnableEnum;
import cn.ruleengine.web.enums.DataStatus;
import cn.ruleengine.web.listener.body.RuleMessageBody;
import cn.ruleengine.web.listener.event.SimpleRuleEvent;
import cn.ruleengine.web.service.ConditionService;
import cn.ruleengine.web.service.impl.RuleParameterService;
import cn.ruleengine.web.service.simplerule.RuleResolveService;
import cn.ruleengine.web.service.simplerule.SimpleRuleService;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import cn.ruleengine.web.store.mapper.RuleEngineRuleMapper;
import cn.ruleengine.web.store.mapper.RuleEngineSimpleRuleMapper;

import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageBase;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.condition.ConditionGroupCondition;
import cn.ruleengine.web.vo.condition.ConditionGroupConfig;
import cn.ruleengine.web.vo.condition.ConditionResponse;
import cn.ruleengine.web.vo.condition.ConfigBean;
import cn.ruleengine.web.vo.simplerule.*;
import cn.ruleengine.core.condition.ConditionGroup;

import cn.ruleengine.core.condition.Condition;
import cn.ruleengine.web.vo.simplerule.DefaultAction;
import cn.ruleengine.web.vo.simplerule.Action;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.ruleengine.core.Engine;
import cn.ruleengine.core.exception.ValidException;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.Workspace;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/24
 * @since 1.0.0
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class SimpleRuleServiceImpl implements SimpleRuleService {

    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private RuleEngineSimpleRuleManager ruleEngineSimpleRuleManager;
    @Resource
    private RuleEngineSimpleRuleMapper ruleEngineSimpleRuleMapper;
    @Resource
    private RuleEngineRuleMapper ruleEngineRuleMapper;
    @Resource
    private RuleEngineConditionGroupConditionManager ruleEngineConditionGroupConditionManager;
    @Resource
    private RuleEngineConditionGroupManager ruleEngineConditionGroupManager;
    @Resource
    private Engine engine;
    @Resource
    private ConditionService conditionService;
    @Resource
    private RuleParameterService ruleCountInfoService;
    @Resource
    private RuleEngineSimpleRulePublishManager ruleEngineSimpleRulePublishManager;
    @Resource
    private RuleResolveService ruleResolveService;
    @Resource
    private RuleEngineConditionManager ruleEngineConditionManager;
    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineElementManager ruleEngineElementManager;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 规则列表
     *
     * @param pageRequest 分页查询数据
     * @return page
     */
    @Override
    public PageResult<ListSimpleRuleResponse> list(PageRequest<ListSimpleRuleRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        Workspace workspace = Context.getCurrentWorkspace();
        return PageUtils.page(this.ruleEngineSimpleRuleManager, page, () -> {
            QueryWrapper<RuleEngineSimpleRule> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(RuleEngineSimpleRule::getWorkspaceId, workspace.getId());
            PageUtils.defaultOrder(orders, wrapper);

            ListSimpleRuleRequest query = pageRequest.getQuery();
            if (Validator.isNotEmpty(query.getName())) {
                wrapper.lambda().like(RuleEngineSimpleRule::getName, query.getName());
            }
            if (Validator.isNotEmpty(query.getCode())) {
                wrapper.lambda().like(RuleEngineSimpleRule::getCode, query.getCode());
            }
            if (Validator.isNotEmpty(query.getStatus())) {
                wrapper.lambda().eq(RuleEngineSimpleRule::getStatus, query.getStatus());
            }
            return wrapper;
        }, m -> {
            ListSimpleRuleResponse listRuleResponse = new ListSimpleRuleResponse();
            listRuleResponse.setId(m.getId());
            listRuleResponse.setName(m.getName());
            listRuleResponse.setCode(m.getCode());
            listRuleResponse.setIsPublish(this.engine.isExists(m.getWorkspaceCode(), m.getCode()));
            listRuleResponse.setCreateUserName(m.getCreateUserName());
            listRuleResponse.setStatus(m.getStatus());
            listRuleResponse.setCreateTime(m.getCreateTime());
            return listRuleResponse;
        });
    }

    /**
     * 规则code是否存在
     *
     * @param code 规则code
     * @return true存在
     */
    @Override
    public Boolean ruleCodeIsExists(String code) {
        Workspace workspace = Context.getCurrentWorkspace();
        Integer count = this.ruleEngineSimpleRuleManager.lambdaQuery()
                .eq(RuleEngineSimpleRule::getWorkspaceId, workspace.getId())
                .eq(RuleEngineSimpleRule::getCode, code)
                .count();
        return count != null && count >= 1;
    }

    /**
     * 更新规则信息
     *
     * @param updateRuleRequest 规则配置数据
     * @return true执行成功
     */
    @Override
    public Boolean updateRule(UpdateSimpleRuleRequest updateRuleRequest) {
        RuleEngineSimpleRule ruleEngineSimpleRule = this.ruleEngineSimpleRuleManager.getById(updateRuleRequest.getId());
        if (ruleEngineSimpleRule == null) {
            throw new ValidException("不存在规则:{}", updateRuleRequest.getId());
        }
        // 如果之前是待发布，则删除原有待发布数据
        if (Objects.equals(ruleEngineSimpleRule.getStatus(), DataStatus.WAIT_PUBLISH.getStatus())) {
            this.ruleEngineSimpleRulePublishManager.lambdaUpdate()
                    .eq(RuleEngineSimpleRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                    .eq(RuleEngineSimpleRulePublish::getSimpleRuleId, updateRuleRequest.getId())
                    .remove();
        }
        // 如果原来有条件信息，先删除原有信息
        this.removeConditionGroupByRuleId(ruleEngineSimpleRule.getRuleId());
        // 保存条件信息
        this.saveConditionGroup(ruleEngineSimpleRule.getRuleId(), updateRuleRequest.getConditionGroup());
        //  更新规则信息
        ruleEngineSimpleRule.setId(updateRuleRequest.getId());
        ruleEngineSimpleRule.setStatus(DataStatus.EDIT.getStatus());
        // 保存规则结果
        Action action = updateRuleRequest.getAction();
        RuleEngineRule ruleEngineRule = new RuleEngineRule();
        ruleEngineRule.setId(ruleEngineSimpleRule.getRuleId());
        ruleEngineRule.setActionType(action.getType());
        ruleEngineRule.setActionValueType(action.getValueType());
        ruleEngineRule.setActionValue(action.getValue());
        ruleEngineRuleMapper.updateRuleById(ruleEngineRule);
        // 保存默认结果
        DefaultAction defaultAction = updateRuleRequest.getDefaultAction();
        ruleEngineSimpleRule.setEnableDefaultAction(defaultAction.getEnableDefaultAction());
        ruleEngineSimpleRule.setDefaultActionValue(defaultAction.getValue());
        ruleEngineSimpleRule.setDefaultActionValueType(defaultAction.getValueType());
        ruleEngineSimpleRule.setDefaultActionType(defaultAction.getType());
        ruleEngineSimpleRule.setAbnormalAlarm(JSONObject.toJSONString(updateRuleRequest.getAbnormalAlarm()));
        this.ruleEngineSimpleRuleMapper.updateRuleById(ruleEngineSimpleRule);
        return true;
    }

    /**
     * 保存条件组
     *
     * @param ruleId         规则id
     * @param conditionGroup 条件组信息
     */
    private void saveConditionGroup(Integer ruleId, List<ConditionGroupConfig> conditionGroup) {
        if (CollUtil.isEmpty(conditionGroup)) {
            return;
        }
        List<RuleEngineConditionGroupCondition> ruleEngineConditionGroupConditions = new LinkedList<>();
        for (ConditionGroupConfig groupConfig : conditionGroup) {
            RuleEngineConditionGroup engineConditionGroup = new RuleEngineConditionGroup();
            engineConditionGroup.setName(groupConfig.getName());
            engineConditionGroup.setRuleId(ruleId);
            engineConditionGroup.setOrderNo(groupConfig.getOrderNo());
            this.ruleEngineConditionGroupManager.save(engineConditionGroup);
            List<ConditionGroupCondition> conditionGroupConditions = groupConfig.getConditionGroupCondition();
            if (CollUtil.isNotEmpty(conditionGroupConditions)) {
                for (ConditionGroupCondition conditionGroupCondition : conditionGroupConditions) {
                    RuleEngineConditionGroupCondition ruleEngineConditionGroupCondition = new RuleEngineConditionGroupCondition();
                    ruleEngineConditionGroupCondition.setConditionId(conditionGroupCondition.getCondition().getId());
                    ruleEngineConditionGroupCondition.setConditionGroupId(engineConditionGroup.getId());
                    ruleEngineConditionGroupCondition.setOrderNo(conditionGroupCondition.getOrderNo());
                    ruleEngineConditionGroupConditions.add(ruleEngineConditionGroupCondition);
                }
            }
        }
        if (CollUtil.isNotEmpty(ruleEngineConditionGroupConditions)) {
            this.ruleEngineConditionGroupConditionManager.saveBatch(ruleEngineConditionGroupConditions);
        }
    }

    /**
     * 删除规则
     *
     * @param id 规则id
     * @return true
     */
    @Override
    public Boolean delete(Integer id) {
        RuleEngineSimpleRule ruleEngineSimpleRule = this.ruleEngineSimpleRuleManager.getById(id);
        if (ruleEngineSimpleRule == null) {
            return false;
        }
        // 从引擎中移除规则
        if (this.engine.isExists(ruleEngineSimpleRule.getWorkspaceCode(), ruleEngineSimpleRule.getCode())) {
            RuleMessageBody ruleMessageBody = new RuleMessageBody();
            ruleMessageBody.setType(RuleMessageBody.Type.REMOVE);
            ruleMessageBody.setWorkspaceId(ruleEngineSimpleRule.getWorkspaceId());
            ruleMessageBody.setWorkspaceCode(ruleEngineSimpleRule.getWorkspaceCode());
            ruleMessageBody.setRuleCode(ruleEngineSimpleRule.getCode());
            this.eventPublisher.publishEvent(new SimpleRuleEvent(ruleMessageBody));
        }
        this.ruleEngineRuleManager.removeById(ruleEngineSimpleRule.getRuleId());
        // 删除规则发布记录
        this.ruleEngineSimpleRulePublishManager.lambdaUpdate().eq(RuleEngineSimpleRulePublish::getSimpleRuleId, id).remove();
        // 删除规则条件组信息
        this.removeConditionGroupByRuleId(ruleEngineSimpleRule.getRuleId());
        // 删除规则
        return this.ruleEngineSimpleRuleManager.removeById(id);
    }

    /**
     * 删除规则条件组信息
     *
     * @param ruleId 规则id
     */
    public void removeConditionGroupByRuleId(Integer ruleId) {
        List<RuleEngineConditionGroup> engineConditionGroups = ruleEngineConditionGroupManager.lambdaQuery()
                .eq(RuleEngineConditionGroup::getRuleId, ruleId)
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
     * 保存或者更新规则定义信息
     *
     * @param ruleDefinition 规则定义信息
     * @return 规则id
     */
    @Override
    public Integer saveOrUpdateRuleDefinition(SimpleRuleDefinition ruleDefinition) {
        // 创建规则
        RuleEngineSimpleRule ruleEngineSimpleRule = new RuleEngineSimpleRule();
        if (ruleDefinition.getId() == null) {
            if (this.ruleCodeIsExists(ruleDefinition.getCode())) {
                throw new ValidException("规则Code：{}已经存在", ruleDefinition.getCode());
            }
            Workspace workspace = Context.getCurrentWorkspace();
            UserData userData = Context.getCurrentUser();
            ruleEngineSimpleRule.setCreateUserId(userData.getId());
            ruleEngineSimpleRule.setCreateUserName(userData.getUsername());
            ruleEngineSimpleRule.setWorkspaceId(workspace.getId());
            ruleEngineSimpleRule.setWorkspaceCode(workspace.getCode());
            RuleEngineRule ruleEngineRule = new RuleEngineRule();
            ruleEngineRule.setName(ruleDefinition.getName());
            ruleEngineRule.setCode(ruleDefinition.getCode());
            ruleEngineRule.setDescription(ruleDefinition.getDescription());
            ruleEngineRule.setCreateUserId(userData.getId());
            ruleEngineRule.setCreateUserName(userData.getUsername());
            this.ruleEngineRuleManager.save(ruleEngineRule);
            ruleEngineSimpleRule.setRuleId(ruleEngineRule.getId());
        } else {
            Integer count = this.ruleEngineRuleManager.lambdaQuery()
                    .eq(RuleEngineRule::getId, ruleDefinition.getId())
                    .count();
            if (count == null || count == 0) {
                throw new ValidException("不存在规则:{}", ruleDefinition.getId());
            }
        }
        ruleEngineSimpleRule.setId(ruleDefinition.getId());
        ruleEngineSimpleRule.setName(ruleDefinition.getName());
        ruleEngineSimpleRule.setCode(ruleDefinition.getCode());
        ruleEngineSimpleRule.setDescription(ruleDefinition.getDescription());
        ruleEngineSimpleRule.setStatus(DataStatus.EDIT.getStatus());
        this.ruleEngineSimpleRuleManager.saveOrUpdate(ruleEngineSimpleRule);
        return ruleEngineSimpleRule.getId();
    }

    /**
     * 获取规则定义信息
     *
     * @param id 规则id
     * @return 规则定义信息
     */
    @Override
    public SimpleRuleDefinition getRuleDefinition(Integer id) {
        RuleEngineRule ruleEngineRule = this.ruleEngineRuleManager.lambdaQuery()
                .eq(RuleEngineRule::getId, id)
                .one();
        if (ruleEngineRule == null) {
            return null;
        }
        return BasicConversion.INSTANCE.convert(ruleEngineRule);
    }

    /**
     * 生成待发布版本，更新规则数据
     *
     * @param releaseRequest 规则配置数据
     * @return true
     */
    @Override
    public Boolean generationRelease(GenerationReleaseRequest releaseRequest) {
        // 更新规则
        RuleEngineSimpleRule ruleEngineSimpleRule = this.ruleEngineSimpleRuleManager.getById(releaseRequest.getId());
        if (ruleEngineSimpleRule == null) {
            throw new ValidException("不存在规则:{}", releaseRequest.getId());
        }
        Integer originStatus = ruleEngineSimpleRule.getStatus();
        // 如果开启了默认结果
        DefaultAction defaultAction = releaseRequest.getDefaultAction();
        if (EnableEnum.ENABLE.getStatus().equals(defaultAction.getEnableDefaultAction())) {
            if (Validator.isEmpty(defaultAction.getType())) {
                throw new ValidException("默认结果类型不能为空");
            }
            if (Validator.isEmpty(defaultAction.getValueType())) {
                throw new ValidException("默认结果值类型不能为空");
            }
            if (Validator.isEmpty(defaultAction.getValue())) {
                throw new ValidException("默认结果值不能为空");
            }
        }
        // 如果原来有条件信息，先删除原有信息
        this.removeConditionGroupByRuleId(ruleEngineSimpleRule.getRuleId());
        // 保存条件信息
        this.saveConditionGroup(ruleEngineSimpleRule.getRuleId(), releaseRequest.getConditionGroup());
        //  更新规则信息
        ruleEngineSimpleRule.setId(releaseRequest.getId());
        ruleEngineSimpleRule.setStatus(DataStatus.WAIT_PUBLISH.getStatus());
        // 保存结果
        Action action = releaseRequest.getAction();
        RuleEngineRule ruleEngineRule = new RuleEngineRule();
        ruleEngineRule.setActionValue(action.getValue());
        ruleEngineRule.setId(ruleEngineSimpleRule.getRuleId());
        ruleEngineRule.setActionType(action.getType());
        ruleEngineRule.setActionValueType(action.getValueType());
        ruleEngineRuleMapper.updateRuleById(ruleEngineRule);
        // 保存默认结果
        ruleEngineSimpleRule.setEnableDefaultAction(defaultAction.getEnableDefaultAction());
        ruleEngineSimpleRule.setDefaultActionValue(defaultAction.getValue());
        ruleEngineSimpleRule.setDefaultActionValueType(defaultAction.getValueType());
        ruleEngineSimpleRule.setDefaultActionType(defaultAction.getType());
        ruleEngineSimpleRule.setAbnormalAlarm(JSONObject.toJSONString(releaseRequest.getAbnormalAlarm()));
        this.ruleEngineSimpleRuleMapper.updateRuleById(ruleEngineSimpleRule);
        // 生成待发布规则
        if (Objects.equals(originStatus, DataStatus.WAIT_PUBLISH.getStatus())) {
            // 删除原有待发布规则
            this.ruleEngineSimpleRulePublishManager.lambdaUpdate()
                    .eq(RuleEngineSimpleRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                    .eq(RuleEngineSimpleRulePublish::getSimpleRuleId, ruleEngineSimpleRule.getId())
                    .remove();
        }
        // 添加新的待发布数据
        SimpleRule rule = this.ruleResolveService.ruleProcess(ruleEngineSimpleRule);
        RuleEngineSimpleRulePublish rulePublish = new RuleEngineSimpleRulePublish();
        rulePublish.setSimpleRuleId(rule.getId());
        rulePublish.setSimpleRuleCode(rule.getCode());
        rulePublish.setData(rule.toJson());
        rulePublish.setStatus(DataStatus.WAIT_PUBLISH.getStatus());
        rulePublish.setWorkspaceId(rule.getWorkspaceId());
        rulePublish.setWorkspaceCode(rule.getWorkspaceCode());
        this.ruleEngineSimpleRulePublishManager.save(rulePublish);
        return true;
    }


    /**
     * 规则发布
     *
     * @param id 规则id
     * @return true
     */
    @Override
    public Boolean publish(Integer id) {
        RuleEngineSimpleRule ruleEngineSimpleRule = this.ruleEngineSimpleRuleManager.getById(id);
        if (ruleEngineSimpleRule == null) {
            throw new ValidException("不存在规则:{}", id);
        }
        if (ruleEngineSimpleRule.getStatus().equals(DataStatus.EDIT.getStatus())) {
            throw new ValidException("该规则不可执行:{}", id);
        }
        // 如果已经是发布规则了
        if (ruleEngineSimpleRule.getStatus().equals(DataStatus.PUBLISHED.getStatus())) {
            return true;
        }
        // 修改为已发布
        this.ruleEngineSimpleRuleManager.lambdaUpdate()
                .set(RuleEngineSimpleRule::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineSimpleRule::getId, ruleEngineSimpleRule.getId())
                .update();
        // 删除原有的已发布规则数据
        this.ruleEngineSimpleRulePublishManager.lambdaUpdate()
                .eq(RuleEngineSimpleRulePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineSimpleRulePublish::getSimpleRuleId, ruleEngineSimpleRule.getId())
                .remove();
        // 更新待发布为已发布
        this.ruleEngineSimpleRulePublishManager.lambdaUpdate()
                .set(RuleEngineSimpleRulePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineSimpleRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                .eq(RuleEngineSimpleRulePublish::getSimpleRuleId, ruleEngineSimpleRule.getId())
                .update();
        // 加载规则
        RuleMessageBody ruleMessageBody = new RuleMessageBody();
        ruleMessageBody.setType(RuleMessageBody.Type.LOAD);
        ruleMessageBody.setRuleCode(ruleEngineSimpleRule.getCode());
        ruleMessageBody.setWorkspaceId(ruleEngineSimpleRule.getWorkspaceId());
        ruleMessageBody.setWorkspaceCode(ruleEngineSimpleRule.getWorkspaceCode());
        this.eventPublisher.publishEvent(new SimpleRuleEvent(ruleMessageBody));
        return true;
    }

    /**
     * 获取规则信息
     *
     * @param id 规则id
     * @return 规则信息
     */
    @Override
    public GetSimpleRuleResponse getRuleConfig(Integer id) {
        RuleEngineSimpleRule ruleEngineSimpleRule = this.ruleEngineSimpleRuleManager.getById(id);
        if (ruleEngineSimpleRule == null) {
            return null;
        }
        GetSimpleRuleResponse ruleResponse = new GetSimpleRuleResponse();
        ruleResponse.setId(ruleEngineSimpleRule.getId());
        ruleResponse.setCode(ruleEngineSimpleRule.getCode());
        ruleResponse.setName(ruleEngineSimpleRule.getName());
        ruleResponse.setDescription(ruleEngineSimpleRule.getDescription());
        List<RuleEngineConditionGroup> engineConditionGroups = this.ruleEngineConditionGroupManager.lambdaQuery()
                .eq(RuleEngineConditionGroup::getRuleId, ruleEngineSimpleRule.getRuleId())
                .orderByAsc(RuleEngineConditionGroup::getOrderNo)
                .list();
        if (CollUtil.isNotEmpty(engineConditionGroups)) {
            // 加载所有的用到的条件组条件
            Set<Integer> conditionGroupIds = engineConditionGroups.stream().map(RuleEngineConditionGroup::getId).collect(Collectors.toSet());
            List<RuleEngineConditionGroupCondition> ruleEngineConditionGroupConditions = this.ruleEngineConditionGroupConditionManager.lambdaQuery()
                    .in(RuleEngineConditionGroupCondition::getConditionGroupId, conditionGroupIds)
                    .orderByAsc(RuleEngineConditionGroupCondition::getOrderNo)
                    .list();
            if (CollUtil.isNotEmpty(ruleEngineConditionGroupConditions)) {
                Map<Integer, List<RuleEngineConditionGroupCondition>> conditionGroupConditionMaps = ruleEngineConditionGroupConditions.stream()
                        .collect(Collectors.groupingBy(RuleEngineConditionGroupCondition::getConditionGroupId));
                Set<Integer> conditionIds = conditionGroupConditionMaps.values().stream().flatMap(Collection::stream).map(RuleEngineConditionGroupCondition::getConditionId)
                        .collect(Collectors.toSet());
                List<RuleEngineCondition> ruleEngineConditions = this.ruleEngineConditionManager.lambdaQuery().in(RuleEngineCondition::getId, conditionIds).list();
                if (CollUtil.isNotEmpty(ruleEngineConditions)) {
                    Map<Integer, RuleEngineCondition> conditionMap = ruleEngineConditions.stream().collect(Collectors.toMap(RuleEngineCondition::getId, Function.identity()));
                    Map<Integer, RuleEngineElement> elementMap = this.conditionService.getConditionElementMap(conditionMap.values());
                    Map<Integer, RuleEngineVariable> variableMap = this.conditionService.getConditionVariableMap(conditionMap.values());
                    // 转换条件组数据
                    List<ConditionGroupConfig> conditionGroup = new ArrayList<>();
                    for (RuleEngineConditionGroup engineConditionGroup : engineConditionGroups) {
                        ConditionGroupConfig group = new ConditionGroupConfig();
                        group.setId(engineConditionGroup.getId());
                        group.setName(engineConditionGroup.getName());
                        group.setOrderNo(engineConditionGroup.getOrderNo());
                        List<RuleEngineConditionGroupCondition> conditionGroupConditions = conditionGroupConditionMaps.get(engineConditionGroup.getId());
                        if (CollUtil.isEmpty(conditionGroupConditions)) {
                            continue;
                        }
                        List<ConditionGroupCondition> groupConditions = new ArrayList<>(conditionGroupConditions.size());
                        for (RuleEngineConditionGroupCondition conditionGroupCondition : conditionGroupConditions) {
                            ConditionGroupCondition conditionSet = new ConditionGroupCondition();
                            conditionSet.setId(conditionGroupCondition.getId());
                            conditionSet.setOrderNo(conditionGroupCondition.getOrderNo());
                            RuleEngineCondition engineCondition = conditionMap.get(conditionGroupCondition.getConditionId());
                            conditionSet.setCondition(this.conditionService.getConditionResponse(engineCondition, variableMap, elementMap));
                            groupConditions.add(conditionSet);
                        }
                        group.setConditionGroupCondition(groupConditions);
                        conditionGroup.add(group);
                    }
                    ruleResponse.setConditionGroup(conditionGroup);
                }
            }
        }
        // 结果
        RuleEngineRule ruleEngineRule = this.ruleEngineRuleManager.getById(ruleEngineSimpleRule.getRuleId());
        Action action = getAction(ruleEngineRule.getActionValue(), ruleEngineRule.getActionType(), ruleEngineRule.getActionValueType());
        ruleResponse.setAction(action);
        // 默认结果
        Action defaultValue = getAction(ruleEngineSimpleRule.getDefaultActionValue(), ruleEngineSimpleRule.getDefaultActionType(), ruleEngineSimpleRule.getDefaultActionValueType());
        DefaultAction defaultAction = BasicConversion.INSTANCE.convert(defaultValue);
        defaultAction.setEnableDefaultAction(ruleEngineSimpleRule.getEnableDefaultAction());
        ruleResponse.setDefaultAction(defaultAction);
        ruleResponse.setAbnormalAlarm(JSON.parseObject(ruleEngineSimpleRule.getAbnormalAlarm(), AbnormalAlarm.class));
        return ruleResponse;
    }

    /**
     * 获取预览已发布的规则
     *
     * @param id 规则id
     * @return GetRuleResponse
     */
    @Override
    public ViewSimpleRuleResponse getPublishRule(Integer id) {
        RuleEngineSimpleRulePublish engineRulePublish = this.ruleEngineSimpleRulePublishManager.lambdaQuery()
                .eq(RuleEngineSimpleRulePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineSimpleRulePublish::getSimpleRuleId, id)
                .one();
        if (engineRulePublish == null) {
            throw new ValidException("找不到发布的规则:{}", id);
        }
        String data = engineRulePublish.getData();
        SimpleRule rule = SimpleRule.buildRule(data);
        return this.getRuleResponseProcess(rule);
    }

    /**
     * 规则预览
     *
     * @param id 规则id
     * @return GetRuleResponse
     */
    @Override
    public ViewSimpleRuleResponse getViewRule(Integer id) {
        RuleEngineSimpleRule ruleEngineSimpleRule = this.ruleEngineSimpleRuleManager.getById(id);
        if (ruleEngineSimpleRule == null) {
            throw new ValidException("找不到预览的规则数据:{}", id);
        }
        // 如果只有已发布
        if (ruleEngineSimpleRule.getStatus().equals(DataStatus.PUBLISHED.getStatus())) {
            return this.getPublishRule(id);
        }
        RuleEngineSimpleRulePublish engineRulePublish = this.ruleEngineSimpleRulePublishManager.lambdaQuery()
                .eq(RuleEngineSimpleRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                .eq(RuleEngineSimpleRulePublish::getSimpleRuleId, id)
                .one();
        if (engineRulePublish == null) {
            throw new ValidException("找不到预览的规则数据:{}", id);
        }
        String data = engineRulePublish.getData();
        SimpleRule rule = SimpleRule.buildRule(data);
        return this.getRuleResponseProcess(rule);
    }

    /**
     * 解析Rule配置信息为GetRuleResponse
     *
     * @param rule Rule
     * @return GetRuleResponse
     */
    private ViewSimpleRuleResponse getRuleResponseProcess(SimpleRule rule) {
        ViewSimpleRuleResponse ruleResponse = new ViewSimpleRuleResponse();
        ruleResponse.setId(rule.getId());
        ruleResponse.setName(rule.getName());
        ruleResponse.setCode(rule.getCode());
        ruleResponse.setWorkspaceId(rule.getWorkspaceId());
        ruleResponse.setWorkspaceCode(rule.getWorkspaceCode());
        ruleResponse.setDescription(rule.getDescription());

        List<ConditionGroup> conditionGroups = rule.getConditionSet().getConditionGroups();
        List<ConditionGroupConfig> groupArrayList = new ArrayList<>(conditionGroups.size());
        for (ConditionGroup conditionGroup : conditionGroups) {
            ConditionGroupConfig group = new ConditionGroupConfig();
            List<Condition> conditions = conditionGroup.getConditions();
            List<ConditionGroupCondition> conditionGroupConditions = new ArrayList<>(conditions.size());
            for (Condition condition : conditions) {
                ConditionGroupCondition conditionSet = new ConditionGroupCondition();
                ConditionResponse conditionResponse = new ConditionResponse();
                conditionResponse.setName(condition.getName());
                ConfigBean configBean = new ConfigBean();
                configBean.setLeftValue(this.getConfigValue(condition.getLeftValue()));
                configBean.setSymbol(condition.getOperator().getExplanation());
                configBean.setRightValue(this.getConfigValue(condition.getRightValue()));
                conditionResponse.setConfig(configBean);
                conditionSet.setCondition(conditionResponse);
                conditionGroupConditions.add(conditionSet);
            }
            group.setConditionGroupCondition(conditionGroupConditions);
            groupArrayList.add(group);
        }
        ruleResponse.setConditionGroup(groupArrayList);
        Value actionValue = rule.getActionValue();
        ruleResponse.setAction(BasicConversion.INSTANCE.convertAction(this.getConfigValue(actionValue)));
        DefaultAction defaultAction;
        if (rule.getDefaultActionValue() != null) {
            ConfigBean.Value value = this.getConfigValue(rule.getDefaultActionValue());
            defaultAction = BasicConversion.INSTANCE.convert(value);
            defaultAction.setEnableDefaultAction(EnableEnum.ENABLE.getStatus());
        } else {
            defaultAction = new DefaultAction();
            defaultAction.setEnableDefaultAction(EnableEnum.DISABLE.getStatus());
        }
        ruleResponse.setDefaultAction(defaultAction);
        ruleResponse.setAbnormalAlarm(rule.getAbnormalAlarm());
        // 规则调用接口，以及规则入参
        ruleResponse.setParameters(this.ruleCountInfoService.getParameters(rule));
        return ruleResponse;
    }


    /**
     * 解析值/变量/元素/固定值
     *
     * @param cValue Value
     * @return ConfigBean.Value
     */
    public ConfigBean.Value getConfigValue(Value cValue) {
        ConfigBean.Value value = new ConfigBean.Value();
        value.setValueType(cValue.getValueType().getValue());
        if (cValue instanceof Constant) {
            value.setType(VariableType.CONSTANT.getType());
            Constant constant = (Constant) cValue;
            value.setValue(String.valueOf(constant.getValue()));
            value.setValueName(String.valueOf(constant.getValue()));
        } else if (cValue instanceof Element) {
            value.setType(VariableType.ELEMENT.getType());
            Element element = (Element) cValue;
            RuleEngineElement ruleEngineElement = this.ruleEngineElementManager.getById(element.getElementId());
            value.setValue(String.valueOf(element.getElementId()));
            value.setValueName(ruleEngineElement.getName());
        } else if (cValue instanceof Variable) {
            value.setType(VariableType.VARIABLE.getType());
            Variable variable = (Variable) cValue;
            value.setValue(String.valueOf(variable.getVariableId()));
            RuleEngineVariable engineVariable = this.ruleEngineVariableManager.getById(variable.getVariableId());
            value.setValueName(engineVariable.getName());
            if (engineVariable.getType().equals(VariableType.CONSTANT.getType())) {
                value.setVariableValue(engineVariable.getValue());
            }
        }
        return value;
    }

    /**
     * 解析结果/默认结果
     *
     * @param value     结果值/可能为变量/元素
     * @param type      变量/元素/固定值
     * @param valueType STRING/NUMBER...
     * @return Action
     */
    public Action getAction(String value, Integer type, String valueType) {
        Action action = new Action();
        if (Validator.isEmpty(type)) {
            return action;
        }
        action.setValueType(valueType);
        action.setType(type);
        if (Validator.isEmpty(value)) {
            return action;
        }
        if (type.equals(VariableType.ELEMENT.getType())) {
            action.setValueName(this.ruleEngineElementManager.getById(value).getName());
        } else if (type.equals(VariableType.VARIABLE.getType())) {
            RuleEngineVariable engineVariable = this.ruleEngineVariableManager.getById(value);
            action.setValueName(engineVariable.getName());
            if (engineVariable.getType().equals(VariableType.CONSTANT.getType())) {
                action.setVariableValue(engineVariable.getValue());
            }
        }
        action.setValue(value);
        return action;
    }

}
