package cn.ruleengine.web.service.generalrule.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.ruleengine.core.GeneralRuleEngine;
import cn.ruleengine.core.rule.GeneralRule;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.EnableEnum;
import cn.ruleengine.web.enums.DataStatus;
import cn.ruleengine.web.listener.body.GeneralRuleMessageBody;
import cn.ruleengine.web.listener.event.GeneralRuleEvent;
import cn.ruleengine.web.service.*;
import cn.ruleengine.web.service.generalrule.GeneralRuleService;
import cn.ruleengine.web.store.entity.*;
import cn.ruleengine.web.store.manager.*;
import cn.ruleengine.web.store.mapper.RuleEngineRuleMapper;
import cn.ruleengine.web.store.mapper.RuleEngineGeneralRuleMapper;

import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.util.VersionUtils;
import cn.ruleengine.web.vo.common.ViewRequest;
import cn.ruleengine.web.vo.condition.*;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.base.PageRequest;
import cn.ruleengine.web.vo.base.PageBase;
import cn.ruleengine.web.vo.base.PageResult;
import cn.ruleengine.web.vo.generalrule.*;
import cn.ruleengine.core.condition.ConditionGroup;

import cn.ruleengine.web.vo.generalrule.DefaultAction;


import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.exception.ValidException;

import com.alibaba.fastjson.JSON;
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
    private GeneralRuleEngine generalRuleEngine;
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
    private ConditionSetService conditionSetService;
    @Resource
    private ReferenceDataService referenceDataService;

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
            // 遗留bug修复
            if (Validator.isNotEmpty(query.getStatus())) {
                if (query.getStatus().equals(DataStatus.PUBLISHED.getStatus())) {
                    wrapper.lambda().isNotNull(RuleEngineGeneralRule::getPublishVersion);
                } else {
                    wrapper.lambda().eq(RuleEngineGeneralRule::getStatus, query.getStatus());
                }
            }
            return wrapper;
        }, m -> {
            ListGeneralRuleResponse listRuleResponse = new ListGeneralRuleResponse();
            BeanUtil.copyProperties(m, listRuleResponse);
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
     * @param generalRuleBody 规则配置数据
     * @return true执行成功
     */
    @Override
    public Boolean updateRule(GeneralRuleBody generalRuleBody) {
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(generalRuleBody.getId());
        if (ruleEngineGeneralRule == null) {
            throw new ValidException("不存在规则:{}", generalRuleBody.getId());
        }
        // 如果之前是待发布，则删除原有待发布数据
        if (Objects.equals(ruleEngineGeneralRule.getStatus(), DataStatus.WAIT_PUBLISH.getStatus())) {
            this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                    .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                    .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, generalRuleBody.getId())
                    .remove();
        }
        // 如果原来有条件信息，先删除原有信息
        this.ruleEngineConditionGroupService.removeConditionGroupByRuleIds(Collections.singletonList(ruleEngineGeneralRule.getRuleId()));
        // 保存条件信息
        this.ruleEngineConditionGroupService.saveConditionGroup(ruleEngineGeneralRule.getRuleId(), generalRuleBody.getConditionGroup());
        //  更新规则信息
        ruleEngineGeneralRule.setId(generalRuleBody.getId());
        ruleEngineGeneralRule.setStatus(DataStatus.EDIT.getStatus());
        // 保存规则结果
        this.saveAction(ruleEngineGeneralRule.getRuleId(), generalRuleBody.getAction());
        // 保存默认结果
        DefaultAction defaultAction = generalRuleBody.getDefaultAction();
        ruleEngineGeneralRule.setEnableDefaultAction(defaultAction.getEnableDefaultAction());
        ruleEngineGeneralRule.setDefaultActionValue(defaultAction.getValue());
        ruleEngineGeneralRule.setDefaultActionValueType(defaultAction.getValueType());
        ruleEngineGeneralRule.setDefaultActionType(defaultAction.getType());
        ruleEngineGeneralRule.setReferenceData(JSON.toJSONString(referenceDataService.countReferenceData(generalRuleBody)));
        this.ruleEngineGeneralRuleMapper.updateRuleById(ruleEngineGeneralRule);
        return true;
    }

    /**
     * 保存结果
     *
     * @param ruleId 规则id
     * @param action 结果
     */
    private void saveAction(Integer ruleId, ConfigValue action) {
        RuleEngineRule ruleEngineRule = new RuleEngineRule();
        ruleEngineRule.setId(ruleId);
        ruleEngineRule.setActionType(action.getType());
        ruleEngineRule.setActionValueType(action.getValueType());
        ruleEngineRule.setActionValue(action.getValue());
        this.ruleEngineRuleMapper.updateRuleById(ruleEngineRule);
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
        if (this.generalRuleEngine.isExists(ruleEngineGeneralRule.getWorkspaceCode(), ruleEngineGeneralRule.getCode())) {
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
     * @param generalRuleBody 规则配置数据
     * @return true
     */
    @Override
    public Boolean generationRelease(GeneralRuleBody generalRuleBody) {
        // 更新规则
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(generalRuleBody.getId());
        if (ruleEngineGeneralRule == null) {
            throw new ValidException("不存在规则:{}", generalRuleBody.getId());
        }
        // 如果开启了默认结果
        DefaultAction defaultAction = generalRuleBody.getDefaultAction();
        defaultAction.valid();
        // 如果原来有条件信息，先删除原有信息
        this.ruleEngineConditionGroupService.removeConditionGroupByRuleIds(Collections.singletonList(ruleEngineGeneralRule.getRuleId()));
        // 保存条件信息
        this.ruleEngineConditionGroupService.saveConditionGroup(ruleEngineGeneralRule.getRuleId(), generalRuleBody.getConditionGroup());
        // 更新版本
        if (StrUtil.isBlank(ruleEngineGeneralRule.getCurrentVersion())) {
            ruleEngineGeneralRule.setCurrentVersion(VersionUtils.INIT_VERSION);
        } else {
            // 如果待发布与已经发布版本一致，则需要更新一个版本号
            if (ruleEngineGeneralRule.getCurrentVersion().equals(ruleEngineGeneralRule.getPublishVersion())) {
                // 获取下一个版本
                ruleEngineGeneralRule.setCurrentVersion(VersionUtils.getNextVersion(ruleEngineGeneralRule.getCurrentVersion()));
            }
        }
        //  更新规则信息
        ruleEngineGeneralRule.setStatus(DataStatus.WAIT_PUBLISH.getStatus());
        // 保存结果
        ConfigValue action = generalRuleBody.getAction();
        this.saveAction(ruleEngineGeneralRule.getRuleId(), generalRuleBody.getAction());
        // 保存默认结果
        ruleEngineGeneralRule.setEnableDefaultAction(defaultAction.getEnableDefaultAction());
        ruleEngineGeneralRule.setDefaultActionValue(defaultAction.getValue());
        ruleEngineGeneralRule.setDefaultActionValueType(defaultAction.getValueType());
        ruleEngineGeneralRule.setDefaultActionType(defaultAction.getType());
        String referenceData = JSON.toJSONString(referenceDataService.countReferenceData(generalRuleBody));
        ruleEngineGeneralRule.setReferenceData(referenceData);
        this.ruleEngineGeneralRuleMapper.updateRuleById(ruleEngineGeneralRule);
        // 添加新的待发布数据
        GeneralRule generalRule = new GeneralRule();
        generalRule.setId(generalRuleBody.getId());
        generalRule.setCode(ruleEngineGeneralRule.getCode());
        generalRule.setName(ruleEngineGeneralRule.getName());
        generalRule.setWorkspaceCode(ruleEngineGeneralRule.getWorkspaceCode());
        generalRule.setWorkspaceId(ruleEngineGeneralRule.getWorkspaceId());
        generalRule.setDescription(ruleEngineGeneralRule.getDescription());
        generalRule.setConditionSet(this.conditionSetService.loadConditionSet(generalRuleBody.getConditionGroup()));
        generalRule.setActionValue(this.valueResolve.getValue(action.getType(), action.getValueType(), action.getValue()));
        // 如果启用了默认结果
        if (EnableEnum.ENABLE.getStatus().equals(defaultAction.getEnableDefaultAction())) {
            generalRule.setDefaultActionValue(this.valueResolve.getValue(defaultAction.getType(), defaultAction.getValueType(), defaultAction.getValue()));
        }
        // 将不再判断是否存在待发布，直接执行删除sql
        this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, ruleEngineGeneralRule.getId())
                .remove();
        // 生成待发布规则
        RuleEngineGeneralRulePublish rulePublish = new RuleEngineGeneralRulePublish();
        rulePublish.setGeneralRuleId(generalRule.getId());
        rulePublish.setGeneralRuleCode(generalRule.getCode());
        rulePublish.setData(generalRule.toJson());
        rulePublish.setStatus(DataStatus.WAIT_PUBLISH.getStatus());
        rulePublish.setWorkspaceId(generalRule.getWorkspaceId());
        // add version
        rulePublish.setVersion(ruleEngineGeneralRule.getCurrentVersion());
        rulePublish.setWorkspaceCode(generalRule.getWorkspaceCode());
        rulePublish.setReferenceData(referenceData);
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
                // 更新发布的版本号
                .set(RuleEngineGeneralRule::getPublishVersion, ruleEngineGeneralRule.getCurrentVersion())
                .eq(RuleEngineGeneralRule::getId, ruleEngineGeneralRule.getId())
                .update();
        /*
         * 删除原有的已发布规则数据
         * 修改为，原有已发布为历史版本，后期准备回退
         */
        this.ruleEngineGeneralRulePublishManager.lambdaUpdate()
                .set(RuleEngineGeneralRulePublish::getStatus, DataStatus.HISTORY_PUBLISHED.getStatus())
                .eq(RuleEngineGeneralRulePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineGeneralRulePublish::getGeneralRuleId, ruleEngineGeneralRule.getId())
                .update();
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
        ConfigValue action = this.valueResolve.getConfigValue(ruleEngineRule.getActionValue(), ruleEngineRule.getActionType(), ruleEngineRule.getActionValueType());
        ruleResponse.setAction(action);
        // 默认结果
        ConfigValue defaultValue = this.valueResolve.getConfigValue(ruleEngineGeneralRule.getDefaultActionValue(), ruleEngineGeneralRule.getDefaultActionType(), ruleEngineGeneralRule.getDefaultActionValueType());
        DefaultAction defaultAction = new DefaultAction(defaultValue);
        defaultAction.setEnableDefaultAction(ruleEngineGeneralRule.getEnableDefaultAction());
        ruleResponse.setDefaultAction(defaultAction);
        return ruleResponse;
    }

    /**
     * 规则预览
     *
     * @param viewRequest 规则id
     * @return GetRuleResponse
     */
    @Override
    public ViewGeneralRuleResponse view(ViewRequest viewRequest) {
        Integer id = viewRequest.getId();
        RuleEngineGeneralRule ruleEngineGeneralRule = this.ruleEngineGeneralRuleManager.getById(id);
        if (ruleEngineGeneralRule == null) {
            throw new ValidException("找不到预览的规则数据:{}", id);
        }
        if (ruleEngineGeneralRule.getStatus().equals(DataStatus.PUBLISHED.getStatus()) || viewRequest.getStatus().equals(DataStatus.PUBLISHED.getStatus())) {
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
