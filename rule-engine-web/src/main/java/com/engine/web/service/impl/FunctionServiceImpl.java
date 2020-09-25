package com.engine.web.service.impl;

import java.lang.reflect.Method;
import java.util.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.engine.core.FunctionProcessor;
import com.engine.core.annotation.Executor;
import com.engine.core.exception.ValidException;
import com.engine.core.value.Constant;
import com.engine.core.value.DataType;
import com.engine.web.enums.FunctionSource;
import com.engine.web.service.FunctionService;
import com.engine.web.store.entity.RuleEngineFunction;
import com.engine.web.store.entity.RuleEngineFunctionParam;
import com.engine.web.store.manager.RuleEngineFunctionManager;
import com.engine.web.store.manager.RuleEngineFunctionParamManager;
import com.engine.web.util.PageUtils;
import com.engine.web.vo.base.request.PageRequest;
import com.engine.web.vo.base.response.PageBase;
import com.engine.web.vo.base.response.PageResult;
import com.engine.web.vo.base.response.Rows;
import com.engine.web.vo.function.*;
import com.engine.web.vo.variable.ParamValue;
import com.itranswarp.compiler.JavaStringCompiler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/27
 * @since 1.0.0
 */
@Service
public class FunctionServiceImpl implements FunctionService {

    @Resource
    private RuleEngineFunctionManager ruleEngineFunctionManager;
    @Resource
    private RuleEngineFunctionParamManager ruleEngineFunctionParamManager;
    @Resource
    private ApplicationContext applicationContext;

    private static final String FUNCTION_PACKAGE = "com.engine.web.function";

    @Override
    public PageResult<ListFunctionResponse> list(PageRequest<ListFunctionRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();

        QueryWrapper<RuleEngineFunction> wrapper = new QueryWrapper<>();
        PageUtils.defaultOrder(orders, wrapper);
        ListFunctionRequest query = pageRequest.getQuery();
        if (Validator.isNotEmpty(query.getName())) {
            wrapper.lambda().like(RuleEngineFunction::getName, query.getName());
        }
        IPage<RuleEngineFunction> functionPage = ruleEngineFunctionManager.page(new Page<>(page.getPageIndex(), page.getPageSize()), wrapper);

        List<RuleEngineFunction> records = functionPage.getRecords();

        // 获取本次请求用到的所有函数参数
        Map<Integer, List<RuleEngineFunctionParam>> functionParamMap = Optional.of(records)
                .filter(CollUtil::isNotEmpty)
                .map(m -> {
                    List<Integer> functionIds = m.stream().map(RuleEngineFunction::getId).collect(Collectors.toList());
                    return this.ruleEngineFunctionParamManager.lambdaQuery().in(RuleEngineFunctionParam::getFunctionId, functionIds).list();
                })
                .filter(CollUtil::isNotEmpty)
                .map(m -> m.stream().collect(Collectors.groupingBy(RuleEngineFunctionParam::getFunctionId))).orElse(Collections.emptyMap());

        PageResult<ListFunctionResponse> pageResult = new PageResult<>();
        List<ListFunctionResponse> responseList = records.stream().map(m -> {
            ListFunctionResponse functionResponse = new ListFunctionResponse();
            functionResponse.setId(m.getId());
            functionResponse.setName(m.getName());
            functionResponse.setExecutor(m.getExecutor());
            functionResponse.setReturnValueType(m.getReturnValueType());
            functionResponse.setCreateTime(m.getCreateTime());
            // 处理方法参数
            functionResponse.setParams(this.getFunctionParam(functionParamMap.get(m.getId())));
            return functionResponse;
        }).collect(Collectors.toList());
        pageResult.setData(new Rows<>(responseList, PageUtils.getPageResponse(functionPage)));
        return pageResult;
    }

