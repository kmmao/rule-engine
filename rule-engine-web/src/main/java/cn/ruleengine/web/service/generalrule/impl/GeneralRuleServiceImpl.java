package cn.ruleengine.web.service.generalrule.impl;


import cn.ruleengine.core.rule.GeneralRule;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.EnableEnum;
import cn.ruleengine.web.enums.DataStatus;
import cn.ruleengine.web.listener.body.GeneralRuleMessageBody;
import cn.ruleengine.web.listener.event.GeneralRuleEvent;
import cn.ruleengine.web.service.ActionService;
import cn.ruleengine.web.service.ConditionSetService;
import cn.ruleengine.web.service.RuleEngineConditionGroupService;
import cn.ruleengine.web.service.ValueResolve;
import cn.ruleengine.web.service.ParameterService;
import cn.ruleengine.web.service.generalrule.GeneralRuleService;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import cn.ruleengine.web.store.mapper.RuleEngineRuleMapper;
import cn.ruleengine.web.store.mapper.RuleEngineGeneralRuleMapper;

import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.condition.*;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageBase;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.generalrule.*;
import cn.ruleengine.core.condition.ConditionGroup;

import cn.ruleengine.web.vo.generalrule.DefaultAction;


import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.Engine;
import cn.ruleengine.core.exception.ValidException;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.Workspace;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/24
 * @since 1.0.0
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class GeneralRuleServiceImpl implements GeneralRuleService {

    @Resource
    private RuleEngineRuleManager ruleEngineRuleManager;
    @Resource
    private RuleEngineGeneralRuleManager ruleEngineGeneralRuleManager;
    @Resource
    private RuleEngineGeneralRuleMapper ruleEngineGeneralRuleMapper;
    @Resource
    private RuleEngineRuleMapper ruleEngineRuleMapper;
    @Resource
    private Engine engine;
    @Resource
    private ParameterService parameterService;
    @Resource
    private RuleEngineGeneralRulePublishManager ruleEngineGeneralRulePublishManager;
    @Resource
    private ApplicationEventPublisher eventPublisher;
    @Resource
    private ValueResolve valueResolve;
    @Resource
    private RuleEngineConditionGroupService ruleEngineConditionGroupService;
    @Resource
    private ActionService actionService;
    @Resource
    private ConditionSetService conditionSetService;

    /**
     * 规则列表
     *
     * @param pageRequest 分页查询数据
     * @return page
     */
    @Override
    public PageResult<ListGeneralRuleResponse> list(PageRequest<ListGeneralRuleRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        Workspace workspace = Context.getCurrentWorkspace();
        return PageUtils.page(this.ruleEngineGeneralRuleManager, page, () -> {
            QueryWrapper<RuleEngineGeneralRule> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(RuleEngineGeneralRule::getWorkspaceId, workspace.getId());
            PageUtils.defaultOrder(orders, wrapper);

            ListGeneralRuleRequest query = pageRequest.getQuery();
            if (Validator.isNotEmpty(query.getName())) {
                wrapper.lambda().like(RuleEngineGeneralRule::getName, query.getName());
            }
            if (Validator.isNotEmpty(query.getCode())) {
                wrapper.lambda().like(RuleEngineGeneralRule::getCode, query.getCode());
            }
            if (Validator.isNotEmpty(query.getStatus())) {
                wrapper.lambda().eq(RuleEngineGeneralRule::getStatus, query.getStatus());
            }
            return wrapper;
        }, m -> {
            ListGeneralRuleResponse listRuleResponse = new ListGeneralRuleResponse();
            listRuleResponse.setId(m.getId());
            listRuleResponse.setName(m.getName());
            listRuleResponse.setCode(m.getCode());
            listRuleResponse.setIsPublish(this.engine.isExists(m.getWorkspaceCode(), m.getCode()));
            listRuleResponse.setCreateUserName(m.getCreateUserName());
            listRuleResponse.setStatus(m.getStatus());
            listRuleResponse.setCreateTime(m.getCreateTime());
            return listRuleResponse;
        });
    }

    /**
     * 规则code是否存在
     *
     * @param code 规则code
     * @return true存在
     */
    @Override
    public Boolean ruleCodeIsExists(String code) {
        Workspace workspace = Context.getCurrentWorkspace();
        Integer count = this.ruleEngineGeneralRuleManager.lambdaQuery()
                .eq(RuleEngineGeneralRule::getWorkspaceId, workspace.getId())
                .eq(RuleEngineGeneralRule::getCode, code)
                .count();
        return count != null && count >= 1;
    }

    /**
     * 更新规则信息
     *
     * @param updateRuleRequest 规则配置数据
     * @return true执行成功
     */
    @Override
    public Boolean updateRule(UpdateGeneralRuleRequest updateRuleRequest) {
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(updateRuleRequest.getId());
        if (ruleEngineGeneralRule == null) {
            throw new ValidException("不存在规则:{}", updateRuleRequest.getId());
        }
        // 如果之前是待发布，则删除原有待发布数据
        if (Objects.equals(ruleEngineGeneralRule.getStatus(), DataStatus.WAIT_PUBLISH.getStatus())) {
            this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                    .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                    .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, updateRuleRequest.getId())
                    .remove();
        }
        // 如果原来有条件信息，先删除原有信息
        this.ruleEngineConditionGroupService.removeConditionGroupByRuleIds(Collections.singletonList(ruleEngineGeneralRule.getRuleId()));
        // 保存条件信息
        this.ruleEngineConditionGroupService.saveConditionGroup(ruleEngineGeneralRule.getRuleId(), updateRuleRequest.getConditionGroup());
        //  更新规则信息
        ruleEngineGeneralRule.setId(updateRuleRequest.getId());
        ruleEngineGeneralRule.setStatus(DataStatus.EDIT.getStatus());
        // 保存规则结果
        ConfigValue action = updateRuleRequest.getAction();
        RuleEngineRule ruleEngineRule = new RuleEngineRule();
        ruleEngineRule.setId(ruleEngineGeneralRule.getRuleId());
        ruleEngineRule.setActionType(action.getType());
        ruleEngineRule.setActionValueType(action.getValueType());
        ruleEngineRule.setActionValue(action.getValue());
        ruleEngineRuleMapper.updateRuleById(ruleEngineRule);
        // 保存默认结果
        DefaultAction defaultAction = updateRuleRequest.getDefaultAction();
        ruleEngineGeneralRule.setEnableDefaultAction(defaultAction.getEnableDefaultAction());
        ruleEngineGeneralRule.setDefaultActionValue(defaultAction.getValue());
        ruleEngineGeneralRule.setDefaultActionValueType(defaultAction.getValueType());
        ruleEngineGeneralRule.setDefaultActionType(defaultAction.getType());
        this.ruleEngineGeneralRuleMapper.updateRuleById(ruleEngineGeneralRule);
        return true;
    }


    /**
     * 删除规则
     *
     * @param id 规则id
     * @return true
     */
    @Override
    public Boolean delete(Integer id) {
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        if (ruleEngineGeneralRule == null) {
            return false;
        }
        // 从引擎中移除规则
        if (this.engine.isExists(ruleEngineGeneralRule.getWorkspaceCode(), ruleEngineGeneralRule.getCode())) {
            GeneralRuleMessageBody ruleMessageBody = new GeneralRuleMessageBody();
            ruleMessageBody.setType(GeneralRuleMessageBody.Type.REMOVE);
            ruleMessageBody.setWorkspaceId(ruleEngineGeneralRule.getWorkspaceId());
            ruleMessageBody.setWorkspaceCode(ruleEngineGeneralRule.getWorkspaceCode());
            ruleMessageBody.setRuleCode(ruleEngineGeneralRule.getCode());
            this.eventPublisher.publishEvent(new GeneralRuleEvent(ruleMessageBody));
        }
        this.ruleEngineRuleManager.removeById(ruleEngineGeneralRule.getRuleId());
        // 删除规则发布记录
        this.ruleEngineGeneralRulePublishManager.lambdaUpdate().eq(RuleEngineGeneralRulePublish::getGeneralRuleId, id).remove();
        // 删除规则条件组信息
        this.ruleEngineConditionGroupService.removeConditionGroupByRuleIds(Collections.singletonList(ruleEngineGeneralRule.getRuleId()));
        // 删除规则
        return this.ruleEngineGeneralRuleManager.removeById(id);
    }

    /**
     * 保存或者更新规则定义信息
     *
     * @param ruleDefinition 规则定义信息
     * @return 规则id
     */
    @Override
    public Integer saveOrUpdateRuleDefinition(GeneralRuleDefinition ruleDefinition) {
        // 创建规则
        RuleEngineGeneralRule ruleEngineGeneralRule = new RuleEngineGeneralRule();
        if (ruleDefinition.getId() == null) {
            if (this.ruleCodeIsExists(ruleDefinition.getCode())) {
                throw new ValidException("规则Code：{}已经存在", ruleDefinition.getCode());
            }
            Workspace workspace = Context.getCurrentWorkspace();
            UserData userData = Context.getCurrentUser();
            ruleEngineGeneralRule.setCreateUserId(userData.getId());
            ruleEngineGeneralRule.setCreateUserName(userData.getUsername());
            ruleEngineGeneralRule.setWorkspaceId(workspace.getId());
            ruleEngineGeneralRule.setWorkspaceCode(workspace.getCode());
            RuleEngineRule ruleEngineRule = new RuleEngineRule();
            ruleEngineRule.setName(ruleDefinition.getName());
            ruleEngineRule.setCode(ruleDefinition.getCode());
            ruleEngineRule.setDescription(ruleDefinition.getDescription());
            ruleEngineRule.setCreateUserId(userData.getId());
            ruleEngineRule.setCreateUserName(userData.getUsername());
            this.ruleEngineRuleManager.save(ruleEngineRule);
            ruleEngineGeneralRule.setRuleId(ruleEngineRule.getId());
        } else {
            ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.lambdaQuery()
                    .eq(RuleEngineGeneralRule::getId, ruleDefinition.getId())
                    .one();
            if (ruleEngineGeneralRule == null) {
                throw new ValidException("不存在规则:{}", ruleDefinition.getId());
            } else {
                if (Objects.equals(ruleEngineGeneralRule.getStatus(), DataStatus.WAIT_PUBLISH.getStatus())) {
                    // 删除原有待发布规则
                    this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                            .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                            .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, ruleEngineGeneralRule.getId())
                            .remove();
                }
            }
        }
        ruleEngineGeneralRule.setId(ruleDefinition.getId());
        ruleEngineGeneralRule.setName(ruleDefinition.getName());
        ruleEngineGeneralRule.setCode(ruleDefinition.getCode());
        ruleEngineGeneralRule.setDescription(ruleDefinition.getDescription());
        ruleEngineGeneralRule.setStatus(DataStatus.EDIT.getStatus());
        this.ruleEngineGeneralRuleManager.saveOrUpdate(ruleEngineGeneralRule);
        return ruleEngineGeneralRule.getId();
    }

    /**
     * 获取规则定义信息
     *
     * @param id 规则id
     * @return 规则定义信息
     */
    @Override
    public GeneralRuleDefinition getRuleDefinition(Integer id) {
        RuleEngineGeneralRule engineGeneralRule = this.ruleEngineGeneralRuleManager.lambdaQuery()
                .eq(RuleEngineGeneralRule::getId, id)
                .one();
        if (engineGeneralRule == null) {
            return null;
        }
        return BasicConversion.INSTANCE.convert(engineGeneralRule);
    }

    /**
     * 生成待发布版本，更新规则数据
     *
     * @param releaseRequest 规则配置数据
     * @return true
     */
    @Override
    public Boolean generationRelease(GenerationReleaseRequest releaseRequest) {
        // 更新规则
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(releaseRequest.getId());
        if (ruleEngineGeneralRule == null) {
            throw new ValidException("不存在规则:{}", releaseRequest.getId());
        }
        Integer originStatus = ruleEngineGeneralRule.getStatus();
        // 如果开启了默认结果
        DefaultAction defaultAction = releaseRequest.getDefaultAction();
        defaultAction.valid();
        // 如果原来有条件信息，先删除原有信息
        this.ruleEngineConditionGroupService.removeConditionGroupByRuleIds(Collections.singletonList(ruleEngineGeneralRule.getRuleId()));
        // 保存条件信息
        this.ruleEngineConditionGroupService.saveConditionGroup(ruleEngineGeneralRule.getRuleId(), releaseRequest.getConditionGroup());
        //  更新规则信息
        ruleEngineGeneralRule.setStatus(DataStatus.WAIT_PUBLISH.getStatus());
        // 保存结果
        ConfigValue action = releaseRequest.getAction();
        RuleEngineRule ruleEngineRule = new RuleEngineRule();
        ruleEngineRule.setActionValue(action.getValue());
        ruleEngineRule.setId(ruleEngineGeneralRule.getRuleId());
        ruleEngineRule.setActionType(action.getType());
        ruleEngineRule.setActionValueType(action.getValueType());
        ruleEngineRuleMapper.updateRuleById(ruleEngineRule);
        // 保存默认结果
        ruleEngineGeneralRule.setEnableDefaultAction(defaultAction.getEnableDefaultAction());
        ruleEngineGeneralRule.setDefaultActionValue(defaultAction.getValue());
        ruleEngineGeneralRule.setDefaultActionValueType(defaultAction.getValueType());
        ruleEngineGeneralRule.setDefaultActionType(defaultAction.getType());
        this.ruleEngineGeneralRuleMapper.updateRuleById(ruleEngineGeneralRule);
        // 生成待发布规则
        if (Objects.equals(originStatus, DataStatus.WAIT_PUBLISH.getStatus())) {
            // 删除原有待发布规则
            this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                    .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                    .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, ruleEngineGeneralRule.getId())
                    .remove();
        }
        // 添加新的待发布数据
        GeneralRule generalRule = new GeneralRule();
        generalRule.setId(releaseRequest.getId());
        generalRule.setCode(ruleEngineGeneralRule.getCode());
        generalRule.setName(ruleEngineGeneralRule.getName());
        generalRule.setWorkspaceCode(ruleEngineGeneralRule.getWorkspaceCode());
        generalRule.setWorkspaceId(ruleEngineGeneralRule.getWorkspaceId());
        generalRule.setDescription(ruleEngineGeneralRule.getDescription());
        generalRule.setConditionSet(this.conditionSetService.loadConditionSet(releaseRequest.getConditionGroup()));
        generalRule.setActionValue(this.valueResolve.getValue(action.getType(), action.getValueType(), action.getValue()));
        // 如果启用了默认结果
        if (EnableEnum.ENABLE.getStatus().equals(defaultAction.getEnableDefaultAction())) {
            generalRule.setDefaultActionValue(this.valueResolve.getValue(defaultAction.getType(), defaultAction.getValueType(), defaultAction.getValue()));
        }

        RuleEngineGeneralRulePublish rulePublish = new RuleEngineGeneralRulePublish();
        rulePublish.setGeneralRuleId(generalRule.getId());
        rulePublish.setGeneralRuleCode(generalRule.getCode());
        rulePublish.setData(generalRule.toJson());
        rulePublish.setStatus(DataStatus.WAIT_PUBLISH.getStatus());
        rulePublish.setWorkspaceId(generalRule.getWorkspaceId());
        rulePublish.setWorkspaceCode(generalRule.getWorkspaceCode());
        this.ruleEngineGeneralRulePublishManager.save(rulePublish);
        return true;
    }


    /**
     * 规则发布
     *
     * @param id 规则id
     * @return true
     */
    @Override
    public Boolean publish(Integer id) {
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        if (ruleEngineGeneralRule == null) {
            throw new ValidException("不存在规则:{}", id);
        }
        if (ruleEngineGeneralRule.getStatus().equals(DataStatus.EDIT.getStatus())) {
            throw new ValidException("该规则不可执行:{}", id);
        }
        // 如果已经是发布规则了
        if (ruleEngineGeneralRule.getStatus().equals(DataStatus.PUBLISHED.getStatus())) {
            return true;
        }
        // 修改为已发布
        this.ruleEngineGeneralRuleManager.lambdaUpdate()
                .set(RuleEngineGeneralRule::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineGeneralRule::getId, ruleEngineGeneralRule.getId())
                .update();
        // 删除原有的已发布规则数据
        this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, ruleEngineGeneralRule.getId())
                .remove();
        // 更新待发布为已发布
        this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                .set(RuleEngineGeneralRulePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, ruleEngineGeneralRule.getId())
                .update();
        // 加载规则
        GeneralRuleMessageBody ruleMessageBody = new GeneralRuleMessageBody();
        ruleMessageBody.setType(GeneralRuleMessageBody.Type.LOAD);
        ruleMessageBody.setRuleCode(ruleEngineGeneralRule.getCode());
        ruleMessageBody.setWorkspaceId(ruleEngineGeneralRule.getWorkspaceId());
        ruleMessageBody.setWorkspaceCode(ruleEngineGeneralRule.getWorkspaceCode());
        this.eventPublisher.publishEvent(new GeneralRuleEvent(ruleMessageBody));
        return true;
    }

    /**
     * 获取规则信息
     *
     * @param id 规则id
     * @return 规则信息
     */
    @Override
    public GetGeneralRuleResponse getRuleConfig(Integer id) {
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        if (ruleEngineGeneralRule == null) {
            return null;
        }
        GetGeneralRuleResponse ruleResponse = new GetGeneralRuleResponse();
        ruleResponse.setId(ruleEngineGeneralRule.getId());
        ruleResponse.setCode(ruleEngineGeneralRule.getCode());
        ruleResponse.setName(ruleEngineGeneralRule.getName());
        ruleResponse.setDescription(ruleEngineGeneralRule.getDescription());
        ruleResponse.setConditionGroup(this.ruleEngineConditionGroupService.getConditionGroupConfig(ruleEngineGeneralRule.getRuleId()));
        // 结果
        RuleEngineRule ruleEngineRule = this.ruleEngineRuleManager.getById(ruleEngineGeneralRule.getRuleId());
        ConfigValue action = this.actionService.getAction(ruleEngineRule.getActionValue(), ruleEngineRule.getActionType(), ruleEngineRule.getActionValueType());
        ruleResponse.setAction(action);
        // 默认结果
        ConfigValue defaultValue = this.actionService.getAction(ruleEngineGeneralRule.getDefaultActionValue(), ruleEngineGeneralRule.getDefaultActionType(), ruleEngineGeneralRule.getDefaultActionValueType());
        DefaultAction defaultAction = new DefaultAction(defaultValue);
        defaultAction.setEnableDefaultAction(ruleEngineGeneralRule.getEnableDefaultAction());
        ruleResponse.setDefaultAction(defaultAction);
        return ruleResponse;
    }

    /**
     * 获取预览已发布的规则
     *
     * @param id 规则id
     * @return GetRuleResponse
     */
    @Override
    public ViewGeneralRuleResponse getPublishRule(Integer id) {
        RuleEngineGeneralRulePublish engineRulePublish = this.ruleEngineGeneralRulePublishManager.lambdaQuery()
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, id)
                .one();
        if (engineRulePublish == null) {
            throw new ValidException("找不到发布的规则:{}", id);
        }
        String data = engineRulePublish.getData();
        GeneralRule rule = GeneralRule.buildRule(data);
        return this.getRuleResponseProcess(rule);
    }

    /**
     * 规则预览
     *
     * @param id 规则id
     * @return GetRuleResponse
     */
    @Override
    public ViewGeneralRuleResponse getViewRule(Integer id) {
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        if (ruleEngineGeneralRule == null) {
            throw new ValidException("找不到预览的规则数据:{}", id);
        }
        // 如果只有已发布
        if (ruleEngineGeneralRule.getStatus().equals(DataStatus.PUBLISHED.getStatus())) {
            return this.getPublishRule(id);
        }
        RuleEngineGeneralRulePublish engineRulePublish = this.ruleEngineGeneralRulePublishManager.lambdaQuery()
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, id)
                .one();
        if (engineRulePublish == null) {
            throw new ValidException("找不到预览的规则数据:{}", id);
        }
        String data = engineRulePublish.getData();
        GeneralRule rule = GeneralRule.buildRule(data);
        return this.getRuleResponseProcess(rule);
    }

    /**
     * 解析Rule配置信息为ViewGeneralRuleResponse
     *
     * @param rule Rule
     * @return ViewGeneralRuleResponse
     */
    private ViewGeneralRuleResponse getRuleResponseProcess(GeneralRule rule) {
        ViewGeneralRuleResponse ruleResponse = new ViewGeneralRuleResponse();
        ruleResponse.setId(rule.getId());
        ruleResponse.setName(rule.getName());
        ruleResponse.setCode(rule.getCode());
        ruleResponse.setWorkspaceId(rule.getWorkspaceId());
        ruleResponse.setWorkspaceCode(rule.getWorkspaceCode());
        ruleResponse.setDescription(rule.getDescription());
        List<ConditionGroup> conditionGroups = rule.getConditionSet().getConditionGroups();
        ruleResponse.setConditionGroup(this.ruleEngineConditionGroupService.pressConditionGroupConfig(conditionGroups));
        ruleResponse.setAction(valueResolve.getConfigValue(rule.getActionValue()));
        DefaultAction defaultAction;
        if (rule.getDefaultActionValue() != null) {
            defaultAction = new DefaultAction(this.valueResolve.getConfigValue(rule.getDefaultActionValue()));
            defaultAction.setEnableDefaultAction(EnableEnum.ENABLE.getStatus());
        } else {
            defaultAction = new DefaultAction();
            defaultAction.setEnableDefaultAction(EnableEnum.DISABLE.getStatus());
        }
        ruleResponse.setDefaultAction(defaultAction);
        // 规则调用接口，以及规则入参
        ruleResponse.setParameters(this.parameterService.getParameters(rule));
        return ruleResponse;
    }


}
