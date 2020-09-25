package com.engine.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.engine.core.exception.ValidException;
import com.engine.web.enums.DeletedEnum;
import com.engine.web.service.ElementService;
import com.engine.web.store.entity.RuleEngineRule;
import com.engine.web.store.manager.RuleEngineElementManager;
import com.engine.web.store.mapper.RuleEngineElementMapper;
import com.engine.web.util.PageUtils;
import com.engine.web.vo.base.request.PageRequest;
import com.engine.web.vo.base.response.PageBase;
import com.engine.web.vo.base.response.PageResult;
import com.engine.web.vo.element.*;
import com.engine.web.store.entity.RuleEngineElement;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
    private RuleEngineElementMapper ruleEngineElementMapper;

    @Override
    public Boolean add(AddElementRequest addConditionRequest) {
        RuleEngineElement engineElement = new RuleEngineElement();
        engineElement.setName(addConditionRequest.getName());
        engineElement.setCode(addConditionRequest.getCode());
        engineElement.setDescription(addConditionRequest.getDescription());
        engineElement.setValueType(addConditionRequest.getValueType());
        engineElement.setDeleted(DeletedEnum.ENABLE.getStatus());
        return ruleEngineElementManager.save(engineElement);
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
        List<RuleEngineRule> ruleEngineRules = ruleEngineElementMapper.countRule(id);
        if (CollUtil.isNotEmpty(ruleEngineRules)) {
            throw new ValidException("有规则在引用此元素，无法删除");
        }
        return ruleEngineElementManager.removeById(id);
    }

}