    @Override
    public GetFunctionResponse get(Integer id) {
        RuleEngineFunction ruleEngineFunction = this.ruleEngineFunctionManager.getById(id);
        if (ruleEngineFunction == null) {
            throw new ValidException("不存在函数：{}", id);
        }
        GetFunctionResponse functionResponse = new GetFunctionResponse();
        functionResponse.setId(ruleEngineFunction.getId());
        functionResponse.setName(ruleEngineFunction.getName());
        functionResponse.setDescription(ruleEngineFunction.getDescription());
        functionResponse.setExecutor(ruleEngineFunction.getExecutor());
        functionResponse.setReturnValueType(ruleEngineFunction.getReturnValueType());
        // 处理方法参数
        List<RuleEngineFunctionParam> functionParamList = this.ruleEngineFunctionParamManager.lambdaQuery().eq(RuleEngineFunctionParam::getFunctionId, id).list();
        functionResponse.setParams(this.getFunctionParam(functionParamList));
        return functionResponse;
    }

    /**
     * 安全问题待考虑
     * 如果上传的java文件涉及到删除磁盘，或者修改等操作，危险性极高
     *
     * @param addFunction 函数注册信息
     * @return true
     */
    @Override
    public Boolean add(AddFunction addFunction) {
        String className = FUNCTION_PACKAGE + StringPool.DOT + addFunction.getClassName();
        try {
            // 添加时如果存在了，报错
            Class.forName(className);
            throw new ValidException("已经存在此类，请修改后重新添加");
        } catch (ClassNotFoundException ignored) {
        }
        // 编译java文件
        Class<?> clazz = this.functionCompiler(addFunction.getClassName(), addFunction.getJavaCode());
        RuleEngineFunction ruleEngineFunction = new RuleEngineFunction();
        ruleEngineFunction.setSource(FunctionSource.JAVA_CODE.getValue());
        ruleEngineFunction.setFunctionJavaCode(addFunction.getJavaCode());
        ruleEngineFunction.setName(addFunction.getName());
        ruleEngineFunction.setDescription(addFunction.getDescription());
        ruleEngineFunction.setExecutor(StrUtil.lowerFirst(addFunction.getClassName()));
        // 动态解析@Executor注解的方法返回值
        ruleEngineFunction.setReturnValueType(this.getFunctionReturnValueType(clazz));
        this.ruleEngineFunctionManager.save(ruleEngineFunction);

        this.saveFunctionParam(addFunction.getParam(), ruleEngineFunction.getId());
        return true;
    }

    private void saveFunctionParam(List<Param> param, Integer functionId) {
        if (CollUtil.isNotEmpty(param)) {
            List<RuleEngineFunctionParam> functionParamList = param.stream().map(m -> {
                RuleEngineFunctionParam ruleEngineFunctionParam = new RuleEngineFunctionParam();
                ruleEngineFunctionParam.setFunctionId(functionId);
                ruleEngineFunctionParam.setParamName(m.getParamName());
                ruleEngineFunctionParam.setParamCode(m.getParamCode());
                ruleEngineFunctionParam.setValueType(m.getValueType());
                return ruleEngineFunctionParam;
            }).collect(Collectors.toList());
            this.ruleEngineFunctionParamManager.saveBatch(functionParamList);
        }
    }

    /**
     * 函数修改，函数涉及到的变量都必须修改
     * <p>
     * 1.首先移除注册到Spring容器中的函数
     * 2.然后移除已经加载的函数class
     * 3.然后重新加载到jvm
     * 4.然后重新加载到Spring容器中
     *
     * @param updateFunction 函数参数
     * @return true
     */
    @SneakyThrows
    @Override
    public Boolean update(UpdateFunction updateFunction) {
        RuleEngineFunction engineFunction = this.ruleEngineFunctionManager.getById(updateFunction.getId());
        if (engineFunction == null) {
            throw new ValidException("不存在函数：{}", updateFunction.getId());
        }
        if (engineFunction.getSource().equals(FunctionSource.SYSTEM.getValue())) {
            throw new ValidException("系统自带函数不支持修改");
        }
        // 函数的返回值类型不可以修改
        Class<?> clazz = this.functionCompiler(updateFunction.getClassName(), updateFunction.getJavaCode());
        if (!Objects.equals(this.getFunctionReturnValueType(clazz), engineFunction.getReturnValueType())) {
            throw new ValidException("函数返回值类型不允许修改");
        }

        RuleEngineFunction ruleEngineFunction = new RuleEngineFunction();
        ruleEngineFunction.setId(updateFunction.getId());
        ruleEngineFunction.setFunctionJavaCode(updateFunction.getJavaCode());
        ruleEngineFunction.setName(updateFunction.getName());
        ruleEngineFunction.setDescription(updateFunction.getDescription());
        ruleEngineFunction.setExecutor(StrUtil.lowerFirst(updateFunction.getClassName()));
        this.ruleEngineFunctionManager.updateById(ruleEngineFunction);
        // 先删除原有函数参数
        this.ruleEngineFunctionParamManager.lambdaUpdate().eq(RuleEngineFunctionParam::getFunctionId, ruleEngineFunction.getId()).remove();
        // 重新保存
        this.saveFunctionParam(updateFunction.getParam(), ruleEngineFunction.getId());

        String beanName = StrUtil.lowerFirst(updateFunction.getClassName());
        // 移除掉，重新加载
        if (applicationContext.containsBean(beanName)) {
            BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
            beanDefinitionRegistry.removeBeanDefinition(beanName);
        }
        // 注册新的函数bean
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
        beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
        return true;
    }

