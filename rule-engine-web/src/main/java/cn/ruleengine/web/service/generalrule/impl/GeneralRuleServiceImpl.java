package cn.ruleengine.web.service.generalrule.impl;


import cn.ruleengine.core.rule.AbnormalAlarm;
import cn.ruleengine.core.rule.GeneralRule;
import cn.ruleengine.core.value.*;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.EnableEnum;
import cn.ruleengine.web.enums.DataStatus;
import cn.ruleengine.web.listener.body.GeneralRuleMessageBody;
import cn.ruleengine.web.listener.event.GeneralRuleEvent;
import cn.ruleengine.web.service.ConditionService;
import cn.ruleengine.web.service.impl.RuleParameterService;
import cn.ruleengine.web.service.generalrule.GeneralRuleResolveService;
import cn.ruleengine.web.service.generalrule.GeneralRuleService;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import cn.ruleengine.web.store.mapper.RuleEngineRuleMapper;
import cn.ruleengine.web.store.mapper.RuleEngineGeneralRuleMapper;

import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.condition.*;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageBase;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.generalrule.*;
import cn.ruleengine.core.condition.ConditionGroup;

import cn.ruleengine.core.condition.Condition;
import cn.ruleengine.web.vo.generalrule.DefaultAction;


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
public class GeneralRuleServiceImpl implements GeneralRuleService {

    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private RuleEngineGeneralRuleManager ruleEngineGeneralRuleManager;
    @Resource
    private RuleEngineGeneralRuleMapper ruleEngineGeneralRuleMapper;
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
    private RuleEngineGeneralRulePublishManager ruleEngineGeneralRulePublishManager;
    @Resource
    private GeneralRuleResolveService ruleResolveService;
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
    public PageResult<ListGeneralRuleResponse> list(PageRequest<ListGeneralRuleRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        Workspace workspace = Context.getCurrentWorkspace();
        return PageUtils.page(this.ruleEngineGeneralRuleManager, page, () -> {
            QueryWrapper<RuleEngineGeneralRule> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(RuleEngineGeneralRule::getWorkspaceId, workspace.getId());
            PageUtils.defaultOrder(orders, wrapper);

            ListGeneralRuleRequest query = pageRequest.getQuery();
            if (Validator.isNotEmpty(query.getName())) {
                wrapper.lambda().like(RuleEngineGeneralRule::getName, query.getName());
            }
            if (Validator.isNotEmpty(query.getCode())) {
                wrapper.lambda().like(RuleEngineGeneralRule::getCode, query.getCode());
            }
            if (Validator.isNotEmpty(query.getStatus())) {
                wrapper.lambda().eq(RuleEngineGeneralRule::getStatus, query.getStatus());
            }
            return wrapper;
        }, m -> {
            ListGeneralRuleResponse listRuleResponse = new ListGeneralRuleResponse();
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
        Integer count = this.ruleEngineGeneralRuleManager.lambdaQuery()
                .eq(RuleEngineGeneralRule::getWorkspaceId, workspace.getId())
                .eq(RuleEngineGeneralRule::getCode, code)
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
    public Boolean updateRule(UpdateGeneralRuleRequest updateRuleRequest) {
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(updateRuleRequest.getId());
        if (ruleEngineGeneralRule == null) {
            throw new ValidException("不存在规则:{}", updateRuleRequest.getId());
        }
        // 如果之前是待发布，则删除原有待发布数据
        if (Objects.equals(ruleEngineGeneralRule.getStatus(), DataStatus.WAIT_PUBLISH.getStatus())) {
            this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                    .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                    .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, updateRuleRequest.getId())
                    .remove();
        }
        // 如果原来有条件信息，先删除原有信息
        this.removeConditionGroupByRuleId(ruleEngineGeneralRule.getRuleId());
        // 保存条件信息
        this.saveConditionGroup(ruleEngineGeneralRule.getRuleId(), updateRuleRequest.getConditionGroup());
        //  更新规则信息
        ruleEngineGeneralRule.setId(updateRuleRequest.getId());
        ruleEngineGeneralRule.setStatus(DataStatus.EDIT.getStatus());
        // 保存规则结果
        ConfigValue action = updateRuleRequest.getAction();
        RuleEngineRule ruleEngineRule = new RuleEngineRule();
        ruleEngineRule.setId(ruleEngineGeneralRule.getRuleId());
        ruleEngineRule.setActionType(action.getType());
        ruleEngineRule.setActionValueType(action.getValueType());
        ruleEngineRule.setActionValue(action.getValue());
        ruleEngineRuleMapper.updateRuleById(ruleEngineRule);
        // 保存默认结果
        DefaultAction defaultAction = updateRuleRequest.getDefaultAction();
        ruleEngineGeneralRule.setEnableDefaultAction(defaultAction.getEnableDefaultAction());
        ruleEngineGeneralRule.setDefaultActionValue(defaultAction.getValue());
        ruleEngineGeneralRule.setDefaultActionValueType(defaultAction.getValueType());
        ruleEngineGeneralRule.setDefaultActionType(defaultAction.getType());
        ruleEngineGeneralRule.setAbnormalAlarm(JSONObject.toJSONString(updateRuleRequest.getAbnormalAlarm()));
        this.ruleEngineGeneralRuleMapper.updateRuleById(ruleEngineGeneralRule);
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
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        if (ruleEngineGeneralRule == null) {
            return false;
        }
        // 从引擎中移除规则
        if (this.engine.isExists(ruleEngineGeneralRule.getWorkspaceCode(), ruleEngineGeneralRule.getCode())) {
            GeneralRuleMessageBody ruleMessageBody = new GeneralRuleMessageBody();
            ruleMessageBody.setType(GeneralRuleMessageBody.Type.REMOVE);
            ruleMessageBody.setWorkspaceId(ruleEngineGeneralRule.getWorkspaceId());
            ruleMessageBody.setWorkspaceCode(ruleEngineGeneralRule.getWorkspaceCode());
            ruleMessageBody.setRuleCode(ruleEngineGeneralRule.getCode());
            this.eventPublisher.publishEvent(new GeneralRuleEvent(ruleMessageBody));
        }
        this.ruleEngineRuleManager.removeById(ruleEngineGeneralRule.getRuleId());
        // 删除规则发布记录
        this.ruleEngineGeneralRulePublishManager.lambdaUpdate().eq(RuleEngineGeneralRulePublish::getGeneralRuleId, id).remove();
        // 删除规则条件组信息
        this.removeConditionGroupByRuleId(ruleEngineGeneralRule.getRuleId());
        // 删除规则
        return this.ruleEngineGeneralRuleManager.removeById(id);
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
    public Integer saveOrUpdateRuleDefinition(GeneralRuleDefinition ruleDefinition) {
        // 创建规则
        RuleEngineGeneralRule ruleEngineGeneralRule = new RuleEngineGeneralRule();
        if (ruleDefinition.getId() == null) {
            if (this.ruleCodeIsExists(ruleDefinition.getCode())) {
                throw new ValidException("规则Code：{}已经存在", ruleDefinition.getCode());
            }
            Workspace workspace = Context.getCurrentWorkspace();
            UserData userData = Context.getCurrentUser();
            ruleEngineGeneralRule.setCreateUserId(userData.getId());
            ruleEngineGeneralRule.setCreateUserName(userData.getUsername());
            ruleEngineGeneralRule.setWorkspaceId(workspace.getId());
            ruleEngineGeneralRule.setWorkspaceCode(workspace.getCode());
            RuleEngineRule ruleEngineRule = new RuleEngineRule();
            ruleEngineRule.setName(ruleDefinition.getName());
            ruleEngineRule.setCode(ruleDefinition.getCode());
            ruleEngineRule.setDescription(ruleDefinition.getDescription());
            ruleEngineRule.setCreateUserId(userData.getId());
            ruleEngineRule.setCreateUserName(userData.getUsername());
            this.ruleEngineRuleManager.save(ruleEngineRule);
            ruleEngineGeneralRule.setRuleId(ruleEngineRule.getId());
        } else {
            Integer count = this.ruleEngineRuleManager.lambdaQuery()
                    .eq(RuleEngineRule::getId, ruleDefinition.getId())
                    .count();
            if (count == null || count == 0) {
                throw new ValidException("不存在规则:{}", ruleDefinition.getId());
            }
        }
        ruleEngineGeneralRule.setId(ruleDefinition.getId());
        ruleEngineGeneralRule.setName(ruleDefinition.getName());
        ruleEngineGeneralRule.setCode(ruleDefinition.getCode());
        ruleEngineGeneralRule.setDescription(ruleDefinition.getDescription());
        ruleEngineGeneralRule.setStatus(DataStatus.EDIT.getStatus());
        this.ruleEngineGeneralRuleManager.saveOrUpdate(ruleEngineGeneralRule);
        return ruleEngineGeneralRule.getId();
    }

    /**
     * 获取规则定义信息
     *
     * @param id 规则id
     * @return 规则定义信息
     */
    @Override
    public GeneralRuleDefinition getRuleDefinition(Integer id) {
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
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(releaseRequest.getId());
        if (ruleEngineGeneralRule == null) {
            throw new ValidException("不存在规则:{}", releaseRequest.getId());
        }
        Integer originStatus = ruleEngineGeneralRule.getStatus();
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
        this.removeConditionGroupByRuleId(ruleEngineGeneralRule.getRuleId());
        // 保存条件信息
        this.saveConditionGroup(ruleEngineGeneralRule.getRuleId(), releaseRequest.getConditionGroup());
        //  更新规则信息
        ruleEngineGeneralRule.setId(releaseRequest.getId());
        ruleEngineGeneralRule.setStatus(DataStatus.WAIT_PUBLISH.getStatus());
        // 保存结果
        ConfigValue action = releaseRequest.getAction();
        RuleEngineRule ruleEngineRule = new RuleEngineRule();
        ruleEngineRule.setActionValue(action.getValue());
        ruleEngineRule.setId(ruleEngineGeneralRule.getRuleId());
        ruleEngineRule.setActionType(action.getType());
        ruleEngineRule.setActionValueType(action.getValueType());
        ruleEngineRuleMapper.updateRuleById(ruleEngineRule);
        // 保存默认结果
        ruleEngineGeneralRule.setEnableDefaultAction(defaultAction.getEnableDefaultAction());
        ruleEngineGeneralRule.setDefaultActionValue(defaultAction.getValue());
        ruleEngineGeneralRule.setDefaultActionValueType(defaultAction.getValueType());
        ruleEngineGeneralRule.setDefaultActionType(defaultAction.getType());
        ruleEngineGeneralRule.setAbnormalAlarm(JSONObject.toJSONString(releaseRequest.getAbnormalAlarm()));
        this.ruleEngineGeneralRuleMapper.updateRuleById(ruleEngineGeneralRule);
        // 生成待发布规则
        if (Objects.equals(originStatus, DataStatus.WAIT_PUBLISH.getStatus())) {
            // 删除原有待发布规则
            this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                    .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                    .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, ruleEngineGeneralRule.getId())
                    .remove();
        }
        // 添加新的待发布数据
        GeneralRule rule = this.ruleResolveService.ruleProcess(ruleEngineGeneralRule);
        RuleEngineGeneralRulePublish rulePublish = new RuleEngineGeneralRulePublish();
        rulePublish.setGeneralRuleId(rule.getId());
        rulePublish.setGeneralRuleCode(rule.getCode());
        rulePublish.setData(rule.toJson());
        rulePublish.setStatus(DataStatus.WAIT_PUBLISH.getStatus());
        rulePublish.setWorkspaceId(rule.getWorkspaceId());
        rulePublish.setWorkspaceCode(rule.getWorkspaceCode());
        this.ruleEngineGeneralRulePublishManager.save(rulePublish);
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
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        if (ruleEngineGeneralRule == null) {
            throw new ValidException("不存在规则:{}", id);
        }
        if (ruleEngineGeneralRule.getStatus().equals(DataStatus.EDIT.getStatus())) {
            throw new ValidException("该规则不可执行:{}", id);
        }
        // 如果已经是发布规则了
        if (ruleEngineGeneralRule.getStatus().equals(DataStatus.PUBLISHED.getStatus())) {
            return true;
        }
        // 修改为已发布
        this.ruleEngineGeneralRuleManager.lambdaUpdate()
                .set(RuleEngineGeneralRule::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineGeneralRule::getId, ruleEngineGeneralRule.getId())
                .update();
        // 删除原有的已发布规则数据
        this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, ruleEngineGeneralRule.getId())
                .remove();
        // 更新待发布为已发布
        this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                .set(RuleEngineGeneralRulePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, ruleEngineGeneralRule.getId())
                .update();
        // 加载规则
        GeneralRuleMessageBody ruleMessageBody = new GeneralRuleMessageBody();
        ruleMessageBody.setType(GeneralRuleMessageBody.Type.LOAD);
        ruleMessageBody.setRuleCode(ruleEngineGeneralRule.getCode());
        ruleMessageBody.setWorkspaceId(ruleEngineGeneralRule.getWorkspaceId());
        ruleMessageBody.setWorkspaceCode(ruleEngineGeneralRule.getWorkspaceCode());
        this.eventPublisher.publishEvent(new GeneralRuleEvent(ruleMessageBody));
        return true;
    }

    /**
     * 获取规则信息
     *
     * @param id 规则id
     * @return 规则信息
     */
    @Override
    public GetGeneralRuleResponse getRuleConfig(Integer id) {
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        if (ruleEngineGeneralRule == null) {
            return null;
        }
        GetGeneralRuleResponse ruleResponse = new GetGeneralRuleResponse();
        ruleResponse.setId(ruleEngineGeneralRule.getId());
        ruleResponse.setCode(ruleEngineGeneralRule.getCode());
        ruleResponse.setName(ruleEngineGeneralRule.getName());
        ruleResponse.setDescription(ruleEngineGeneralRule.getDescription());
        List<RuleEngineConditionGroup> engineConditionGroups = this.ruleEngineConditionGroupManager.lambdaQuery()
                .eq(RuleEngineConditionGroup::getRuleId, ruleEngineGeneralRule.getRuleId())
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
        RuleEngineRule ruleEngineRule = this.ruleEngineRuleManager.getById(ruleEngineGeneralRule.getRuleId());
        ConfigValue action = this.getAction(ruleEngineRule.getActionValue(), ruleEngineRule.getActionType(), ruleEngineRule.getActionValueType());
        ruleResponse.setAction(action);
        // 默认结果
        ConfigValue defaultValue = this.getAction(ruleEngineGeneralRule.getDefaultActionValue(), ruleEngineGeneralRule.getDefaultActionType(), ruleEngineGeneralRule.getDefaultActionValueType());
        DefaultAction defaultAction = new DefaultAction(defaultValue);
        defaultAction.setEnableDefaultAction(ruleEngineGeneralRule.getEnableDefaultAction());
        ruleResponse.setDefaultAction(defaultAction);
        ruleResponse.setAbnormalAlarm(JSON.parseObject(ruleEngineGeneralRule.getAbnormalAlarm(), AbnormalAlarm.class));
        return ruleResponse;
    }

    /**
     * 获取预览已发布的规则
     *
     * @param id 规则id
     * @return GetRuleResponse
     */
    @Override
    public ViewGeneralRuleResponse getPublishRule(Integer id) {
        RuleEngineGeneralRulePublish engineRulePublish = this.ruleEngineGeneralRulePublishManager.lambdaQuery()
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, id)
                .one();
        if (engineRulePublish == null) {
            throw new ValidException("找不到发布的规则:{}", id);
        }
        String data = engineRulePublish.getData();
        GeneralRule rule = GeneralRule.buildRule(data);
        return this.getRuleResponseProcess(rule);
    }

