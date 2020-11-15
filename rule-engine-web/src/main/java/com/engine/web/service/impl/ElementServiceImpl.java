package com.engine.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.engine.core.exception.ValidException;
import com.engine.core.value.VariableType;
import com.engine.web.enums.DeletedEnum;
import com.engine.web.service.ElementService;
import com.engine.web.store.entity.*;
import com.engine.web.store.manager.*;
import com.engine.web.util.PageUtils;
import com.engine.web.vo.base.request.PageRequest;
import com.engine.web.vo.base.response.PageBase;
import com.engine.web.vo.base.response.PageResult;
import com.engine.web.vo.element.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Service
public class ElementServiceImpl implements ElementService {

    @Resource
    private RuleEngineElementManager ruleEngineElementManager;
    @Resource
    private RuleEngineFunctionValueManager ruleEngineFunctionValueManager;
    @Resource
    private RuleEngineConditionManager ruleEngineConditionManager;
    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;

    @Override
    public Boolean add(AddElementRequest addConditionRequest) {
        if (this.elementCodeIsExists(addConditionRequest.getCode())) {
            throw new ValidException("元素Code：{}已经存在", addConditionRequest.getCode());
        }
        RuleEngineElement engineElement = new RuleEngineElement();
        engineElement.setName(addConditionRequest.getName());
        engineElement.setCode(addConditionRequest.getCode());
        engineElement.setDescription(addConditionRequest.getDescription());
        engineElement.setValueType(addConditionRequest.getValueType());
        engineElement.setDeleted(DeletedEnum.ENABLE.getStatus());
        return ruleEngineElementManager.save(engineElement);
    }

    /**
     * 元素code是否存在
     *
     * @param code 元素code
     * @return true存在
     */
    @Override
    public Boolean elementCodeIsExists(String code) {
        Integer count = this.ruleEngineElementManager.lambdaQuery().eq(RuleEngineElement::getCode, code).count();
        return count != null && count >= 1;
    }

    @Override
    public PageResult<ListElementResponse> list(PageRequest<ListElementRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        return PageUtils.page(ruleEngineElementManager, page, () -> {
            QueryWrapper<RuleEngineElement> wrapper = new QueryWrapper<>();

            PageUtils.defaultOrder(orders, wrapper);

            ListElementRequest query = pageRequest.getQuery();
            if (Validator.isNotEmpty(query.getValueType())) {
                wrapper.lambda().eq(RuleEngineElement::getValueType, query.getValueType());
            }
            if (Validator.isNotEmpty(query.getName())) {
                wrapper.lambda().like(RuleEngineElement::getName, query.getName());
            }
            if (Validator.isNotEmpty(query.getCode())) {
                wrapper.lambda().like(RuleEngineElement::getName, query.getCode());
            }
            return wrapper;
        }, m -> {
            ListElementResponse listElementResponse = new ListElementResponse();
            listElementResponse.setId(m.getId());
            listElementResponse.setName(m.getName());
            listElementResponse.setCode(m.getCode());

            listElementResponse.setValueType(m.getValueType());
            listElementResponse.setDescription(m.getDescription());
            listElementResponse.setCreateTime(m.getCreateTime());
            return listElementResponse;
        });
    }

    @Override
    public GetElementResponse get(Integer id) {
        RuleEngineElement engineElement = ruleEngineElementManager.getById(id);
        GetElementResponse elementResponse = new GetElementResponse();
        BeanUtil.copyProperties(engineElement, elementResponse);
        return elementResponse;
    }

    @Override
    public Boolean update(UpdateElementRequest updateElementRequest) {
        RuleEngineElement engineElement = new RuleEngineElement();
        engineElement.setId(updateElementRequest.getId());
        engineElement.setName(updateElementRequest.getName());
        engineElement.setDescription(updateElementRequest.getDescription());
        return this.ruleEngineElementManager.updateById(engineElement);
    }

    @Override
    public Boolean delete(Integer id) {
        {
            Integer count = this.ruleEngineFunctionValueManager.lambdaQuery()
                    .eq(RuleEngineFunctionValue::getType, VariableType.ELEMENT.getType())
                    .eq(RuleEngineFunctionValue::getValue, id).count();
            if (count != null && count > 0) {
                throw new ValidException("有函数值在引用此元素，无法删除");
            }
        }
        {
            Integer count = this.ruleEngineRuleManager.lambdaQuery()
                    .and(a -> a.eq(RuleEngineRule::getActionType, VariableType.ELEMENT.getType()).eq(RuleEngineRule::getActionValue, id))
                    .or(o -> o.eq(RuleEngineRule::getDefaultActionType, VariableType.ELEMENT.getType()).eq(RuleEngineRule::getDefaultActionValue, id)).count();
            if (count != null && count > 0) {
                throw new ValidException("有规则在引用此元素，无法删除");
            }
        }
        {
            Integer count = ruleEngineConditionManager.lambdaQuery()
                    .and(a ->
                            a.eq(RuleEngineCondition::getLeftType, VariableType.ELEMENT.getType())
                                    .eq(RuleEngineCondition::getLeftValue, id)
                    ).or(o -> o.eq(RuleEngineCondition::getRightType, VariableType.ELEMENT.getType())
                            .eq(RuleEngineCondition::getRightValue, id)).count();
            if (count != null && count > 0) {
                throw new ValidException("有条件在引用此元素，无法删除");
            }
        }
        return ruleEngineElementManager.removeById(id);
    }

}
