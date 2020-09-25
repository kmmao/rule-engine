package com.engine.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.engine.core.value.*;
import com.engine.web.enums.FunctionSource;
import com.engine.web.service.FunctionService;
import com.engine.web.service.VariableResolveService;
import com.engine.web.vo.common.DataCacheMap;
import com.engine.web.service.ValueResolve;
import com.engine.web.store.entity.RuleEngineFunction;
import com.engine.web.store.entity.RuleEngineFunctionValue;
import com.engine.web.store.entity.RuleEngineVariable;
import com.engine.web.store.manager.RuleEngineFunctionManager;
import com.engine.web.store.manager.RuleEngineFunctionValueManager;
import com.engine.web.store.manager.RuleEngineVariableManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/17
 * @since 1.0.0
 */
@Slf4j
@Service
public class VariableResolveServiceImpl implements VariableResolveService {

    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineFunctionValueManager ruleEngineFunctionValueManager;
    @Resource
    private RuleEngineFunctionManager ruleEngineFunctionManager;
    @Resource
    private ValueResolve valueResolve;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private FunctionService functionService;

    /**
     * 获取所有的变量/函数配置信息
     *
     * @return 变量
     */
    @Override
    public Map<Integer, Value> getAllVariable() {
        log.info("开始加载规则引擎变量");
        DataCacheMap cacheMap = valueResolve.getCacheMap();
        Map<Integer, Value> maps = new HashMap<>(100);
        //查询到所有的变量
        Collection<RuleEngineVariable> ruleEngineVariables = cacheMap.getVariableMap().values();
        for (RuleEngineVariable engineVariable : ruleEngineVariables) {
            Integer type = engineVariable.getType();
            if (VariableType.CONSTANT.getType().equals(type)) {
                Value value = valueResolve.getValue(VariableType.CONSTANT.getType(), engineVariable.getValueType(), engineVariable.getValue(), cacheMap);
                maps.put(engineVariable.getId(), value);
            } else if (VariableType.FUNCTION.getType().equals(type)) {
                maps.put(engineVariable.getId(), functionProcess(engineVariable, cacheMap));
            }
        }
        return maps;
    }


    /**
     * 根据变量获取变量/函数配置信息
     *
     * @param id 变量id
     * @return 变量
     */
    @Override
    public Value getVarById(Integer id) {
        RuleEngineVariable ruleEngineVariable = ruleEngineVariableManager.getById(id);
        if (Objects.equals(ruleEngineVariable.getType(), VariableType.CONSTANT.getType())) {
            return valueResolve.getValue(VariableType.CONSTANT.getType(), ruleEngineVariable.getValueType(), ruleEngineVariable.getValue());
        }
        RuleEngineFunction engineFunction = ruleEngineFunctionManager.getById(ruleEngineVariable.getValue());

        // 如果为用户上传的java 源码，则先解析后加载到Spring容器中
        if (FunctionSource.JAVA_CODE.getValue().equals(engineFunction.getSource())) {
            this.registerFunction(engineFunction);
        }

        List<RuleEngineFunctionValue> functionValueList = ruleEngineFunctionValueManager.lambdaQuery()
                .eq(RuleEngineFunctionValue::getFunctionId, ruleEngineVariable.getValue())
                .eq(RuleEngineFunctionValue::getVariableId, ruleEngineVariable.getId()).list();

        Map<String, Value> param = new HashMap<>(10);
        if (CollUtil.isNotEmpty(functionValueList)) {
            for (RuleEngineFunctionValue engineFunctionValue : functionValueList) {
                param.put(engineFunctionValue.getParamCode(), valueResolve.getValue(engineFunctionValue.getType(), engineFunctionValue.getValueType(), engineFunctionValue.getValue()));
            }
        }
        Object abstractFunction = applicationContext.getBean(engineFunction.getExecutor());
        return new Function(engineFunction.getName(), abstractFunction, DataType.getByValue(engineFunction.getReturnValueType()), param);

    }

    /**
     * 如果为用户上传的java 源码，则先解析后加载到Spring容器中
     *
     * @param engineFunction 函数信息
     */
    private void registerFunction(RuleEngineFunction engineFunction) {
        String executor = engineFunction.getExecutor();
        String functionJavaCode = engineFunction.getFunctionJavaCode();
        Class<?> clazz = this.functionService.functionTryCompiler(StrUtil.upperFirst(executor), functionJavaCode);
        // 如果不存在这个bean
        if (!applicationContext.containsBean(executor)) {
            // 加载到Spring容器中
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
            AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
            BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
            beanDefinitionRegistry.registerBeanDefinition(executor, beanDefinition);
        }
    }

    /**
     * 规则引擎函数处理
     *
     * @param ruleEngineVariable 规则函数元数据
     * @param cacheMap           配置缓存数据
     * @return Function
     */
    @Override
    public Function functionProcess(RuleEngineVariable ruleEngineVariable, DataCacheMap cacheMap) {
        Integer functionId = Integer.valueOf(ruleEngineVariable.getValue());
        RuleEngineFunction engineFunction = cacheMap.getFunctionMap().get(functionId);

        // 如果为用户上传的java 源码，则先解析后加载到Spring容器中
        if (FunctionSource.JAVA_CODE.getValue().equals(engineFunction.getSource())) {
            this.registerFunction(engineFunction);
        }

        List<RuleEngineFunctionValue> functionValueList = cacheMap.getFunctionValueMap().get(ruleEngineVariable.getId());
        Map<String, Value> param = new HashMap<>(10);
        if (CollUtil.isNotEmpty(functionValueList)) {
            for (RuleEngineFunctionValue engineFunctionValue : functionValueList) {
                param.put(engineFunctionValue.getParamCode(), valueResolve.getValue(engineFunctionValue.getType(), engineFunctionValue.getValueType(), engineFunctionValue.getValue(), cacheMap));
            }
        }
        Object abstractFunction = this.applicationContext.getBean(engineFunction.getExecutor());
        return new Function(engineFunction.getName(), abstractFunction, DataType.getByValue(engineFunction.getReturnValueType()), param);
    }
}
