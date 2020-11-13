package com.engine.web.service.impl;

import com.engine.core.condition.ConditionGroup;
import com.engine.core.value.*;
import com.engine.web.service.ValueResolve;
import com.engine.web.vo.common.DataCacheMap;
import com.engine.web.vo.condition.ConditionGroupCondition;
import com.engine.web.vo.condition.ConfigBean;

import com.engine.web.vo.condition.ConditionResponse;

import com.engine.core.condition.Condition;
import com.engine.web.vo.rule.DefaultAction;
import com.engine.web.vo.rule.Action;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.engine.core.DefaultInput;
import com.engine.core.Engine;
import com.engine.core.OutPut;
import com.engine.core.exception.ValidException;
import com.engine.core.rule.Rule;
import com.engine.web.enums.EnableEnum;
import com.engine.web.enums.RuleStatus;
import com.engine.web.service.ConditionService;
import com.engine.web.service.RuleResolveService;
import com.engine.web.store.entity.*;

import com.engine.web.config.rabbit.RabbitTopicConfig;
import com.engine.web.store.manager.*;

import com.engine.web.vo.condition.ConditionGroupConfig;
import com.engine.web.vo.rule.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.engine.web.service.RuleService;
import com.engine.web.util.PageUtils;
import com.engine.web.vo.base.request.PageRequest;
import com.engine.web.vo.base.response.PageBase;
import com.engine.web.vo.base.response.PageResult;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
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
public class RuleServiceImpl implements RuleService {

    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private RuleEngineConditionGroupConditionManager ruleEngineConditionGroupConditionManager;
    @Resource
    private RuleEngineConditionGroupManager ruleEngineConditionGroupManager;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private Engine engine;
    @Resource
    private ConditionService conditionService;
    @Resource
    private RuleCountInfoService ruleCountInfoService;
    @Resource
    private RuleEngineRulePublishManager ruleEngineRulePublishManager;
    @Resource
    private RuleResolveService ruleResolveService;
    @Resource
    private ValueResolve valueResolve;

    /**
     * 规则列表
     *
     * @param pageRequest 分页查询数据
     * @return page
     */
    @Override
    public PageResult<ListRuleResponse> list(PageRequest<ListRuleRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        return PageUtils.page(ruleEngineRuleManager, page, () -> {
            QueryWrapper<RuleEngineRule> wrapper = new QueryWrapper<>();
            PageUtils.defaultOrder(orders, wrapper);

            ListRuleRequest query = pageRequest.getQuery();
            if (Validator.isNotEmpty(query.getName())) {
                wrapper.lambda().like(RuleEngineRule::getName, query.getName());
            }
            if (Validator.isNotEmpty(query.getCode())) {
                wrapper.lambda().like(RuleEngineRule::getCode, query.getCode());
            }
            if (Validator.isNotEmpty(query.getStatus())) {
                wrapper.lambda().eq(RuleEngineRule::getStatus, query.getStatus());
            }
            return wrapper;
        }, m -> {
            ListRuleResponse listRuleResponse = new ListRuleResponse();
            listRuleResponse.setId(m.getId());
            listRuleResponse.setName(m.getName());
            listRuleResponse.setCode(m.getCode());
            listRuleResponse.setIsPublish(engine.isExistsRule(m.getCode()));
            listRuleResponse.setCreateUserName(m.getCreateUserName());
            listRuleResponse.setStatus(m.getStatus());
            listRuleResponse.setCreateTime(m.getCreateTime());
            return listRuleResponse;
        });
    }

