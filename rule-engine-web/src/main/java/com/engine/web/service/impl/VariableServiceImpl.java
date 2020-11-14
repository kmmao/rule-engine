package com.engine.web.service.impl;

import com.engine.web.store.entity.RuleEngineFunction;
import com.engine.web.store.manager.*;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.engine.core.exception.ValidException;
import com.engine.core.value.VariableType;
import com.engine.web.config.rabbit.RabbitTopicConfig;
import com.engine.web.enums.DeletedEnum;
import com.engine.web.service.VariableService;
import com.engine.web.store.entity.RuleEngineFunctionValue;
import com.engine.web.store.entity.RuleEngineRule;
import com.engine.web.store.entity.RuleEngineVariable;
import com.engine.web.store.mapper.RuleEngineVariableMapper;
import com.engine.web.util.PageUtils;
import com.engine.web.vo.base.request.PageRequest;
import com.engine.web.vo.base.response.PageBase;
import com.engine.web.vo.base.response.PageResult;
import com.engine.web.vo.variable.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
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
@Service
public class VariableServiceImpl implements VariableService {

    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineFunctionValueManager ruleEngineFunctionValueManager;
    @Resource
    private RuleEngineFunctionManager ruleEngineFunctionManager;
    @Resource
    private RuleEngineVariableMapper ruleEngineVariableMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RuleEngineElementManager ruleEngineElementManager;
    @Resource
    private RuleCountInfoService ruleCountInfoService;
    @Resource
    private ApplicationContext applicationContext;

