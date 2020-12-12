package cn.ruleengine.web.vo.conver;

import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.vo.condition.ConfigBean;
import cn.ruleengine.web.vo.condition.group.condition.SaveOrUpdateConditionGroupCondition;
import cn.ruleengine.web.vo.element.GetElementResponse;
import cn.ruleengine.web.vo.menu.ListMenuResponse;
import cn.ruleengine.web.vo.rule.DefaultAction;
import cn.ruleengine.web.vo.rule.RuleDefinition;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.user.UserResponse;
import cn.ruleengine.web.vo.variable.GetVariableResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/12
 * @since 1.0.0
 */
@Mapper
public interface BasicConversion {

    BasicConversion INSTANCE = Mappers.getMapper(BasicConversion.class);

    /**
     * RuleEngineMenu to ListMenuResponse
     *
     * @param ruleEngineMenu ruleEngineMenu
     * @return ListMenuResponse
     */
    ListMenuResponse conver(RuleEngineMenu ruleEngineMenu);

    /**
     * ruleEngineElement to GetElementResponse
     *
     * @param ruleEngineElement ruleEngineElement
     * @return GetElementResponse
     */
    GetElementResponse conver(RuleEngineElement ruleEngineElement);

    /**
     * ruleEngineVariable to GetVariableResponse
     *
     * @param ruleEngineVariable ruleEngineVariable
     * @return GetVariableResponse
     */
    GetVariableResponse conver(RuleEngineVariable ruleEngineVariable);

    /**
     * ruleEngineRule to RuleDefinition
     *
     * @param ruleEngineRule ruleEngineRule
     * @return RuleDefinition
     */
    RuleDefinition conver(RuleEngineRule ruleEngineRule);

    /**
     * value to DefaultAction
     *
     * @param value value
     * @return DefaultAction
     */
    DefaultAction conver(ConfigBean.Value value);

    /**
     * ruleEngineUser to UserData
     *
     * @param ruleEngineUser ruleEngineUser
     * @return UserData
     */
    UserData conver(RuleEngineUser ruleEngineUser);

    /**
     * userData to UserResponse
     *
     * @param userData userData
     * @return UserResponse
     */
    UserResponse conver(UserData userData);

    /**
     * saveOrUpdateConditionGroup to  RuleEngineConditionGroupCondition
     *
     * @param saveOrUpdateConditionGroup saveOrUpdateConditionGroup
     * @return RuleEngineConditionGroupCondition
     */
    RuleEngineConditionGroupCondition conver(SaveOrUpdateConditionGroupCondition saveOrUpdateConditionGroup);

}