    /**
     * 更新规则信息
     *
     * @param updateRuleRequest 规则配置数据
     * @return true执行成功
     */
    @Override
    public Boolean updateRule(UpdateRuleRequest updateRuleRequest) {
        // 如果原来有条件信息，先删除原有信息
        List<RuleEngineConditionGroup> engineConditionGroups = ruleEngineConditionGroupManager.lambdaQuery().eq(RuleEngineConditionGroup::getRuleId, updateRuleRequest.getId()).list();
        if (CollUtil.isNotEmpty(engineConditionGroups)) {
            List<Integer> engineConditionGroupIds = engineConditionGroups.stream().map(RuleEngineConditionGroup::getId).collect(Collectors.toList());
            this.ruleEngineConditionGroupManager.removeByIds(engineConditionGroupIds);
            // 删除条件组条件
            this.ruleEngineConditionGroupConditionManager.lambdaUpdate().in(RuleEngineConditionGroupCondition::getConditionGroupId, engineConditionGroupIds).remove();
        }
        //  更新规则信息
        LambdaUpdateChainWrapper<RuleEngineRule> updateChainWrapper = this.ruleEngineRuleManager.lambdaUpdate()
                .set(RuleEngineRule::getId, updateRuleRequest.getId())
                .set(RuleEngineRule::getAbnormalAlarm, JSONObject.toJSONString(updateRuleRequest.getAbnormalAlarm()))
                .set(RuleEngineRule::getStatus, updateRuleRequest.getStatus());
        // 保存结果
        Action action = updateRuleRequest.getAction();
        updateChainWrapper.set(RuleEngineRule::getActionType, action.getType())
                .set(RuleEngineRule::getActionValue, action.getValue())
                .set(RuleEngineRule::getActionValueType, action.getValueType());
        // 保存默认结果
        DefaultAction defaultAction = updateRuleRequest.getDefaultAction();
        updateChainWrapper.set(RuleEngineRule::getDefaultActionType, defaultAction.getType())
                .set(RuleEngineRule::getDefaultActionValue, defaultAction.getValue())
                .set(RuleEngineRule::getDefaultActionValueType, defaultAction.getValueType())
                .set(RuleEngineRule::getEnableDefaultAction, defaultAction.getEnableDefaultAction())
                .eq(RuleEngineRule::getId, updateRuleRequest.getId())
                .update();
        // 保存条件信息
        this.saveConditionGroup(updateRuleRequest.getId(), updateRuleRequest.getConditionGroup());
        RuleEngineRule ruleEngineRule = this.ruleEngineRuleManager.getById(updateRuleRequest.getId());
        // 统计引用的变量元素条件
        RuleCountInfo ruleCountInfo = this.ruleCountInfoService.countRuleInfo(ruleEngineRule);
        this.ruleEngineRuleManager.lambdaUpdate().set(RuleEngineRule::getCountInfo, JSON.toJSONString(ruleCountInfo))
                .eq(RuleEngineRule::getId, updateRuleRequest.getId())
                .update();
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
        RuleEngineRule engineRule = this.ruleEngineRuleManager.getById(id);
        if (engineRule == null) {
            return false;
        }
        if (engine.isExistsRule(engineRule.getCode())) {
            RuleMessageVo ruleMessageVo = new RuleMessageVo();
            ruleMessageVo.setType(RuleMessageVo.Type.REMOVE);
            ruleMessageVo.setRuleCode(engineRule.getCode());
            this.rabbitTemplate.convertAndSend(RabbitTopicConfig.RULE_EXCHANGE, RabbitTopicConfig.RULE_TOPIC_ROUTING_KEY, ruleMessageVo);
        }
        return this.ruleEngineRuleManager.removeById(id);
    }

    /**
     * 保存或者更新规则定义信息
     *
     * @param ruleDefinition 规则定义信息
     * @return 规则id
     */
    @Override
    public Integer saveOrUpdateRuleDefinition(RuleDefinition ruleDefinition) {
        RuleEngineRule ruleEngineRule = new RuleEngineRule();
        ruleEngineRule.setId(ruleDefinition.getId());
        ruleEngineRule.setName(ruleDefinition.getName());
        ruleEngineRule.setCode(ruleDefinition.getCode());
        ruleEngineRule.setDescription(ruleDefinition.getDescription());
        ruleEngineRule.setStatus(RuleStatus.EDIT.getStatus());
        this.ruleEngineRuleManager.saveOrUpdate(ruleEngineRule);
        return ruleEngineRule.getId();
    }

    /**
     * 获取规则定义信息
     *
     * @param id 规则id
     * @return 规则定义信息
     */
    @Override
    public RuleDefinition getRuleDefinition(Integer id) {
        RuleEngineRule engineRule = this.ruleEngineRuleManager.getById(id);
        if (engineRule == null) {
            return null;
        }
        RuleDefinition ruleDefinition = new RuleDefinition();
        BeanUtil.copyProperties(engineRule, ruleDefinition);
        return ruleDefinition;
    }

