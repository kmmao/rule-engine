package com.engine.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.engine.core.exception.ValidException;
import com.engine.core.value.VariableType;
import com.engine.web.enums.DeletedEnum;
import com.engine.web.enums.RuleStatus;
import com.engine.web.exception.ApiException;
import com.engine.web.store.entity.RuleEngineCondition;
import com.engine.web.store.entity.RuleEngineElement;
import com.engine.web.store.entity.RuleEngineRule;
import com.engine.web.store.entity.RuleEngineVariable;
import com.engine.web.store.manager.RuleEngineRuleManager;
import com.engine.web.store.manager.RuleEngineVariableManager;
import com.engine.web.store.mapper.RuleEngineConditionMapper;
import com.engine.web.util.PageUtils;
import com.engine.web.vo.base.request.PageRequest;
import com.engine.web.vo.base.response.PageBase;
import com.engine.web.vo.base.response.PageResult;
import com.engine.web.service.ConditionService;
import com.engine.web.store.manager.RuleEngineConditionManager;
import com.engine.web.store.manager.RuleEngineElementManager;
import com.engine.web.vo.base.response.Rows;
import com.engine.web.vo.condition.*;
import org.springframework.stereotype.Component;
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
 * @date 2020/7/14
 * @since 1.0.0
 */
@Transactional(rollbackFor = Exception.class)
@Component
public class ConditionServiceImpl implements ConditionService {

    @Resource
    private RuleEngineConditionManager ruleEngineConditionManager;
    @Resource
    private RuleEngineConditionMapper ruleEngineConditionMapper;
    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineElementManager ruleEngineElementManager;
    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;

    /**
     * 保存条件
     *
     * @param addConditionRequest 条件配置信息
     * @return true
     */
    @Override
    public Boolean save(AddConditionRequest addConditionRequest) {
        if (this.conditionNameIsExists(addConditionRequest.getName())) {
            throw new ValidException("条件名称：{}已经存在", addConditionRequest.getName());
        }
        RuleEngineCondition condition = new RuleEngineCondition();
        condition.setName(addConditionRequest.getName());
        condition.setDescription(addConditionRequest.getDescription());
        // 条件配置信息
        configBeanCopyToCondition(condition, addConditionRequest.getConfig());
        condition.setDeleted(DeletedEnum.ENABLE.getStatus());
        return ruleEngineConditionManager.save(condition);
    }

    /**
     * 条件名称是否存在
     *
     * @param name 条件名称
     * @return true存在
     */
    @Override
    public Boolean conditionNameIsExists(String name) {
        Integer count = this.ruleEngineConditionManager.lambdaQuery().eq(RuleEngineCondition::getName, name).count();
        return count != null && count >= 1;
    }

    /**
     * 根绝id查询条件信息
     *
     * @param id 条件id
     * @return ConditionResponse
     */
    @Override
    public ConditionResponse getById(Integer id) {
        RuleEngineCondition condition = ruleEngineConditionManager.getById(id);
        if (condition == null) {
            throw new ApiException("根据Id:{},没有查询到数据", id);
        }
        return getConditionResponse(condition);
    }

    /**
     * 条件转换
     *
     * @param engineCondition engineCondition
     * @return ConditionResponse
     */
    @Override
    public ConditionResponse getConditionResponse(RuleEngineCondition engineCondition) {
        ConditionResponse conditionResponse = new ConditionResponse();

        conditionResponse.setDescription(engineCondition.getDescription());
        conditionResponse.setId(engineCondition.getId());
        conditionResponse.setName(engineCondition.getName());

        ConfigBean configBean = getConfigBean(engineCondition);
        conditionResponse.setConfig(configBean);
        return conditionResponse;
    }

