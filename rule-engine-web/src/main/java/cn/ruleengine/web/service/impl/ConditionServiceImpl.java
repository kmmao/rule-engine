package cn.ruleengine.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.value.*;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.DeletedEnum;
import cn.ruleengine.web.service.ConditionService;
import cn.ruleengine.web.service.ValueResolve;
import cn.ruleengine.web.store.entity.RuleEngineCondition;
import cn.ruleengine.web.store.entity.RuleEngineConditionGroupCondition;
import cn.ruleengine.web.store.entity.RuleEngineElement;
import cn.ruleengine.web.store.entity.RuleEngineVariable;
import cn.ruleengine.web.store.manager.RuleEngineConditionGroupConditionManager;
import cn.ruleengine.web.store.manager.RuleEngineConditionManager;
import cn.ruleengine.web.store.manager.RuleEngineElementManager;
import cn.ruleengine.web.store.manager.RuleEngineVariableManager;
import cn.ruleengine.web.store.mapper.RuleEngineConditionMapper;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageBase;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.base.response.Rows;
import cn.ruleengine.web.vo.condition.*;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.variable.ParamValue;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.ruleengine.core.Configuration;
import cn.ruleengine.core.DefaultInput;
import cn.ruleengine.core.Engine;
import cn.ruleengine.core.Input;
import cn.ruleengine.core.condition.Condition;
import cn.ruleengine.core.condition.Operator;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.core.rule.Rule;
import cn.ruleengine.web.exception.ApiException;
import cn.ruleengine.web.vo.workspace.Workspace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Slf4j
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
    private RuleEngineConditionGroupConditionManager ruleEngineConditionGroupConditionManager;
    @Resource
    private ValueResolve valueResolve;
    @Resource
    private Engine engine;


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
        Workspace workspace = Context.getCurrentWorkspace();
        RuleEngineCondition condition = new RuleEngineCondition();
        UserData userData = Context.getCurrentUser();
        condition.setCreateUserId(userData.getId());
        condition.setCreateUserName(userData.getUsername());
        condition.setName(addConditionRequest.getName());
        condition.setDescription(addConditionRequest.getDescription());
        // 条件配置信息
        this.configBeanCopyToCondition(condition, addConditionRequest.getConfig());
        condition.setWorkspaceId(workspace.getId());
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
        Workspace workspace = Context.getCurrentWorkspace();
        Integer count = this.ruleEngineConditionManager.lambdaQuery()
                .eq(RuleEngineCondition::getWorkspaceId, workspace.getId())
                .eq(RuleEngineCondition::getName, name)
                .count();
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
        RuleEngineCondition condition = this.ruleEngineConditionManager.lambdaQuery()
                .eq(RuleEngineCondition::getId, id)
                .one();
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
        Workspace workspace = Context.getCurrentWorkspace();
        QueryWrapper<RuleEngineCondition> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RuleEngineCondition::getWorkspaceId, workspace.getId());
        // 查询的数据排序
        PageUtils.defaultOrder(orders, wrapper);
        ListConditionRequest query = pageRequest.getQuery();
        if (Validator.isNotEmpty(query.getName())) {
            wrapper.lambda().like(RuleEngineCondition::getName, query.getName());
        }
        IPage<RuleEngineCondition> iPage = this.ruleEngineConditionManager.page(new Page<>(page.getPageIndex(), page.getPageSize()), wrapper);
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

    /**
     * 获取条件中的变量
     *
     * @param ruleEngineConditions 条件信息
     * @return map
     */
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
                        .stream().collect(Collectors.toMap(RuleEngineVariable::getId, v -> v)))
                .orElse(new HashMap<>());
    }

    /**
     * 获取条件中的元素
     *
     * @param ruleEngineConditions 条件信息
     * @return map
     */
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
                        .stream().collect(Collectors.toMap(RuleEngineElement::getId, v -> v)))
                .orElse(new HashMap<>());
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
        RuleEngineCondition ruleEngineCondition = this.ruleEngineConditionManager.lambdaQuery()
                .eq(RuleEngineCondition::getId, updateConditionRequest.getId())
                .one();
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
        // 更新引用此规则的条件到待发布
        this.ruleEngineConditionMapper.updateRuleWaitPublish(conditionId);
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
        Integer count = ruleEngineConditionGroupConditionManager.lambdaQuery()
                .eq(RuleEngineConditionGroupCondition::getConditionId, id)
                .count();
        if (count != null && count > 0) {
            throw new ValidException("有规则在引用此条件，无法删除");
        }
        return this.ruleEngineConditionManager.removeById(id);
    }

    /**
     * 根据id获取条件中的元素
     *
     * @param id 条件id
     * @return list
     */
    @Override
    public Set<Rule.Parameter> getParameter(Integer id) {
        Workspace workspace = Context.getCurrentWorkspace();
        RuleEngineCondition ruleEngineCondition = this.ruleEngineConditionManager.lambdaQuery()
                .eq(RuleEngineCondition::getId, id)
                .eq(RuleEngineCondition::getWorkspaceId, workspace.getId())
                .one();
        if (ruleEngineCondition == null) {
            throw new ValidException("规则条件找不到：{}", id);
        }
        Set<Integer> elementIds = new HashSet<>();
        // 左边的
        this.conditionAllElementId(elementIds, ruleEngineCondition.getLeftType(), ruleEngineCondition.getLeftValue());
        // 右边的
        this.conditionAllElementId(elementIds, ruleEngineCondition.getRightType(), ruleEngineCondition.getRightValue());
        if (CollUtil.isEmpty(elementIds)) {
            return Collections.emptySet();
        }
        List<RuleEngineElement> ruleEngineElements = this.ruleEngineElementManager.lambdaQuery().in(RuleEngineElement::getId, elementIds)
                .eq(RuleEngineElement::getWorkspaceId, workspace.getId()).list();
        if (CollUtil.isEmpty(ruleEngineElements)) {
            return Collections.emptySet();
        }
        Set<Rule.Parameter> parameters = new HashSet<>(ruleEngineElements.size());
        for (RuleEngineElement ruleEngineElement : ruleEngineElements) {
            Rule.Parameter parameter = new Rule.Parameter();
            parameter.setName(ruleEngineElement.getName());
            parameter.setCode(ruleEngineElement.getCode());
            parameter.setValueType(ruleEngineElement.getValueType());
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * 测试运行条件
     *
     * @param executeCondition 参数
     * @return true/false
     */
    @Override
    public Boolean run(ExecuteConditionRequest executeCondition) {
        Integer conditionId = executeCondition.getId();
        RuleEngineCondition ruleEngineCondition = this.ruleEngineConditionManager.getById(conditionId);
        if (ruleEngineCondition == null) {
            throw new ValidException("规则条件找不到：{}", conditionId);
        }
        List<ParamValue> paramValues = executeCondition.getParamValues();
        Input input = new DefaultInput();
        if (CollUtil.isNotEmpty(paramValues)) {
            for (ParamValue paramValue : paramValues) {
                input.put(paramValue.getCode(), paramValue.getValue());
            }
        }
        Configuration configuration = new Configuration();
        configuration.setEngineVariable(this.engine.getEngineVariable());
        Condition condition = new Condition();
        condition.setId(ruleEngineCondition.getId());
        condition.setName(ruleEngineCondition.getName());
        condition.setLeftValue(this.valueResolve.getValue(ruleEngineCondition.getLeftType(), ruleEngineCondition.getLeftValueType(), ruleEngineCondition.getLeftValue()));
        condition.setOperator(Operator.getByName(ruleEngineCondition.getSymbol()));
        condition.setRightValue(this.valueResolve.getValue(ruleEngineCondition.getRightType(), ruleEngineCondition.getRightValueType(), ruleEngineCondition.getRightValue()));
        Condition.verify(condition);
        return condition.compare(input, configuration);
    }

    /**
     * 获取条件中所有的元素
     *
     * @param elementIds 元素id
     * @param type       值类型
     * @param value      value
     */
    private void conditionAllElementId(Set<Integer> elementIds, Integer type, String value) {
        if (VariableType.VARIABLE.getType().equals(type)) {
            Value val = this.engine.getEngineVariable().getVariable(Integer.valueOf(value));
            if (val instanceof Function) {
                Function function = (Function) val;
                Map<String, Value> param = function.getParam();
                Collection<Value> values = param.values();
                for (Value v : values) {
                    if (v instanceof Element) {
                        elementIds.add(((Element) v).getElementId());
                    } else if (v instanceof Variable) {
                        try {
                            String varId = ((Variable) v).getVariableId().toString();
                            this.conditionAllElementId(elementIds, VariableType.VARIABLE.getType(), varId);
                            // 后面改为最大引用链不超过20，否则报错
                        } catch (StackOverflowError e) {
                            log.error("堆栈溢出错误", e);
                            throw new ValidException("请检查规则变量是否存在循环引用");
                        }
                    }
                }
            }
        } else if (VariableType.ELEMENT.getType().equals(type)) {
            elementIds.add(Integer.valueOf(value));
        }
    }

}