    /**
     * 生成待发布版本，更新规则数据
     *
     * @param releaseRequest 规则配置数据
     * @return true
     */
    @Override
    public Boolean generationRelease(GenerationReleaseRequest releaseRequest) {
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
        RuleEngineRule ruleEngineRule = ruleEngineRuleManager.getById(releaseRequest.getId());
        if (ruleEngineRule == null) {
            throw new ValidException("不存在规则:{}", releaseRequest.getId());
        }
        releaseRequest.setStatus(RuleStatus.WAIT_PUBLISH.getStatus());
        this.updateRule(releaseRequest);

        // 删除原有的待发布规则数据
        this.ruleEngineRulePublishManager.lambdaUpdate()
                .eq(RuleEngineRulePublish::getStatus, RuleStatus.WAIT_PUBLISH.getStatus())
                .eq(RuleEngineRulePublish::getRuleId, ruleEngineRule.getId()).remove();
        // 添加新的待发布数据
        Rule rule = this.ruleResolveService.getRuleByCode(ruleEngineRule.getCode());
        RuleEngineRulePublish rulePublish = new RuleEngineRulePublish();
        rulePublish.setStatus(RuleStatus.WAIT_PUBLISH.getStatus());
        rulePublish.setRuleId(rule.getId());
        rulePublish.setData(rule.toJson());
        rulePublish.setRuleCode(rule.getCode());
        rulePublish.setCountInfo(ruleEngineRule.getCountInfo());
        this.ruleEngineRulePublishManager.save(rulePublish);
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
        RuleEngineRule ruleEngineRule = ruleEngineRuleManager.getById(id);
        if (ruleEngineRule == null) {
            throw new ValidException("不存在规则:{}", id);
        }
        if (ruleEngineRule.getStatus().equals(RuleStatus.EDIT.getStatus())) {
            throw new ValidException("该规则不可执行:{}", id);
        }
        // 修改为已发布
        this.ruleEngineRuleManager.lambdaUpdate()
                .set(RuleEngineRule::getStatus, RuleStatus.PUBLISHED.getStatus())
                .eq(RuleEngineRule::getId, ruleEngineRule.getId())
                .update();
        // 删除原有的已发布规则数据
        this.ruleEngineRulePublishManager.lambdaUpdate()
                .eq(RuleEngineRulePublish::getStatus, RuleStatus.PUBLISHED.getStatus())
                .eq(RuleEngineRulePublish::getRuleId, ruleEngineRule.getId()).remove();
        // 添加新的发布数据
        Rule rule = this.ruleResolveService.getRuleByCode(ruleEngineRule.getCode());
        RuleEngineRulePublish rulePublish = new RuleEngineRulePublish();
        rulePublish.setStatus(RuleStatus.PUBLISHED.getStatus());
        rulePublish.setRuleId(rule.getId());
        rulePublish.setRuleCode(ruleEngineRule.getCode());
        rulePublish.setData(rule.toJson());
        rulePublish.setCountInfo(ruleEngineRule.getCountInfo());
        this.ruleEngineRulePublishManager.save(rulePublish);
        // 加载规则
        RuleMessageVo ruleMessageVo = new RuleMessageVo();
        ruleMessageVo.setType(RuleMessageVo.Type.LOAD);
        ruleMessageVo.setRuleCode(ruleEngineRule.getCode());
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.RULE_EXCHANGE, RabbitTopicConfig.RULE_TOPIC_ROUTING_KEY, ruleMessageVo);
        return true;
    }

    /**
     * 获取规则信息
     *
     * @param id 规则id
     * @return 规则信息
     */
    @Override
    public GetRuleResponse getRuleConfig(Integer id) {
        RuleEngineRule ruleEngineRule = ruleEngineRuleManager.getById(id);
        if (ruleEngineRule == null) {
            return null;
        }
        GetRuleResponse ruleResponse = new GetRuleResponse();
        ruleResponse.setId(ruleEngineRule.getId());
        ruleResponse.setCode(ruleEngineRule.getCode());
        ruleResponse.setName(ruleEngineRule.getName());
        ruleResponse.setDescription(ruleEngineRule.getDescription());
        List<RuleEngineConditionGroup> engineConditionGroups = this.ruleEngineConditionGroupManager.lambdaQuery()
                .eq(RuleEngineConditionGroup::getRuleId, id)
                .orderByAsc(RuleEngineConditionGroup::getOrderNo)
                .list();
        String countInfo = ruleEngineRule.getCountInfo();
        DataCacheMap cacheMap = this.valueResolve.getCacheMap(JSON.parseObject(countInfo, RuleCountInfo.class));
        Map<Integer, RuleEngineCondition> conditionMap = cacheMap.getConditionMap();
        // 加载所有的用到的条件组条件
        Map<Integer, List<RuleEngineConditionGroupCondition>> conditionGroupConditionMaps = Optional.of(engineConditionGroups)
                .filter(CollUtil::isNotEmpty)
                .map(m -> m.stream().map(RuleEngineConditionGroup::getId).collect(Collectors.toSet()))
                .map(m -> this.ruleEngineConditionGroupConditionManager.lambdaQuery()
                        .in(RuleEngineConditionGroupCondition::getConditionGroupId, m)
                        .orderByAsc(RuleEngineConditionGroupCondition::getOrderNo)
                        .list().stream().collect(Collectors.groupingBy(RuleEngineConditionGroupCondition::getConditionGroupId)))
                .orElse(new HashMap<>());
        // 变量
        Map<Integer, RuleEngineVariable> variableMap = cacheMap.getVariableMap();
        // 元素
        Map<Integer, RuleEngineElement> elementMap = cacheMap.getElementMap();
        // 转换条件组数据
        List<ConditionGroupConfig> conditionGroup = new ArrayList<>();
        for (RuleEngineConditionGroup engineConditionGroup : engineConditionGroups) {
            ConditionGroupConfig group = new ConditionGroupConfig();
            group.setId(engineConditionGroup.getId());
            group.setName(engineConditionGroup.getName());
            group.setOrderNo(engineConditionGroup.getOrderNo());
            List<RuleEngineConditionGroupCondition> conditionGroupConditions = conditionGroupConditionMaps.get(engineConditionGroup.getId());
            if (CollUtil.isNotEmpty(conditionGroupConditions)) {
                List<ConditionGroupCondition> groupConditions = new ArrayList<>();
                for (RuleEngineConditionGroupCondition conditionGroupCondition : conditionGroupConditions) {
                    ConditionGroupCondition conditionSet = new ConditionGroupCondition();
                    conditionSet.setId(conditionGroupCondition.getId());
                    conditionSet.setOrderNo(conditionGroupCondition.getOrderNo());
                    RuleEngineCondition engineCondition = conditionMap.get(conditionGroupCondition.getConditionId());
                    conditionSet.setCondition(this.conditionService.getConditionResponse(engineCondition, variableMap, elementMap));
                    groupConditions.add(conditionSet);
                }
                group.setConditionGroupCondition(groupConditions);
            }
            conditionGroup.add(group);
        }
        ruleResponse.setConditionGroup(conditionGroup);
        // 结果
        ConfigBean.Value action = getAction(ruleEngineRule.getActionValue(), ruleEngineRule.getActionType(), ruleEngineRule.getActionValueType(), elementMap, variableMap);
        ruleResponse.setAction(action);
        // 默认结果
        DefaultAction defaultAction = new DefaultAction();
        BeanUtil.copyProperties(getAction(ruleEngineRule.getDefaultActionValue(), ruleEngineRule.getDefaultActionType(), ruleEngineRule.getDefaultActionValueType(), elementMap, variableMap)
                , defaultAction);
        defaultAction.setEnableDefaultAction(ruleEngineRule.getEnableDefaultAction());
        ruleResponse.setDefaultAction(defaultAction);
        ruleResponse.setAbnormalAlarm(JSON.parseObject(ruleEngineRule.getAbnormalAlarm(), Rule.AbnormalAlarm.class));
        return ruleResponse;
    }

    /**
     * 获取预览已发布的规则
     *
     * @param id 规则id
     * @return GetRuleResponse
     */
    @Override
    public ViewRuleResponse getPublishRule(Integer id) {
        RuleEngineRulePublish engineRulePublish = this.ruleEngineRulePublishManager.lambdaQuery()
                .eq(RuleEngineRulePublish::getStatus, RuleStatus.PUBLISHED.getStatus())
                .eq(RuleEngineRulePublish::getRuleId, id).one();
        if (engineRulePublish == null) {
            throw new ValidException("找不到发布的规则:{}", id);
        }
        String data = engineRulePublish.getData();
        Rule rule = new Rule();
        rule.fromJson(data);
        return this.getRuleResponseProcess(rule);
    }

    /**
     * 规则预览
     *
     * @param id 规则id
     * @return GetRuleResponse
     */
    @Override
    public ViewRuleResponse getViewRule(Integer id) {
        RuleEngineRulePublish engineRulePublish = this.ruleEngineRulePublishManager.lambdaQuery()
                .eq(RuleEngineRulePublish::getStatus, RuleStatus.WAIT_PUBLISH.getStatus())
                .eq(RuleEngineRulePublish::getRuleId, id).one();
        if (engineRulePublish == null) {
            throw new ValidException("找不到待发布的规则:{}", id);
        }
        String data = engineRulePublish.getData();
        Rule rule = new Rule();
        rule.fromJson(data);
        return this.getRuleResponseProcess(rule);
    }

    /**
     * 解析Rule配置信息为GetRuleResponse
     *
     * @param rule Rule
     * @return GetRuleResponse
     */
    private ViewRuleResponse getRuleResponseProcess(Rule rule) {
        ViewRuleResponse ruleResponse = new ViewRuleResponse();
        ruleResponse.setId(rule.getId());
        ruleResponse.setName(rule.getName());
        ruleResponse.setCode(rule.getCode());
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
                conditionResponse.setName(conditionResponse.getName());
                conditionResponse.setDescription(conditionResponse.getDescription());
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
        ruleResponse.setAction(this.getConfigValue(actionValue));
        DefaultAction defaultAction = new DefaultAction();
        if (rule.getDefaultActionValue() != null) {
            defaultAction.setEnableDefaultAction(EnableEnum.ENABLE.getStatus());
            ConfigBean.Value value = this.getConfigValue(rule.getDefaultActionValue());
            BeanUtil.copyProperties(value, defaultAction);
        } else {
            defaultAction.setEnableDefaultAction(EnableEnum.DISABLE.getStatus());
        }
        ruleResponse.setDefaultAction(defaultAction);
        ruleResponse.setAbnormalAlarm(rule.getAbnormalAlarm());
        // 规则调用接口，以及规则入参
        ruleResponse.setRuleInterfaceDescription(this.getRuleInterfaceDescriptionResponse(rule));
        return ruleResponse;
    }

    /**
     * 规则调用接口，以及规则入参
     *
     * @param rule rule
     * @return RuleInterfaceDescriptionResponse
     */
    private RuleInterfaceDescriptionResponse getRuleInterfaceDescriptionResponse(Rule rule) {
        RuleInterfaceDescriptionResponse interfaceDescriptionResponse = new RuleInterfaceDescriptionResponse();
        String ruleExeInterfaceUrlRuleCode = "getRuleExeInterfaceUrl";
        if (engine.isExistsRule(ruleExeInterfaceUrlRuleCode)) {
            // 获取规则调用地址
            OutPut outPut = engine.execute(new DefaultInput(), ruleExeInterfaceUrlRuleCode);
            interfaceDescriptionResponse.setRequestUrl(String.valueOf(outPut.getValue()));
        } else {
            interfaceDescriptionResponse.setRequestUrl("请配置规则:" + ruleExeInterfaceUrlRuleCode);
        }
        interfaceDescriptionResponse.setParameters(rule.getParameters());
        return interfaceDescriptionResponse;
    }

    /**
     * 解析值/变量/元素/固定值
     *
     * @param cValue Value
     * @return ConfigBean.Value
     */
    public ConfigBean.Value getConfigValue(Value cValue) {
        ConfigBean.Value value = new ConfigBean.Value();
        value.setValueType(cValue.getValueType());
        if (cValue instanceof Constant) {
            value.setType(VariableType.CONSTANT.getType());
            Constant constant = (Constant) cValue;
            value.setValue(String.valueOf(constant.getValue()));
            value.setValueName(String.valueOf(constant.getValue()));
        } else if (cValue instanceof Element) {
            value.setType(VariableType.ELEMENT.getType());
            Element element = (Element) cValue;
            value.setValue(String.valueOf(element.getElementId()));
            value.setValueName(element.getElementName());
        } else if (cValue instanceof Variable) {
            value.setType(VariableType.VARIABLE.getType());
            Variable variable = (Variable) cValue;
            value.setValue(String.valueOf(variable.getId()));
            value.setValueName(variable.getName());
            com.engine.core.value.Value cVariable = engine.getEngineVariable().getVariable(variable.getId());
            if (cVariable instanceof Constant) {
                Constant constant = (Constant) cVariable;
                value.setVariableValue(String.valueOf(constant.getValue()));
            }
        }
        return value;
    }

    /**
     * 解析结果/默认结果
     *
     * @param value       结果值/可能为变量/元素
     * @param type        变量/元素/固定值
     * @param valueType   STRING/NUMBER...
     * @param elementMap  用到的元素
     * @param variableMap 用到的变量
     * @return Action
     */
    public ConfigBean.Value getAction(String value, Integer type, String valueType, Map<Integer, RuleEngineElement> elementMap, Map<Integer, RuleEngineVariable> variableMap) {
        if (Validator.isEmpty(type)) {
            return null;
        }
        String valueName = value;
        ConfigBean.Value action = new ConfigBean.Value();
        if (type.equals(VariableType.ELEMENT.getType())) {
            valueName = elementMap.get(Integer.valueOf(value)).getName();
        } else if (type.equals(VariableType.VARIABLE.getType())) {
            RuleEngineVariable engineVariable = variableMap.get(Integer.valueOf(value));
            valueName = engineVariable.getName();
            if (engineVariable.getType().equals(VariableType.CONSTANT.getType())) {
                action.setVariableValue(engineVariable.getValue());
            }
        }
        action.setValue(value);
        action.setValueName(valueName);
        action.setType(type);
        action.setValueType(valueType);
        return action;
    }

}
