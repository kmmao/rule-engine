package cn.ruleengine.web.service.impl;

import cn.ruleengine.web.annotation.DataPermission;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.Permission;
import cn.ruleengine.web.exception.DataPermissionException;
import cn.ruleengine.web.service.DataPermissionService;
import cn.ruleengine.web.service.WorkspaceService;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import cn.ruleengine.web.vo.user.UserData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/13
 * @since 1.0.0
 */
@Service
public class DataPermissionServiceImpl implements DataPermissionService {

    @Resource
    private RuleEngineConditionManager ruleEngineConditionManager;
    @Resource
    private RuleEngineElementManager ruleEngineElementManager;
    @Resource
    private RuleEngineVariableManager ruleEngineVariableManager;
    @Resource
    private RuleEngineDecisionTableManager ruleEngineDecisionTableManager;
    @Resource
    private RuleEngineGeneralRuleManager ruleEngineGeneralRuleManager;
    @Resource
    private WorkspaceService workspaceService;
    @Resource
    private RuleEngineRuleSetManager ruleEngineRuleSetManager;


    /**
     * 校验数据权限
     *
     * @param id             数据id
     * @param dataPermission dataPermission
     * @return true有权限
     */
    @Override
    public Boolean validDataPermission(Serializable id, DataPermission dataPermission) {
        Permission.DataType dataPermissionType = dataPermission.dataType();
        Permission.OperationType operationType = dataPermission.operationType();
        UserData userData = Context.getCurrentUser();
        Integer userId = userData.getId();
        switch (dataPermissionType) {
            case ELEMENT:
                RuleEngineElement ruleEngineElement = this.ruleEngineElementManager.getById(id);
                // 不影响后续逻辑
                if (ruleEngineElement == null) {
                    return true;
                }
                if (Objects.equals(ruleEngineElement.getCreateUserId(), userId)) {
                    return true;
                }
                return this.permissionTypeProcess(userId, ruleEngineElement.getWorkspaceId(), operationType);
            case VARIABLE:
                RuleEngineVariable ruleEngineVariable = this.ruleEngineVariableManager.getById(id);
                // 不影响后续逻辑
                if (ruleEngineVariable == null) {
                    return true;
                }
                if (Objects.equals(ruleEngineVariable.getCreateUserId(), userId)) {
                    return true;
                }
                return this.permissionTypeProcess(userId, ruleEngineVariable.getWorkspaceId(), operationType);
            case FUNCTION:
                break;
            case CONDITION:
                RuleEngineCondition ruleEngineCondition = this.ruleEngineConditionManager.getById(id);
                // 不影响后续逻辑
                if (ruleEngineCondition == null) {
                    return true;
                }
                if (Objects.equals(ruleEngineCondition.getCreateUserId(), userId)) {
                    return true;
                }
                return this.permissionTypeProcess(userId, ruleEngineCondition.getWorkspaceId(), operationType);
            case GENERAL_RULE:
                RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
                // 不影响后续逻辑
                if (ruleEngineGeneralRule == null) {
                    return true;
                }
                if (Objects.equals(ruleEngineGeneralRule.getCreateUserId(), userId)) {
                    return true;
                }
                return this.permissionTypeProcess(userId, ruleEngineGeneralRule.getWorkspaceId(), operationType);
            case DECISION_TABLE:
                RuleEngineDecisionTable ruleEngineDecisionTable = this.ruleEngineDecisionTableManager.getById(id);
                // 不影响后续逻辑
                if (ruleEngineDecisionTable == null) {
                    return true;
                }
                if (Objects.equals(ruleEngineDecisionTable.getCreateUserId(), userId)) {
                    return true;
                }
                return this.permissionTypeProcess(userId, ruleEngineDecisionTable.getWorkspaceId(), operationType);
            case RULE_SET:
                RuleEngineRuleSet ruleEngineRuleSet = this.ruleEngineRuleSetManager.getById(id);
                // 不影响后续逻辑
                if (ruleEngineRuleSet == null) {
                    return true;
                }
                if (Objects.equals(ruleEngineRuleSet.getCreateUserId(), userId)) {
                    return true;
                }
                return this.permissionTypeProcess(userId, ruleEngineRuleSet.getWorkspaceId(), operationType);
            case ELEMENT_GROUP:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + dataPermissionType);
        }
        return true;
    }

    /**
     * 根据权限类型校验相应规则
     *
     * @param userId        用户id
     * @param workspaceId   工作空间id
     * @param operationType 操作类型
     * @return true有权限
     */
    private boolean permissionTypeProcess(Integer userId, Integer workspaceId, Permission.OperationType operationType) {
        switch (operationType) {
            case ADD:
                // 任意人都可以添加
                break;
            case DELETE:
                // 普通用户只能删除自己创建的数据，已经校验过了
                break;
            case UPDATE:
                // 普通用户只能更新自己创建的数据，已经校验过了
                break;
            case VALID_WORKSPACE:
                // 不可以查看别的空间的数据权限
                boolean workspacePermission = this.workspaceService.hasWorkspacePermission(workspaceId, userId);
                if (!workspacePermission) {
                    throw new DataPermissionException("你没有此工作空间的操作权限");
                }
                return true;
            case ADD_OR_UPDATE:
            case SELECT:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + operationType);
        }
        return false;
    }

}
