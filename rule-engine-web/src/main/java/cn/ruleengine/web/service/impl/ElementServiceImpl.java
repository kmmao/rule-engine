package cn.ruleengine.web.service.impl;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.DeletedEnum;
import cn.ruleengine.web.service.ElementService;
import cn.ruleengine.web.store.entity.RuleEngineCondition;
import cn.ruleengine.web.store.entity.RuleEngineElement;
import cn.ruleengine.web.store.entity.RuleEngineFunctionValue;
import cn.ruleengine.web.store.entity.RuleEngineRule;
import cn.ruleengine.web.store.manager.RuleEngineConditionManager;
import cn.ruleengine.web.store.manager.RuleEngineElementManager;
import cn.ruleengine.web.store.manager.RuleEngineFunctionValueManager;
import cn.ruleengine.web.store.manager.RuleEngineRuleManager;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageBase;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.element.*;
import cn.ruleengine.web.vo.user.UserData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.core.value.VariableType;
import cn.ruleengine.web.vo.workspace.Workspace;
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

    /**
     * 添加元素
     *
     * @param addConditionRequest 元素信息
     * @return true
     */
    @Override
    public Boolean add(AddElementRequest addConditionRequest) {
        if (this.elementCodeIsExists(addConditionRequest.getCode())) {
            throw new ValidException("元素Code：{}已经存在", addConditionRequest.getCode());
        }
        Workspace workspace = Context.getCurrentWorkspace();
        RuleEngineElement engineElement = new RuleEngineElement();
        UserData userData = Context.getCurrentUser();
        engineElement.setCreateUserId(userData.getId());
        engineElement.setCreateUserName(userData.getUsername());
        engineElement.setName(addConditionRequest.getName());
        engineElement.setCode(addConditionRequest.getCode());
        engineElement.setWorkspaceId(workspace.getId());
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
        Workspace workspace = Context.getCurrentWorkspace();
        Integer count = this.ruleEngineElementManager.lambdaQuery()
                .eq(RuleEngineElement::getWorkspaceId, workspace.getId())
                .eq(RuleEngineElement::getCode, code)
                .count();
        return count != null && count >= 1;
    }

    /**
     * 元素列表
     *
     * @param pageRequest param
     * @return ListElementResponse
     */
    @Override
    public PageResult<ListElementResponse> list(PageRequest<ListElementRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        Workspace workspace = Context.getCurrentWorkspace();
        return PageUtils.page(ruleEngineElementManager, page, () -> {
            QueryWrapper<RuleEngineElement> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(RuleEngineElement::getWorkspaceId, workspace.getId());
            PageUtils.defaultOrder(orders, wrapper);

            ListElementRequest query = pageRequest.getQuery();
            if (Validator.isNotEmpty(query.getValueType())) {
                wrapper.lambda().eq(RuleEngineElement::getValueType, query.getValueType());
            }
            if (Validator.isNotEmpty(query.getName())) {
                wrapper.lambda().like(RuleEngineElement::getName, query.getName());
            }
            if (Validator.isNotEmpty(query.getCode())) {
                wrapper.lambda().like(RuleEngineElement::getCode, query.getCode());
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

    /**
     * 根据id查询元素
     *
     * @param id 元素id
     * @return GetElementResponse
     */
    @Override
    public GetElementResponse get(Integer id) {
        RuleEngineElement engineElement = this.ruleEngineElementManager.lambdaQuery()
                .eq(RuleEngineElement::getId, id)
                .one();
        return BasicConversion.INSTANCE.convert(engineElement);
    }

    /**
     * 根据元素id更新元素
     *
     * @param updateElementRequest 元素信息
     * @return true
     */
    @Override
    public Boolean update(UpdateElementRequest updateElementRequest) {
        RuleEngineElement engineElement = this.ruleEngineElementManager.lambdaQuery()
                .eq(RuleEngineElement::getId, updateElementRequest.getId())
                .one();
        if (engineElement == null) {
            throw new ValidException("找不到更新的元素");
        }
        engineElement.setId(updateElementRequest.getId());
        engineElement.setName(updateElementRequest.getName());
        engineElement.setDescription(updateElementRequest.getDescription());
        return this.ruleEngineElementManager.updateById(engineElement);
    }

    /**
     * 根据id删除元素
     *
     * @param id 元素id
     * @return true
     */
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
