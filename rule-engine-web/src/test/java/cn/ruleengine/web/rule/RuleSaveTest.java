package cn.ruleengine.web.rule;


import cn.ruleengine.web.BaseTest;
import cn.ruleengine.web.enums.DeletedEnum;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import cn.ruleengine.core.value.VariableType;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;


public class RuleSaveTest extends BaseTest {

    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private RuleEngineConditionGroupManager ruleEngineConditionGroupManager;
    @Resource
    private RuleEngineConditionGroupConditionManager ruleEngineConditionGroupConditionManager;
    @Resource
    private RuleEngineConditionManager ruleEngineConditionManager;
    @Resource
    private RuleEngineElementManager ruleEngineElementManager;
    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineFunctionValueManager ruleEngineFunctionValueManager;

    @Test
    public void test() {
        RuleEngineRule engineRule = new RuleEngineRule();
        engineRule.setCode("test009");
        engineRule.setName("测试规则");
        engineRule.setStatus(0);
        engineRule.setCreateTime(new Date());
        engineRule.setUpdateTime(new Date());
        engineRule.setDeleted(DeletedEnum.ENABLE.getStatus());


        {
            //是否为邮箱函数
            RuleEngineVariable ruleEngineVariable = new RuleEngineVariable();
            ruleEngineVariable.setName("发送邮件集合");
            ruleEngineVariable.setType(VariableType.FUNCTION.getType());
            ruleEngineVariable.setValueType("BOOLEAN");
            ruleEngineVariable.setValue("3");
            ruleEngineVariable.setCreateTime(new Date());
            ruleEngineVariable.setUpdateTime(new Date());
            ruleEngineVariable.setDeleted(0);
            ruleEngineVariableManager.save(ruleEngineVariable);
            //mailSmtpHost
            {
                //函数入参数值
                RuleEngineFunctionValue functionValue = new RuleEngineFunctionValue();
                functionValue.setFunctionId(3);
                functionValue.setParamCode("mailSmtpHost");
                functionValue.setVariableId(ruleEngineVariable.getId());
                functionValue.setType(VariableType.CONSTANT.getType());
                functionValue.setValueType("STRING");
                functionValue.setValue("smtp.qq.com");
                functionValue.setCreateTime(new Date());
                functionValue.setUpdateTime(new Date());
                functionValue.setDeleted(0);
                ruleEngineFunctionValueManager.save(functionValue);
            }
            //mailSmtpPort
            {
                //函数入参数值
                RuleEngineFunctionValue functionValue = new RuleEngineFunctionValue();
                functionValue.setFunctionId(3);
                functionValue.setParamCode("mailSmtpPort");
                functionValue.setVariableId(ruleEngineVariable.getId());
                functionValue.setType(VariableType.CONSTANT.getType());
                functionValue.setValueType("NUMBER");
                functionValue.setValue("465");
                functionValue.setCreateTime(new Date());
                functionValue.setUpdateTime(new Date());
                functionValue.setDeleted(0);
                ruleEngineFunctionValueManager.save(functionValue);
            }
            //user
            {
                //函数入参数值
                RuleEngineFunctionValue functionValue = new RuleEngineFunctionValue();
                functionValue.setFunctionId(3);
                functionValue.setParamCode("user");
                functionValue.setVariableId(ruleEngineVariable.getId());
                functionValue.setType(VariableType.CONSTANT.getType());
                functionValue.setValueType("STRING");
                functionValue.setValue("761945125@qq.com");
                functionValue.setCreateTime(new Date());
                functionValue.setUpdateTime(new Date());
                functionValue.setDeleted(0);
                ruleEngineFunctionValueManager.save(functionValue);
            }
            //password
            {
                //函数入参数值
                RuleEngineFunctionValue functionValue = new RuleEngineFunctionValue();
                functionValue.setFunctionId(3);
                functionValue.setParamCode("password");
                functionValue.setVariableId(ruleEngineVariable.getId());
                functionValue.setType(VariableType.CONSTANT.getType());
                functionValue.setValueType("STRING");
                functionValue.setValue("123");
                functionValue.setCreateTime(new Date());
                functionValue.setUpdateTime(new Date());
                functionValue.setDeleted(0);
                ruleEngineFunctionValueManager.save(functionValue);
            }
            //tos
            {
                //函数入参数值
                RuleEngineFunctionValue functionValue = new RuleEngineFunctionValue();
                functionValue.setFunctionId(3);
                functionValue.setParamCode("tos");
                functionValue.setVariableId(ruleEngineVariable.getId());
                functionValue.setType(VariableType.ELEMENT.getType());
                functionValue.setValueType("COLLECTION");

                RuleEngineElement ruleEngineElement = new RuleEngineElement();
                ruleEngineElement.setName("邮件接收人");
                ruleEngineElement.setCode("email-test-tos");
                ruleEngineElement.setValueType("COLLECTION");
                ruleEngineElement.setCreateTime(new Date());
                ruleEngineElement.setUpdateTime(new Date());
                ruleEngineElement.setDeleted(0);
                ruleEngineElementManager.save(ruleEngineElement);

                functionValue.setValue(ruleEngineElement.getId() + "");
                functionValue.setCreateTime(new Date());
                functionValue.setUpdateTime(new Date());
                functionValue.setDeleted(0);
                ruleEngineFunctionValueManager.save(functionValue);
            }
            //  title
            {
                //函数入参数值
                RuleEngineFunctionValue functionValue = new RuleEngineFunctionValue();
                functionValue.setFunctionId(3);
                functionValue.setParamCode("title");
                functionValue.setVariableId(ruleEngineVariable.getId());
                functionValue.setType(VariableType.ELEMENT.getType());
                functionValue.setValueType("STRING");

                RuleEngineElement ruleEngineElement = new RuleEngineElement();
                ruleEngineElement.setName("邮件标题");
                ruleEngineElement.setCode("email-test-title");
                ruleEngineElement.setValueType("STRING");
                ruleEngineElement.setCreateTime(new Date());
                ruleEngineElement.setUpdateTime(new Date());
                ruleEngineElement.setDeleted(0);
                ruleEngineElementManager.save(ruleEngineElement);

                functionValue.setValue(ruleEngineElement.getId() + "");
                functionValue.setCreateTime(new Date());
                functionValue.setUpdateTime(new Date());
                functionValue.setDeleted(0);
                ruleEngineFunctionValueManager.save(functionValue);
            }
            //  text
            {
                //函数入参数值
                RuleEngineFunctionValue functionValue = new RuleEngineFunctionValue();
                functionValue.setFunctionId(3);
                functionValue.setParamCode("text");
                functionValue.setVariableId(ruleEngineVariable.getId());
                functionValue.setType(VariableType.ELEMENT.getType());
                functionValue.setValueType("STRING");

                RuleEngineElement ruleEngineElement = new RuleEngineElement();
                ruleEngineElement.setName("发送内容");
                ruleEngineElement.setCode("email-test-text");
                ruleEngineElement.setValueType("STRING");
                ruleEngineElement.setCreateTime(new Date());
                ruleEngineElement.setUpdateTime(new Date());
                ruleEngineElement.setDeleted(0);
                ruleEngineElementManager.save(ruleEngineElement);

                functionValue.setValue(ruleEngineElement.getId() + "");
                functionValue.setCreateTime(new Date());
                functionValue.setUpdateTime(new Date());
                functionValue.setDeleted(0);
                ruleEngineFunctionValueManager.save(functionValue);
            }
            engineRule.setActionValue(ruleEngineVariable.getId() + "");
            engineRule.setActionValueType(ruleEngineVariable.getValueType());
            engineRule.setActionType(VariableType.VARIABLE.getType());
        }


        engineRule.setDefaultActionValue("我，是，默，认，规，则");
        engineRule.setDefaultActionValueType("COLLECTION");
        engineRule.setDefaultActionType(VariableType.CONSTANT.getType());
        ruleEngineRuleManager.save(engineRule);


        RuleEngineConditionGroup conditionGroup = new RuleEngineConditionGroup();
        conditionGroup.setName("测试条件组1");
        conditionGroup.setRuleId(engineRule.getId());
        conditionGroup.setOrderNo(0);
        conditionGroup.setCreateTime(new Date());
        conditionGroup.setUpdateTime(new Date());
        conditionGroup.setDeleted(DeletedEnum.ENABLE.getStatus());
        ruleEngineConditionGroupManager.save(conditionGroup);


        {
            RuleEngineCondition engineCondition = new RuleEngineCondition();
            engineCondition.setName("验证输入的参数是否为邮箱");

            {
                RuleEngineElement ruleEngineElement = new RuleEngineElement();
                ruleEngineElement.setName("布尔元素");
                ruleEngineElement.setCode("ele-boolean");
                ruleEngineElement.setValueType("BOOLEAN");
                ruleEngineElement.setCreateTime(new Date());
                ruleEngineElement.setUpdateTime(new Date());
                ruleEngineElement.setDeleted(0);
                ruleEngineElementManager.save(ruleEngineElement);
                engineCondition.setLeftType(VariableType.ELEMENT.getType());
                engineCondition.setLeftValueType(ruleEngineElement.getValueType());
                engineCondition.setLeftValue(ruleEngineElement.getId() + "");
            }

            {
                //是否为邮箱函数
                RuleEngineVariable ruleEngineVariable = new RuleEngineVariable();
                ruleEngineVariable.setName("是否为邮箱函数变量");
                ruleEngineVariable.setType(VariableType.FUNCTION.getType());
                ruleEngineVariable.setValueType("BOOLEAN");
                ruleEngineVariable.setValue("1");
                ruleEngineVariable.setCreateTime(new Date());
                ruleEngineVariable.setUpdateTime(new Date());
                ruleEngineVariable.setDeleted(0);
                ruleEngineVariableManager.save(ruleEngineVariable);

                //函数入参数值
                RuleEngineFunctionValue functionValue = new RuleEngineFunctionValue();
                functionValue.setFunctionId(1);
                functionValue.setParamCode("value");
                functionValue.setVariableId(ruleEngineVariable.getId());
                functionValue.setType(VariableType.ELEMENT.getType());
                functionValue.setValueType("STRING");

                RuleEngineElement ruleEngineElement = new RuleEngineElement();
                ruleEngineElement.setName("字符串元素");
                ruleEngineElement.setCode("ele-string");
                ruleEngineElement.setValueType("STRING");
                ruleEngineElement.setCreateTime(new Date());
                ruleEngineElement.setUpdateTime(new Date());
                ruleEngineElement.setDeleted(0);
                ruleEngineElementManager.save(ruleEngineElement);

                functionValue.setValue(ruleEngineElement.getId() + "");
                functionValue.setCreateTime(new Date());
                functionValue.setUpdateTime(new Date());
                functionValue.setDeleted(0);
                ruleEngineFunctionValueManager.save(functionValue);

                engineCondition.setRightType(VariableType.VARIABLE.getType());
                engineCondition.setRightValueType(ruleEngineVariable.getValueType());
                engineCondition.setRightValue(ruleEngineVariable.getId() + "");
            }
            engineCondition.setSymbol("EQ");
            engineCondition.setUpdateTime(new Date());
            engineCondition.setCreateTime(new Date());
            engineCondition.setDeleted(0);
            ruleEngineConditionManager.save(engineCondition);

            RuleEngineConditionGroupCondition engineConditionSet = new RuleEngineConditionGroupCondition();
            engineConditionSet.setConditionId(engineCondition.getId());
            engineConditionSet.setConditionGroupId(conditionGroup.getId());
            engineConditionSet.setOrderNo(0);
            engineConditionSet.setCreateTime(new Date());
            engineConditionSet.setUpdateTime(new Date());
            engineConditionSet.setDeleted(0);
            ruleEngineConditionGroupConditionManager.save(engineConditionSet);
        }
        {
            RuleEngineCondition engineCondition = new RuleEngineCondition();
            engineCondition.setName("测试集合条件2");
            engineCondition.setDescription("text");
            {
                //是否为邮箱函数
                RuleEngineVariable ruleEngineVariable = new RuleEngineVariable();
                ruleEngineVariable.setName("是否为空集合");
                ruleEngineVariable.setType(VariableType.FUNCTION.getType());
                ruleEngineVariable.setValueType("BOOLEAN");
                ruleEngineVariable.setValue("2");
                ruleEngineVariable.setCreateTime(new Date());
                ruleEngineVariable.setUpdateTime(new Date());
                ruleEngineVariable.setDeleted(0);
                ruleEngineVariableManager.save(ruleEngineVariable);

                //函数入参数值
                RuleEngineFunctionValue functionValue = new RuleEngineFunctionValue();
                functionValue.setFunctionId(2);
                functionValue.setParamCode("list");
                functionValue.setVariableId(ruleEngineVariable.getId());
                functionValue.setType(VariableType.ELEMENT.getType());
                functionValue.setValueType("COLLECTION");

                RuleEngineElement ruleEngineElement = new RuleEngineElement();
                ruleEngineElement.setName("集合元素");
                ruleEngineElement.setCode("ele-collection");
                ruleEngineElement.setValueType("COLLECTION");
                ruleEngineElement.setCreateTime(new Date());
                ruleEngineElement.setUpdateTime(new Date());
                ruleEngineElement.setDeleted(0);
                ruleEngineElementManager.save(ruleEngineElement);

                functionValue.setValue(ruleEngineElement.getId() + "");
                functionValue.setCreateTime(new Date());
                functionValue.setUpdateTime(new Date());
                functionValue.setDeleted(0);
                ruleEngineFunctionValueManager.save(functionValue);

                engineCondition.setLeftType(VariableType.VARIABLE.getType());
                engineCondition.setLeftValueType(ruleEngineVariable.getValueType());
                engineCondition.setLeftValue(ruleEngineVariable.getId() + "");
            }
            engineCondition.setRightType(2);
            engineCondition.setRightValueType("BOOLEAN");
            engineCondition.setRightValue("true");
            engineCondition.setSymbol("EQ");
            engineCondition.setUpdateTime(new Date());
            engineCondition.setCreateTime(new Date());
            engineCondition.setDeleted(0);
            ruleEngineConditionManager.save(engineCondition);

            RuleEngineConditionGroupCondition engineConditionSet = new RuleEngineConditionGroupCondition();
            engineConditionSet.setConditionId(engineCondition.getId());
            engineConditionSet.setConditionGroupId(conditionGroup.getId());
            engineConditionSet.setOrderNo(0);
            engineConditionSet.setCreateTime(new Date());
            engineConditionSet.setUpdateTime(new Date());
            engineConditionSet.setDeleted(0);
            ruleEngineConditionGroupConditionManager.save(engineConditionSet);
        }
    }
}