    /**
     * 规则预览
     *
     * @param id 规则id
     * @return GetRuleResponse
     */
    @Override
    public ViewGeneralRuleResponse getViewRule(Integer id) {
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        if (ruleEngineGeneralRule == null) {
            throw new ValidException("找不到预览的规则数据:{}", id);
        }
        // 如果只有已发布
        if (ruleEngineGeneralRule.getStatus().equals(DataStatus.PUBLISHED.getStatus())) {
            return this.getPublishRule(id);
        }
        RuleEngineGeneralRulePublish engineRulePublish = this.ruleEngineGeneralRulePublishManager.lambdaQuery()
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, id)
                .one();
        if (engineRulePublish == null) {
            throw new ValidException("找不到预览的规则数据:{}", id);
        }
        String data = engineRulePublish.getData();
        GeneralRule rule = GeneralRule.buildRule(data);
        return this.getRuleResponseProcess(rule);
    }

    /**
     * 解析Rule配置信息为GetRuleResponse
     *
     * @param rule Rule
     * @return GetRuleResponse
     */
    private ViewGeneralRuleResponse getRuleResponseProcess(GeneralRule rule) {
        ViewGeneralRuleResponse ruleResponse = new ViewGeneralRuleResponse();
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
        ruleResponse.setAction(this.getConfigValue(rule.getActionValue()));
        DefaultAction defaultAction;
        if (rule.getDefaultActionValue() != null) {
            defaultAction = new DefaultAction(this.getConfigValue(rule.getDefaultActionValue()));
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
    public ConfigValue getConfigValue(Value cValue) {
        ConfigValue value = new ConfigValue();
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
    public ConfigValue getAction(String value, Integer type, String valueType) {
        ConfigValue action = new ConfigValue();
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