    /**
     * 条件转换
     *
     * @param engineCondition engineCondition
     * @param variableMap     条件用到的变量
     * @param elementMap      条件用到的元素
     * @return ConditionResponse
     */
    @Override
    public ConditionResponse getConditionResponse(RuleEngineCondition engineCondition, Map<Integer, RuleEngineVariable> variableMap, Map<Integer, RuleEngineElement> elementMap) {
        ConditionResponse conditionResponse = new ConditionResponse();

        conditionResponse.setDescription(engineCondition.getDescription());
        conditionResponse.setId(engineCondition.getId());
        conditionResponse.setName(engineCondition.getName());

        ConfigBean configBean = getConfigBean(engineCondition, variableMap, elementMap);
        conditionResponse.setConfig(configBean);
        return conditionResponse;
    }

    public ConfigBean getConfigBean(RuleEngineCondition engineCondition) {
        ConfigBean configBean = new ConfigBean();

        ConfigBean.Value leftValue = getConfigBeanValue(engineCondition.getLeftType(), engineCondition.getLeftValue(), engineCondition.getLeftValueType());
        configBean.setLeftValue(leftValue);

        configBean.setSymbol(engineCondition.getSymbol());

        ConfigBean.Value rightValue = getConfigBeanValue(engineCondition.getRightType(), engineCondition.getRightValue(), engineCondition.getRightValueType());
        configBean.setRightValue(rightValue);
        return configBean;
    }

    /**
     * 如果是变量，查询到变量name，如果是元素查询到元素name
     *
     * @param type      类型 变量/元素/固定值
     * @param value     值
     * @param valueType 值类型 STRING/NUMBER...
     * @return ConfigBean.Value
     */
    public ConfigBean.Value getConfigBeanValue(Integer type, String value, String valueType) {
        String valueName = value;
        String variableValue = null;
        if (type.equals(VariableType.ELEMENT.getType())) {
            valueName = ruleEngineElementManager.getById(value).getName();
        } else if (type.equals(VariableType.VARIABLE.getType())) {
            RuleEngineVariable engineVariable = ruleEngineVariableManager.getById(value);
            valueName = engineVariable.getName();
            if (engineVariable.getType().equals(VariableType.CONSTANT.getType())) {
                variableValue = engineVariable.getValue();
            }
        }
        ConfigBean.Value configBeanValue = new ConfigBean.Value();
        configBeanValue.setType(type);
        configBeanValue.setValue(value);
        configBeanValue.setValueName(valueName);
        configBeanValue.setVariableValue(variableValue);
        configBeanValue.setValueType(valueType);
        return configBeanValue;
    }

    /**
     * 条件列表
     *
     * @param pageRequest 分页查询信息
     * @return page
     */
    @Override
    public PageResult<ListConditionResponse> list(PageRequest<ListConditionRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        QueryWrapper<RuleEngineCondition> wrapper = new QueryWrapper<>();
        // 查询的数据排序
        PageUtils.defaultOrder(orders, wrapper);
        ListConditionRequest query = pageRequest.getQuery();
        if (Validator.isNotEmpty(query.getName())) {
            wrapper.lambda().like(RuleEngineCondition::getName, query.getName());
        }
        IPage<RuleEngineCondition> iPage = ruleEngineConditionManager.page(new Page<>(page.getPageIndex(), page.getPageSize()), wrapper);
        List<RuleEngineCondition> engineConditions = iPage.getRecords();
        PageResult<ListConditionResponse> pageResult = new PageResult<>();
        if (CollUtil.isEmpty(engineConditions)) {
            return pageResult;
        }
        Map<Integer, RuleEngineElement> elementMap = this.getConditionElementMap(engineConditions);
        Map<Integer, RuleEngineVariable> variableMap = this.getConditionVariableMap(engineConditions);
        // 类型转换处理
        List<ListConditionResponse> conditionResponses = engineConditions.stream().map(m -> {
            ListConditionResponse listConditionResponse = new ListConditionResponse();
            listConditionResponse.setId(m.getId());
            listConditionResponse.setName(m.getName());
            listConditionResponse.setDescription(m.getDescription());
            listConditionResponse.setCreateTime(m.getCreateTime());

            ConfigBean configBean = getConfigBean(m, variableMap, elementMap);
            listConditionResponse.setConfig(configBean);
            listConditionResponse.setConfigInfo(String.format("%s %s %s", configBean.getLeftValue().getValueName(), m.getSymbol(), configBean.getRightValue().getValueName()));
            return listConditionResponse;
        }).collect(Collectors.toList());
        pageResult.setData(new Rows<>(conditionResponses, PageUtils.getPageResponse(iPage)));
        return pageResult;
    }

