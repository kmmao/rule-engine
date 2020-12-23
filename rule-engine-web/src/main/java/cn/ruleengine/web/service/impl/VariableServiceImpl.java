package cn.ruleengine.web.service.impl;


import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.DeletedEnum;
import cn.ruleengine.web.listener.body.VariableMessageBody;
import cn.ruleengine.web.listener.event.VariableEvent;
import cn.ruleengine.web.service.VariableService;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import cn.ruleengine.web.store.mapper.RuleEngineVariableMapper;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageBase;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.variable.*;

import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.core.value.VariableType;
import cn.ruleengine.web.vo.workspace.Workspace;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
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
    private RuleEngineElementManager ruleEngineElementManager;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private RuleEngineConditionManager ruleEngineConditionManager;
    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 添加变量
     *
     * @param addConditionRequest 变量信息
     * @return true
     */
    @Override
    public Boolean add(AddVariableRequest addConditionRequest) {
        if (this.varNameIsExists(addConditionRequest.getName())) {
            throw new ValidException("变量名称：{}已经存在", addConditionRequest.getName());
        }
        RuleEngineVariable engineVariable = new RuleEngineVariable();
        UserData userData = Context.getCurrentUser();
        engineVariable.setCreateUserId(userData.getId());
        engineVariable.setCreateUserName(userData.getUsername());
        engineVariable.setName(addConditionRequest.getName());
        engineVariable.setDescription(addConditionRequest.getDescription());
        engineVariable.setValueType(addConditionRequest.getValueType());
        engineVariable.setValue(addConditionRequest.getValue());
        engineVariable.setType(addConditionRequest.getType());
        engineVariable.setDeleted(DeletedEnum.ENABLE.getStatus());
        Workspace workspace = Context.getCurrentWorkspace();
        engineVariable.setWorkspaceId(workspace.getId());
        this.ruleEngineVariableManager.save(engineVariable);
        if (addConditionRequest.getType().equals(VariableType.FUNCTION.getType())) {
            // 保存函数参数值
            List<ParamValue> paramValues = addConditionRequest.getParamValues();
            RuleEngineFunction ruleEngineFunction = this.getCurrentFunction(engineVariable.getValue());
            this.saveFunctionParamValues(ruleEngineFunction.getId(), engineVariable, paramValues);
        }
        // 通知加载变量
        VariableMessageBody variableMessageBody = new VariableMessageBody();
        variableMessageBody.setType(VariableMessageBody.Type.LOAD);
        variableMessageBody.setId(engineVariable.getId());
        this.eventPublisher.publishEvent(new VariableEvent(variableMessageBody));
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
        Workspace workspace = Context.getCurrentWorkspace();
        Integer count = this.ruleEngineVariableManager.lambdaQuery()
                .eq(RuleEngineVariable::getWorkspaceId, workspace.getId())
                .eq(RuleEngineVariable::getName, name).count();
        return count != null && count >= 1;
    }

    /**
     * 保存函数参数值
     *
     * @param functionId     函数id
     * @param engineVariable 变量
     * @param paramValues    函数参数值
     */
    public void saveFunctionParamValues(Integer functionId, RuleEngineVariable engineVariable, List<ParamValue> paramValues) {
        List<RuleEngineFunctionValue> engineFunctionValues = paramValues.stream().map(m -> {
            // 变量自己不能引用自己
            if (Objects.equals(engineVariable.getName(), m.getName())) {
                throw new ValidException("变量不可以引用自身");
            }
            RuleEngineFunctionValue engineFunctionValue = new RuleEngineFunctionValue();
            engineFunctionValue.setFunctionId(functionId);
            engineFunctionValue.setParamCode(m.getCode());
            engineFunctionValue.setParamName(m.getName());
            engineFunctionValue.setVariableId(engineVariable.getId());
            engineFunctionValue.setType(m.getType());
            engineFunctionValue.setValueType(m.getValueType());
            engineFunctionValue.setValue(m.getValue());
            return engineFunctionValue;
        }).collect(Collectors.toList());
        this.ruleEngineFunctionValueManager.saveBatch(engineFunctionValues);
    }

    /**
     * 变量列表
     *
     * @param pageRequest param
     * @return result
     */
    @Override
    public PageResult<ListVariableResponse> list(PageRequest<ListVariableRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        Workspace workspace = Context.getCurrentWorkspace();
        return PageUtils.page(ruleEngineVariableManager, page, () -> {
            QueryWrapper<RuleEngineVariable> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(RuleEngineVariable::getWorkspaceId, workspace.getId());
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
        RuleEngineVariable ruleEngineVariable = this.ruleEngineVariableManager.lambdaQuery()
                .eq(RuleEngineVariable::getId, id)
                .one();
        if (ruleEngineVariable == null) {
            return null;
        }
        GetVariableResponse variableResponse = BasicConversion.INSTANCE.convert(ruleEngineVariable);
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

    /**
     * 根据id更新变量
     *
     * @param updateVariableRequest param
     * @return true
     */
    @Override
    public Boolean update(UpdateVariableRequest updateVariableRequest) {
        RuleEngineVariable ruleEngineVariable = this.ruleEngineVariableManager.lambdaQuery()
                .eq(RuleEngineVariable::getId, updateVariableRequest.getId())
                .one();
        if (ruleEngineVariable == null) {
            throw new ValidException("找不到更新的变量：{}", updateVariableRequest.getId());
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
            this.saveFunctionParamValues(ruleEngineFunction.getId(), engineVariable, paramValues);
        }
        // 通知加载变量
        VariableMessageBody variableMessageBody = new VariableMessageBody();
        variableMessageBody.setType(VariableMessageBody.Type.UPDATE);
        variableMessageBody.setId(engineVariable.getId());
        this.eventPublisher.publishEvent(new VariableEvent(variableMessageBody));
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

    /**
     * 根据id删除变量
     *
     * @param id 变量id
     * @return true
     */
    @Override
    public Boolean delete(Integer id) {
        RuleEngineVariable engineVariable = this.ruleEngineVariableManager.getById(id);
        if (engineVariable == null) {
            throw new ValidException("找不到要删除的变量：{}", id);
        }
        {
            Integer count = ruleEngineVariableMapper.countPublishRuleVar(id);
            if (count != null && count > 0) {
                throw new ValidException("有发布规则在引用此变量，无法删除");
            }
        }
        {
            Integer count = this.ruleEngineFunctionValueManager.lambdaQuery()
                    .eq(RuleEngineFunctionValue::getType, VariableType.VARIABLE.getType())
                    .eq(RuleEngineFunctionValue::getValue, id).count();
            if (count != null && count > 0) {
                throw new ValidException("有函数值在引用此变量，无法删除");
            }
        }
        {
            Integer count = this.ruleEngineRuleManager.lambdaQuery()
                    .and(a -> a.eq(RuleEngineRule::getActionType, VariableType.VARIABLE.getType()).eq(RuleEngineRule::getActionValue, id))
                    .or(o -> o.eq(RuleEngineRule::getDefaultActionType, VariableType.VARIABLE.getType()).eq(RuleEngineRule::getDefaultActionValue, id)).count();
            if (count != null && count > 0) {
                throw new ValidException("有规则在引用此变量，无法删除");
            }
        }
        {
            Integer count = ruleEngineConditionManager.lambdaQuery()
                    .and(a ->
                            a.eq(RuleEngineCondition::getLeftType, VariableType.VARIABLE.getType())
                                    .eq(RuleEngineCondition::getLeftValue, id)
                    ).or(o -> o.eq(RuleEngineCondition::getRightType, VariableType.VARIABLE.getType())
                            .eq(RuleEngineCondition::getRightValue, id)).count();
            if (count != null && count > 0) {
                throw new ValidException("有条件在引用此变量，无法删除");
            }
        }
        VariableMessageBody variableMessageBody = new VariableMessageBody();
        variableMessageBody.setType(VariableMessageBody.Type.REMOVE);
        variableMessageBody.setId(id);
        this.eventPublisher.publishEvent(new VariableEvent(variableMessageBody));
        // 删除变量函数值
        this.ruleEngineFunctionValueManager.lambdaUpdate()
                .eq(RuleEngineFunctionValue::getVariableId, engineVariable.getId())
                .remove();
        return ruleEngineVariableManager.removeById(id);
    }

}