    @Override
    public Boolean add(AddVariableRequest addConditionRequest) {
        if (this.varNameIsExists(addConditionRequest.getName())) {
            throw new ValidException("变量名称：{}已经存在", addConditionRequest.getName());
        }
        RuleEngineVariable engineVariable = new RuleEngineVariable();
        engineVariable.setName(addConditionRequest.getName());
        engineVariable.setDescription(addConditionRequest.getDescription());
        engineVariable.setValueType(addConditionRequest.getValueType());
        engineVariable.setValue(addConditionRequest.getValue());
        engineVariable.setType(addConditionRequest.getType());
        engineVariable.setDeleted(DeletedEnum.ENABLE.getStatus());
        this.ruleEngineVariableManager.save(engineVariable);
        if (addConditionRequest.getType().equals(VariableType.FUNCTION.getType())) {
            // 保存函数参数值
            List<ParamValue> paramValues = addConditionRequest.getParamValues();
            RuleEngineFunction ruleEngineFunction = this.getCurrentFunction(engineVariable.getValue());
            this.saveFunctionParamValues(ruleEngineFunction.getId(), engineVariable.getId(), paramValues);
        }
        // 通知加载变量
        VariableMessageVo variableMessageVo = new VariableMessageVo();
        variableMessageVo.setType(VariableMessageVo.Type.LOAD);
        variableMessageVo.setId(engineVariable.getId());
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.VAR_EXCHANGE, RabbitTopicConfig.VAR_TOPIC_ROUTING_KEY, variableMessageVo);
        return true;
    }

    /**
     * 变量名称是否存在
     *
     * @param name 变量名称
     * @return true存在
     */
    @Override
    public Boolean varNameIsExists(String name) {
        Integer count = this.ruleEngineVariableManager.lambdaQuery().eq(RuleEngineVariable::getName, name).count();
        return count != null && count >= 1;
    }

    /**
     * 保存函数参数值
     *
     * @param functionId  函数id
     * @param variableId  变量id
     * @param paramValues 函数参数值
     */
    public void saveFunctionParamValues(Integer functionId, Integer variableId, List<ParamValue> paramValues) {
        List<RuleEngineFunctionValue> engineFunctionValues = paramValues.stream().map(m -> {
            RuleEngineFunctionValue engineFunctionValue = new RuleEngineFunctionValue();
            engineFunctionValue.setFunctionId(functionId);
            engineFunctionValue.setParamCode(m.getCode());
            engineFunctionValue.setParamName(m.getName());
            engineFunctionValue.setVariableId(variableId);
            engineFunctionValue.setType(m.getType());
            engineFunctionValue.setValueType(m.getValueType());
            engineFunctionValue.setValue(m.getValue());
            return engineFunctionValue;
        }).collect(Collectors.toList());
        this.ruleEngineFunctionValueManager.saveBatch(engineFunctionValues);
    }

    @Override
    public PageResult<ListVariableResponse> list(PageRequest<ListVariableRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        return PageUtils.page(ruleEngineVariableManager, page, () -> {
            QueryWrapper<RuleEngineVariable> wrapper = new QueryWrapper<>();

            PageUtils.defaultOrder(orders, wrapper);

            ListVariableRequest query = pageRequest.getQuery();
            if (Validator.isNotEmpty(query.getValueType())) {
                wrapper.lambda().eq(RuleEngineVariable::getValueType, query.getValueType());
            }
            if (Validator.isNotEmpty(query.getName())) {
                wrapper.lambda().like(RuleEngineVariable::getName, query.getName());
            }
            return wrapper;
        }, m -> {
            ListVariableResponse listVariableResponse = new ListVariableResponse();
            listVariableResponse.setId(m.getId());
            listVariableResponse.setName(m.getName());

            listVariableResponse.setValue(m.getValue());
            listVariableResponse.setType(m.getType());
            listVariableResponse.setValueType(m.getValueType());
            listVariableResponse.setDescription(m.getDescription());
            listVariableResponse.setCreateTime(m.getCreateTime());
            return listVariableResponse;
        });
    }

    /**
     * 变量分为固定值变量,函数变量
     *
     * @param id 变量id
     * @return var
     */
    @Override
    public GetVariableResponse get(Integer id) {
        RuleEngineVariable ruleEngineVariable = ruleEngineVariableManager.getById(id);
        if (ruleEngineVariable == null) {
            return null;
        }
        GetVariableResponse variableResponse = new GetVariableResponse();
        BeanUtil.copyProperties(ruleEngineVariable, variableResponse);
        if (ruleEngineVariable.getType().equals(VariableType.CONSTANT.getType())) {
            return variableResponse;
        } else if (ruleEngineVariable.getType().equals(VariableType.FUNCTION.getType())) {
            String functionId = ruleEngineVariable.getValue();
            RuleEngineFunction engineFunction = ruleEngineFunctionManager.getById(functionId);
            GetVariableResponse.Function function = new GetVariableResponse.Function();
            function.setId(engineFunction.getId());
            function.setName(engineFunction.getName());
            function.setReturnValueType(engineFunction.getReturnValueType());
            // 处理函数入参值
            List<RuleEngineFunctionValue> functionValues = ruleEngineFunctionValueManager.lambdaQuery()
                    .eq(RuleEngineFunctionValue::getVariableId, id)
                    .eq(RuleEngineFunctionValue::getFunctionId, functionId).list();
            List<ParamValue> paramValueList = functionValues.stream().map(m -> {
                ParamValue paramValue = new ParamValue();
                paramValue.setName(m.getParamName());
                paramValue.setCode(m.getParamCode());
                paramValue.setType(m.getType());
                paramValue.setValue(m.getValue());
                paramValue.setValueType(m.getValueType());
                String valueName = m.getValue();
                if (VariableType.ELEMENT.getType().equals(m.getType())) {
                    valueName = ruleEngineElementManager.getById(m.getValue()).getName();
                } else if (VariableType.VARIABLE.getType().equals(m.getType())) {
                    RuleEngineVariable engineVariable = ruleEngineVariableManager.getById(m.getValue());
                    valueName = engineVariable.getName();
                }
                paramValue.setValueName(valueName);
                return paramValue;
            }).collect(Collectors.toList());
            function.setParamValues(paramValueList);
            variableResponse.setFunction(function);
        }
        return variableResponse;
    }

    @Override
    public Boolean update(UpdateVariableRequest updateVariableRequest) {
        RuleEngineVariable ruleEngineVariable = this.ruleEngineVariableManager.getById(updateVariableRequest.getId());
        if (ruleEngineVariable == null) {
            throw new ValidException("找不到变量：{}", updateVariableRequest.getId());
        }
        if (!updateVariableRequest.getName().equals(ruleEngineVariable.getName())) {
            if (this.varNameIsExists(updateVariableRequest.getName())) {
                throw new ValidException("变量名称：{}已经存在", updateVariableRequest.getName());
            }
        }
        RuleEngineVariable engineVariable = new RuleEngineVariable();
        engineVariable.setId(updateVariableRequest.getId());
        engineVariable.setName(updateVariableRequest.getName());
        engineVariable.setDescription(updateVariableRequest.getDescription());
        engineVariable.setValue(updateVariableRequest.getValue());
        engineVariable.setType(updateVariableRequest.getType());
        this.ruleEngineVariableManager.updateById(engineVariable);
        // 函数信息
        if (updateVariableRequest.getType().equals(VariableType.FUNCTION.getType())) {
            // 删除原有的函数值信息
            this.ruleEngineFunctionValueManager.lambdaUpdate()
                    .eq(RuleEngineFunctionValue::getFunctionId, updateVariableRequest.getValue())
                    .eq(RuleEngineFunctionValue::getVariableId, engineVariable.getId())
                    .remove();
            RuleEngineFunction ruleEngineFunction = this.getCurrentFunction(engineVariable.getValue());
            // 保存函数参数值
            List<ParamValue> paramValues = updateVariableRequest.getParamValues();
            this.saveFunctionParamValues(ruleEngineFunction.getId(), engineVariable.getId(), paramValues);
        }
        // 刷新引用此规则的统计信息
        this.ruleCountInfoService.refreshByVarId(updateVariableRequest.getId());
        // 通知加载变量
        VariableMessageVo variableMessageVo = new VariableMessageVo();
        variableMessageVo.setType(VariableMessageVo.Type.UPDATE);
        variableMessageVo.setId(engineVariable.getId());
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.VAR_EXCHANGE, RabbitTopicConfig.VAR_TOPIC_ROUTING_KEY, variableMessageVo);
        return true;
    }

    public RuleEngineFunction getCurrentFunction(String functionId) {
        RuleEngineFunction engineFunction = this.ruleEngineFunctionManager.getById(functionId);
        if (engineFunction == null) {
            throw new ValidException("函数不存在");
        }
        String executor = engineFunction.getExecutor();
        if (!applicationContext.containsBean(executor)) {
            throw new ValidException("系统中找不到:{}函数Bean", executor);
        }
        return engineFunction;
    }

    @Override
    public Boolean delete(Integer id) {
        List<RuleEngineRule> ruleEngineRules = ruleEngineVariableMapper.countRule(id);
        if (CollUtil.isNotEmpty(ruleEngineRules)) {
            throw new ValidException("有规则在引用此变量，无法删除");
        }
        VariableMessageVo variableMessageVo = new VariableMessageVo();
        variableMessageVo.setType(VariableMessageVo.Type.REMOVE);
        variableMessageVo.setId(id);
        this.rabbitTemplate.convertAndSend(RabbitTopicConfig.VAR_EXCHANGE, RabbitTopicConfig.VAR_TOPIC_ROUTING_KEY, variableMessageVo);
        return ruleEngineVariableManager.removeById(id);
    }

}