    public ConfigBean getConfigBean(RuleEngineCondition m, Map<Integer, RuleEngineVariable> variableMap, Map<Integer, RuleEngineElement> elementMap) {
        ConfigBean configBean = new ConfigBean();

        ConfigBean.Value leftValue = getConfigBeanValue(m.getLeftType(), m.getLeftValue(), m.getLeftValueType(), variableMap, elementMap);
        configBean.setLeftValue(leftValue);

        configBean.setSymbol(m.getSymbol());

        ConfigBean.Value rightValue = getConfigBeanValue(m.getRightType(), m.getRightValue(), m.getRightValueType(), variableMap, elementMap);
        configBean.setRightValue(rightValue);
        return configBean;
    }

    @Override
    public Map<Integer, RuleEngineVariable> getConditionVariableMap(Collection<RuleEngineCondition> ruleEngineConditions) {
        // 获取条件中的所有的变量数据
        Set<String> variableIds = new HashSet<>();
        for (RuleEngineCondition engineCondition : ruleEngineConditions) {
            Integer rightType = engineCondition.getRightType();
            if (rightType.equals(VariableType.VARIABLE.getType())) {
                variableIds.add(engineCondition.getRightValue());
            }
            Integer leftType = engineCondition.getLeftType();
            if (leftType.equals(VariableType.VARIABLE.getType())) {
                variableIds.add(engineCondition.getLeftValue());
            }
        }
        return Optional.of(variableIds).filter(CollUtil::isNotEmpty)
                .map(m -> ruleEngineVariableManager.lambdaQuery().in(RuleEngineVariable::getId, m).list()
                        .stream().collect(Collectors.toMap(RuleEngineVariable::getId, Function.identity()))).orElse(new HashMap<>());
    }

    @Override
    public Map<Integer, RuleEngineElement> getConditionElementMap(Collection<RuleEngineCondition> ruleEngineConditions) {
        Set<String> elementIds = new HashSet<>();
        for (RuleEngineCondition engineCondition : ruleEngineConditions) {
            Integer rightType = engineCondition.getRightType();
            if (rightType.equals(VariableType.ELEMENT.getType())) {
                elementIds.add(engineCondition.getRightValue());
            }
            Integer leftType = engineCondition.getLeftType();
            if (leftType.equals(VariableType.ELEMENT.getType())) {
                elementIds.add(engineCondition.getLeftValue());
            }
        }
        return Optional.of(elementIds).filter(CollUtil::isNotEmpty)
                .map(m -> ruleEngineElementManager.lambdaQuery().in(RuleEngineElement::getId, m).list()
                        .stream().collect(Collectors.toMap(RuleEngineElement::getId, Function.identity()))).orElse(new HashMap<>());
    }

