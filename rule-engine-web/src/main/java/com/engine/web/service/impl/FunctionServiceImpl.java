package com.engine.web.service.impl;

import java.lang.reflect.Method;
import java.util.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.engine.core.FunctionExecutor;
import com.engine.core.exception.ValidException;
import com.engine.core.value.Constant;
import com.engine.core.value.DataType;
import com.engine.core.value.Function;
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

    /**
     * 函数列表
     *
     * @param pageRequest param
     * @return list
     */
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

    /**
     * 查询函数详情
     *
     * @param idRequest 函数id
     * @return 函数信息
     */
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
     * 函数模拟测试
     *
     * @param runFunction 函数入参值
     * @return result
     */
    @Override
    public Object run(RunFunction runFunction) {
        RuleEngineFunction engineFunction = this.ruleEngineFunctionManager.getById(runFunction.getId());
        String executor = engineFunction.getExecutor();
        if (applicationContext.containsBean(executor)) {
            Object abstractFunction = applicationContext.getBean(executor);
            // 执行函数入参
            Map<String, Object> paramValue = this.getParamValue(runFunction.getParamValues());
            Function function = new Function(abstractFunction);
            return function.executor(paramValue);
        } else {
            throw new ValidException("容器中找不到{}函数", executor);
        }
    }

    /**
     * 处理函数值
     *
     * @param paramValue 函数参数值
     * @return map
     */
    private Map<String, Object> getParamValue(List<ParamValue> paramValue) {
        Map<String, Object> paramMap = new HashMap<>(paramValue.size());
        for (ParamValue value : paramValue) {
            Constant constant = new Constant();
            paramMap.put(value.getCode(), constant.dataConversion(value.getValue(), DataType.getByValue(value.getValueType())));
        }
        return paramMap;
    }

    /**
     * 处理函数参数
     *
     * @param functionParamList 函数参数列表
     * @return list
     */
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