    @Override
    public Object run(RunFunction runFunction) {
        RuleEngineFunction engineFunction = this.ruleEngineFunctionManager.getById(runFunction.getId());
        String executor = engineFunction.getExecutor();
        if (applicationContext.containsBean(executor)) {
            Object bean = applicationContext.getBean(executor);
            // 执行函数入参
            Map<String, Object> paramValue = this.getParamValue(runFunction.getParamValue());
            FunctionProcessor processor = new FunctionProcessor();
            return processor.executor(bean, paramValue);
        } else {
            return "暂不支持";
        }
    }

    private Map<String, Object> getParamValue(List<ParamValue> paramValue) {
        Map<String, Object> paramMap = new HashMap<>(paramValue.size());
        for (ParamValue value : paramValue) {
            Constant constant = new Constant();
            Object dataConversion = constant.dataConversion(value.getValue(), DataType.getByValue(value.getValueType()));
            paramMap.put(value.getCode(), dataConversion);
        }
        return paramMap;
    }

    @Override
    public Class<?> functionTryCompiler(String name, String javaCode) {
        String className = FUNCTION_PACKAGE + StringPool.DOT + name;
        try {
            // 如果存在，直接返回，不再编译
            return this.getClass().getClassLoader().loadClass(className);
        } catch (ClassNotFoundException ignored) {
        }
        return this.functionCompiler(name, javaCode);
    }

    @Override
    public Class<?> functionCompiler(String name, String javaCode) {
        String className = FUNCTION_PACKAGE + StringPool.DOT + name;
        JavaStringCompiler javaStringCompiler = new JavaStringCompiler();
        Map<String, byte[]> compile;
        try {
            compile = javaStringCompiler.compile(name + ".java", javaCode);
        } catch (Exception e) {
            throw new ValidException("编译失败，请检查后重新上传", e);
        }
        try {
            return javaStringCompiler.loadClass(className, compile);
        } catch (Exception e) {
            throw new ValidException("类加载失败，请检查后重新上传", e);
        }
    }


    private String getFunctionReturnValueType(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Executor.class)) {
                Class<?> returnType = method.getReturnType();
                if (Collection.class.isAssignableFrom(returnType)) {
                    return "COLLECTION";
                } else if (Number.class.isAssignableFrom(returnType)) {
                    return "NUMBER";
                } else if (String.class.equals(returnType)) {
                    return "STRING";
                } else if (Boolean.class.equals(returnType)) {
                    return "BOOLEAN";
                } else {
                    throw new ValidException("暂不支持的返回值类型：{}", returnType);
                }
            }
        }
        throw new ValidException("没有找到带有（@Executor）可执行方法：{}", clazz.getSimpleName());
    }

    private List<FunctionParam> getFunctionParam(List<RuleEngineFunctionParam> functionParamList) {
        List<FunctionParam> params = new ArrayList<>();
        if (CollUtil.isNotEmpty(functionParamList)) {
            params = functionParamList.stream().map(m -> {
                FunctionParam param = new FunctionParam();
                param.setName(m.getParamName());
                param.setCode(m.getParamCode());
                param.setValueType(m.getValueType());
                return param;
            }).collect(Collectors.toList());
        }
        return params;
    }
}