    /**
     * 如果是变量，查询到变量name，如果是元素查询到元素name
     *
     * @param type        类型 变量/元素/固定值
     * @param value       值
     * @param valueType   值类型 STRING/NUMBER...
     * @param variableMap 变量缓存
     * @param elementMap  元素缓存
     * @return ConfigBean.Value
     */
    public ConfigBean.Value getConfigBeanValue(Integer type, String value, String valueType, Map<Integer, RuleEngineVariable> variableMap, Map<Integer, RuleEngineElement> elementMap) {
        String valueName = value;
        String variableValue = null;
        if (type.equals(VariableType.ELEMENT.getType())) {
            valueName = elementMap.get(Integer.valueOf(value)).getName();
        } else if (type.equals(VariableType.VARIABLE.getType())) {
            RuleEngineVariable engineVariable = variableMap.get(Integer.valueOf(value));
            valueName = engineVariable.getName();
            if (engineVariable.getType().equals(VariableType.CONSTANT.getType())) {
                variableValue = engineVariable.getValue();
            }
        }
        ConfigBean.Value configBeanValue = new ConfigBean.Value();
        configBeanValue.setType(type);
        configBeanValue.setValue(value);
        configBeanValue.setValueName(valueName);
        configBeanValue.setVariableValue(variableValue);
        configBeanValue.setValueType(valueType);
        return configBeanValue;
    }

    /**
     * 更新条件
     *
     * @param updateConditionRequest 更新条件
     * @return true
     */
    @Override
    public Boolean update(UpdateConditionRequest updateConditionRequest) {
        RuleEngineCondition ruleEngineCondition = this.ruleEngineConditionManager.getById(updateConditionRequest.getId());
        if (ruleEngineCondition == null) {
            throw new ValidException("规则条件找不到：{}", updateConditionRequest.getId());
        }
        if (!ruleEngineCondition.getName().equals(updateConditionRequest.getName())) {
            if (this.conditionNameIsExists(updateConditionRequest.getName())) {
                throw new ValidException("条件名称：{}已经存在", updateConditionRequest.getName());
            }
        }
        Integer conditionId = updateConditionRequest.getId();
        // 更新条件
        RuleEngineCondition condition = new RuleEngineCondition();
        condition.setId(conditionId);
        condition.setName(updateConditionRequest.getName());
        condition.setDescription(updateConditionRequest.getDescription());
        // 条件配置信息
        configBeanCopyToCondition(condition, updateConditionRequest.getConfig());
        this.ruleEngineConditionManager.updateById(condition);
        // 规则重新发布
        // TODO: 2020/11/15  ....
        List<RuleEngineRule> ruleEngineRules = null;
        if (CollUtil.isNotEmpty(ruleEngineRules)) {
            List<RuleEngineRule> updateRuleEngineRule = ruleEngineRules.stream()
                    .filter(f -> f.getStatus().equals(RuleStatus.PUBLISHED.getStatus()))
                    .peek(p -> p.setStatus(RuleStatus.WAIT_PUBLISH.getStatus()))
                    .collect(Collectors.toList());
            // 条件的修改触发，如果存在的是已发布版本需要更新为待发布
            if (CollUtil.isNotEmpty(updateRuleEngineRule)) {
                this.ruleEngineRuleManager.updateBatchById(updateRuleEngineRule);
            }
        }
        return true;
    }

    /**
     * 从ConfigBean信息复制到RuleEngineCondition
     *
     * @param condition RuleEngineCondition
     * @param config    请求的条件配置数据
     */
    private void configBeanCopyToCondition(RuleEngineCondition condition, ConfigBean config) {
        ConfigBean.Value leftValue = config.getLeftValue();
        condition.setLeftValueType(leftValue.getValueType());
        condition.setLeftType(leftValue.getType());
        condition.setLeftValue(leftValue.getValue());
        String symbol = config.getSymbol();
        condition.setSymbol(symbol);
        ConfigBean.Value rightValue = config.getRightValue();
        condition.setRightValueType(rightValue.getValueType());
        condition.setRightType(rightValue.getType());
        condition.setRightValue(rightValue.getValue());
    }

    /**
     * 删除条件
     *
     * @param id 条件id
     * @return true：删除成功
     */
    @Override
    public Boolean delete(Integer id) {
        // TODO: 2020/11/15 ...
        List<RuleEngineRule> ruleEngineRules = null;
        if (CollUtil.isNotEmpty(ruleEngineRules)) {
            throw new ValidException("有规则在引用此条件，无法删除");
        }
        return this.ruleEngineConditionManager.removeById(id);
    }

}
