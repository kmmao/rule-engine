package cn.ruleengine.web.rule;

import cn.ruleengine.web.BaseTest;
import cn.ruleengine.web.store.manager.*;

import org.junit.Test;

import javax.annotation.Resource;


public class RuleRemoveAllTest extends BaseTest {

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
        ruleEngineRuleManager.update().ge("id", 0).remove();
        ruleEngineConditionGroupManager.update().ge("id", 0).remove();
        ruleEngineConditionGroupConditionManager.update().ge("id", 0).remove();
        ruleEngineConditionManager.update().ge("id", 0).remove();
        ruleEngineElementManager.update().ge("id", 0).remove();
        ruleEngineVariableManager.update().ge("id", 0).remove();
        ruleEngineFunctionValueManager.update().ge("id", 0).remove();
    }
}
