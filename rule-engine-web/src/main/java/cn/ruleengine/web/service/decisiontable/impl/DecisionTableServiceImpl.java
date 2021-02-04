package cn.ruleengine.web.service.decisiontable.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.ruleengine.core.DecisionTableEngine;
import cn.ruleengine.core.decisiontable.*;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.core.value.Value;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.DataStatus;
import cn.ruleengine.web.listener.body.DecisionTableMessageBody;
import cn.ruleengine.web.listener.event.DecisionTableEvent;
import cn.ruleengine.web.service.ReferenceDataService;
import cn.ruleengine.web.service.ValueResolve;
import cn.ruleengine.web.service.decisiontable.DecisionTableResolveService;
import cn.ruleengine.web.service.decisiontable.DecisionTableService;
import cn.ruleengine.web.service.ParameterService;
import cn.ruleengine.web.store.entity.RuleEngineDecisionTable;
import cn.ruleengine.web.store.entity.RuleEngineDecisionTablePublish;
import cn.ruleengine.web.store.manager.RuleEngineDecisionTableManager;
import cn.ruleengine.web.store.manager.RuleEngineDecisionTablePublishManager;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.util.VersionUtils;
import cn.ruleengine.web.vo.base.PageRequest;
import cn.ruleengine.web.vo.base.PageBase;
import cn.ruleengine.web.vo.base.PageResult;
import cn.ruleengine.web.vo.common.ViewRequest;
import cn.ruleengine.web.vo.condition.ConfigValue;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.decisiontable.*;
import cn.ruleengine.web.vo.generalrule.DefaultAction;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.Workspace;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/27
 * @since 1.0.0
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class DecisionTableServiceImpl implements DecisionTableService {

    @Resource
    private RuleEngineDecisionTableManager ruleEngineDecisionTableManager;
    @Resource
    private RuleEngineDecisionTablePublishManager ruleEngineDecisionTablePublishManager;
    @Resource
    private DecisionTableEngine decisionTableEngine;
    @Resource
    private ApplicationEventPublisher eventPublisher;
    @Resource
    private DecisionTableResolveService decisionTableResolveService;
    @Resource
    private ValueResolve valueResolve;
    @Resource
    private ParameterService parameterService;
    @Resource
    private ReferenceDataService referenceDataService;

    /**
     * 决策表列表
     *
     * @param pageRequest 分页查询参数
     * @return page
     */
    @Override
    public PageResult<ListDecisionTableResponse> list(PageRequest<ListDecisionTableRequest> pageRequest) {
        List<PageRequest.OrderBy> orders = pageRequest.getOrders();
        PageBase page = pageRequest.getPage();
        Workspace workspace = Context.getCurrentWorkspace();
        return PageUtils.page(this.ruleEngineDecisionTableManager, page, () -> {
            QueryWrapper<RuleEngineDecisionTable> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(RuleEngineDecisionTable::getWorkspaceId, workspace.getId());
            ListDecisionTableRequest query = pageRequest.getQuery();
            if (Validator.isNotEmpty(query.getName())) {
                wrapper.lambda().like(RuleEngineDecisionTable::getName, query.getName());
            }
            // 遗留bug修复
            if (Validator.isNotEmpty(query.getStatus())) {
                if (query.getStatus().equals(DataStatus.PUBLISHED.getStatus())) {
                    wrapper.lambda().isNotNull(RuleEngineDecisionTable::getPublishVersion);
                } else {
                    wrapper.lambda().eq(RuleEngineDecisionTable::getStatus, query.getStatus());
                }
            }
            if (Validator.isNotEmpty(query.getCode())) {
                wrapper.lambda().like(RuleEngineDecisionTable::getCode, query.getCode());
            }
            PageUtils.defaultOrder(orders, wrapper);
            return wrapper;
        }, m -> {
            ListDecisionTableResponse decisionTableResponse = new ListDecisionTableResponse();
            decisionTableResponse.setId(m.getId());
            decisionTableResponse.setCode(m.getCode());
            decisionTableResponse.setName(m.getName());
            decisionTableResponse.setCurrentVersion(m.getCurrentVersion());
            decisionTableResponse.setPublishVersion(m.getPublishVersion());
            decisionTableResponse.setStatus(m.getStatus());
            decisionTableResponse.setCreateTime(m.getCreateTime());
            return decisionTableResponse;
        });
    }

    /**
     * 保存或者更新决策表定义信息
     *
     * @param decisionTableDefinition 定义信息
     * @return 决策表id
     */
    @Override
    public Integer saveOrUpdateDecisionTableDefinition(DecisionTableDefinition decisionTableDefinition) {
        RuleEngineDecisionTable ruleEngineDecisionTable = new RuleEngineDecisionTable();
        if (decisionTableDefinition.getId() == null) {
            if (this.decisionTableCodeIsExists(decisionTableDefinition.getCode())) {
                throw new ValidException("决策表Code：{}已经存在", decisionTableDefinition.getCode());
            }
            Workspace workspace = Context.getCurrentWorkspace();
            UserData userData = Context.getCurrentUser();
            ruleEngineDecisionTable.setCreateUserId(userData.getId());
            ruleEngineDecisionTable.setCreateUserName(userData.getUsername());
            ruleEngineDecisionTable.setWorkspaceId(workspace.getId());
            ruleEngineDecisionTable.setWorkspaceCode(workspace.getCode());
            // 初始化决策表
            TableData tableData = new TableData();
            tableData.getCollConditionHeads().add(new CollConditionHeads());
            Rows rows = new Rows();
            rows.getConditions().add(new ConfigValue());
            tableData.getRows().add(rows);
            ruleEngineDecisionTable.setTableData(JSON.toJSONString(tableData));
            // 默认执行策略
            ruleEngineDecisionTable.setStrategyType(DecisionTableStrategyType.ALL_PRIORITY.getValue());
        } else {
            ruleEngineDecisionTable = this.ruleEngineDecisionTableManager.lambdaQuery()
                    .eq(RuleEngineDecisionTable::getId, decisionTableDefinition.getId())
                    .one();
            if (ruleEngineDecisionTable == null) {
                throw new ValidException("不存在决策表:{}", decisionTableDefinition.getId());
            } else {
                if (Objects.equals(ruleEngineDecisionTable.getStatus(), DataStatus.WAIT_PUBLISH.getStatus())) {
                    // 删除原有待发布规则
                    this.ruleEngineDecisionTablePublishManager.lambdaUpdate()
                            .eq(RuleEngineDecisionTablePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                            .eq(RuleEngineDecisionTablePublish::getDecisionTableId, ruleEngineDecisionTable.getId())
                            .remove();
                }
            }
        }
        ruleEngineDecisionTable.setId(decisionTableDefinition.getId());
        ruleEngineDecisionTable.setName(decisionTableDefinition.getName());
        ruleEngineDecisionTable.setCode(decisionTableDefinition.getCode());
        ruleEngineDecisionTable.setDescription(decisionTableDefinition.getDescription());
        ruleEngineDecisionTable.setStatus(DataStatus.EDIT.getStatus());
        this.ruleEngineDecisionTableManager.saveOrUpdate(ruleEngineDecisionTable);
        return ruleEngineDecisionTable.getId();
    }

    /**
     * 决策表code是否存在
     *
     * @param code 规则code
     * @return true存在
     */
    @Override
    public Boolean decisionTableCodeIsExists(String code) {
        Workspace workspace = Context.getCurrentWorkspace();
        Integer count = this.ruleEngineDecisionTableManager.lambdaQuery()
                .eq(RuleEngineDecisionTable::getWorkspaceId, workspace.getId())
                .eq(RuleEngineDecisionTable::getCode, code)
                .count();
        return count != null && count >= 1;
    }

    /**
     * 查询决策表定义信息
     *
     * @param id 决策表id
     * @return DecisionTableDefinition
     */
    @Override
    public DecisionTableDefinition getDecisionTableDefinition(Integer id) {
        RuleEngineDecisionTable ruleEngineDecisionTable = this.ruleEngineDecisionTableManager.lambdaQuery()
                .eq(RuleEngineDecisionTable::getId, id)
                .one();
        if (ruleEngineDecisionTable == null) {
            return null;
        }
        return BasicConversion.INSTANCE.convert(ruleEngineDecisionTable);
    }

    /**
     * 删除决策表
     *
     * @param id 决策表id
     * @return true
     */
    @Override
    public Boolean delete(Integer id) {
        RuleEngineDecisionTable ruleEngineDecisionTable = this.ruleEngineDecisionTableManager.getById(id);
        if (ruleEngineDecisionTable == null) {
            return false;
        }
        // 从引擎中移除规则
        if (this.decisionTableEngine.isExists(ruleEngineDecisionTable.getWorkspaceCode(), ruleEngineDecisionTable.getCode())) {
            DecisionTableMessageBody decisionTableMessageBody = new DecisionTableMessageBody();
            decisionTableMessageBody.setType(DecisionTableMessageBody.Type.REMOVE);
            decisionTableMessageBody.setWorkspaceId(ruleEngineDecisionTable.getWorkspaceId());
            decisionTableMessageBody.setWorkspaceCode(ruleEngineDecisionTable.getWorkspaceCode());
            decisionTableMessageBody.setDecisionTableCode(ruleEngineDecisionTable.getCode());
            this.eventPublisher.publishEvent(new DecisionTableEvent(decisionTableMessageBody));
        }
        this.ruleEngineDecisionTablePublishManager.lambdaUpdate().eq(RuleEngineDecisionTablePublish::getDecisionTableId, id).remove();
        return this.ruleEngineDecisionTableManager.removeById(id);
    }

    /**
     * 更新决策表信息
     *
     * @param updateDecisionTableRequest 决策表配置数据
     * @return true执行成功
     */
    @Override
    public Boolean updateDecisionTable(UpdateDecisionTableRequest updateDecisionTableRequest) {
        Integer decisionTableId = updateDecisionTableRequest.getId();
        RuleEngineDecisionTable ruleEngineDecisionTable = this.ruleEngineDecisionTableManager.getById(decisionTableId);
        if (ruleEngineDecisionTable == null) {
            throw new ValidException("不存在决策表:{}", decisionTableId);
        }
        if (Objects.equals(ruleEngineDecisionTable.getStatus(), DataStatus.WAIT_PUBLISH.getStatus())) {
            this.ruleEngineDecisionTablePublishManager.lambdaUpdate()
                    .eq(RuleEngineDecisionTablePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                    .eq(RuleEngineDecisionTablePublish::getDecisionTableId, decisionTableId)
                    .remove();
        }
        ruleEngineDecisionTable.setStrategyType(updateDecisionTableRequest.getStrategyType());
        ruleEngineDecisionTable.setStatus(DataStatus.EDIT.getStatus());
        ruleEngineDecisionTable.setTableData(JSON.toJSONString(updateDecisionTableRequest.getTableData()));
        ruleEngineDecisionTable.setReferenceData(JSON.toJSONString(referenceDataService.countReferenceData(updateDecisionTableRequest.getTableData())));
        return this.ruleEngineDecisionTableManager.updateById(ruleEngineDecisionTable);
    }

    /**
     * 获取决策表信息
     *
     * @param id 决策表id
     * @return 决策表信息
     */
    @Override
    public GetDecisionTableResponse getDecisionTableConfig(Integer id) {
        RuleEngineDecisionTable ruleEngineDecisionTable = this.ruleEngineDecisionTableManager.getById(id);
        if (ruleEngineDecisionTable == null) {
            return null;
        }
        GetDecisionTableResponse decisionTableResponse = new GetDecisionTableResponse();
        decisionTableResponse.setId(id);
        decisionTableResponse.setName(ruleEngineDecisionTable.getName());
        decisionTableResponse.setCode(ruleEngineDecisionTable.getCode());
        decisionTableResponse.setDescription(ruleEngineDecisionTable.getDescription());
        decisionTableResponse.setWorkspaceId(ruleEngineDecisionTable.getWorkspaceId());
        decisionTableResponse.setWorkspaceCode(ruleEngineDecisionTable.getWorkspaceCode());
        decisionTableResponse.setTableData(JSON.parseObject(ruleEngineDecisionTable.getTableData(), TableData.class));
        decisionTableResponse.setStrategyType(ruleEngineDecisionTable.getStrategyType());
        return decisionTableResponse;
    }

    /**
     * 生成决策表代发布
     *
     * @param releaseRequest 配置数据
     * @return true
     */
    @Override
    public Boolean generationRelease(GenerationReleaseRequest releaseRequest) {
        // 校验是否配置完善
        DefaultAction defaultAction = releaseRequest.getTableData().getCollResultHead().getDefaultAction();
        defaultAction.valid();
        RuleEngineDecisionTable ruleEngineDecisionTable = this.ruleEngineDecisionTableManager.getById(releaseRequest.getId());
        if (ruleEngineDecisionTable == null) {
            throw new ValidException("不存在决策表:{}", releaseRequest.getId());
        }
        // 更新版本
        if (StrUtil.isBlank(ruleEngineDecisionTable.getCurrentVersion())) {
            ruleEngineDecisionTable.setCurrentVersion(VersionUtils.INIT_VERSION);
        } else {
            // 如果待发布与已经发布版本一致，则需要更新一个版本号
            if (ruleEngineDecisionTable.getCurrentVersion().equals(ruleEngineDecisionTable.getPublishVersion())) {
                // 获取下一个版本
                ruleEngineDecisionTable.setCurrentVersion(VersionUtils.getNextVersion(ruleEngineDecisionTable.getCurrentVersion()));
            }
        }
        ruleEngineDecisionTable.setStrategyType(releaseRequest.getStrategyType());
        ruleEngineDecisionTable.setStatus(DataStatus.WAIT_PUBLISH.getStatus());
        ruleEngineDecisionTable.setTableData(JSON.toJSONString(releaseRequest.getTableData()));
        String referenceDataJson = JSON.toJSONString(referenceDataService.countReferenceData(releaseRequest.getTableData()));
        ruleEngineDecisionTable.setReferenceData(referenceDataJson);
        this.ruleEngineDecisionTableManager.updateById(ruleEngineDecisionTable);
        // 删除原有待发布规则
        this.ruleEngineDecisionTablePublishManager.lambdaUpdate()
                .eq(RuleEngineDecisionTablePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                .eq(RuleEngineDecisionTablePublish::getDecisionTableId, ruleEngineDecisionTable.getId())
                .remove();
        // 生成新的待发布
        RuleEngineDecisionTablePublish ruleEngineDecisionTablePublish = new RuleEngineDecisionTablePublish();
        ruleEngineDecisionTablePublish.setDecisionTableId(ruleEngineDecisionTable.getId());
        ruleEngineDecisionTablePublish.setDecisionTableCode(ruleEngineDecisionTable.getCode());
        ruleEngineDecisionTablePublish.setWorkspaceId(ruleEngineDecisionTable.getWorkspaceId());
        ruleEngineDecisionTablePublish.setWorkspaceCode(ruleEngineDecisionTable.getWorkspaceCode());
        DecisionTable decisionTable = this.decisionTableResolveService.decisionTableProcess(ruleEngineDecisionTable);
        ruleEngineDecisionTablePublish.setData(decisionTable.toJson());
        ruleEngineDecisionTablePublish.setStatus(ruleEngineDecisionTable.getStatus());
        ruleEngineDecisionTablePublish.setReferenceData(referenceDataJson);
        // add version
        ruleEngineDecisionTablePublish.setVersion(ruleEngineDecisionTable.getCurrentVersion());
        this.ruleEngineDecisionTablePublishManager.save(ruleEngineDecisionTablePublish);
        return true;
    }

    /**
     * 获取决策表展示信息
     *
     * @param viewRequest 决策表id
     * @return ViewDecisionTableResponse
     */
    @Override
    public ViewDecisionTableResponse view(ViewRequest viewRequest) {
        Integer id = viewRequest.getId();
        RuleEngineDecisionTable ruleEngineDecisionTable = this.ruleEngineDecisionTableManager.getById(id);
        if (ruleEngineDecisionTable == null) {
            throw new ValidException("找不到预览的规则数据:{}", id);
        }
        // 如果只有已发布
        if (ruleEngineDecisionTable.getStatus().equals(DataStatus.PUBLISHED.getStatus()) || viewRequest.getType().equals(DataStatus.PUBLISHED.getStatus())) {
            RuleEngineDecisionTablePublish ruleEngineDecisionTablePublish = this.ruleEngineDecisionTablePublishManager.lambdaQuery()
                    .eq(RuleEngineDecisionTablePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                    .eq(RuleEngineDecisionTablePublish::getDecisionTableId, id)
                    .one();
            if (ruleEngineDecisionTablePublish == null) {
                throw new ValidException("找不到发布的规则:{}", id);
            }
            String data = ruleEngineDecisionTablePublish.getData();
            DecisionTable decisionTable = DecisionTable.buildDecisionTable(data);
            return this.decisionTableResponseProcess(decisionTable);
        }
        RuleEngineDecisionTablePublish ruleEngineDecisionTablePublish = this.ruleEngineDecisionTablePublishManager.lambdaQuery()
                .eq(RuleEngineDecisionTablePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                .eq(RuleEngineDecisionTablePublish::getDecisionTableId, id)
                .one();
        if (ruleEngineDecisionTablePublish == null) {
            throw new ValidException("找不到预览的规则数据:{}", id);
        }
        String data = ruleEngineDecisionTablePublish.getData();
        DecisionTable decisionTable = DecisionTable.buildDecisionTable(data);
        return this.decisionTableResponseProcess(decisionTable);
    }

    /**
     * 规则决策表
     *
     * @param id 决策表id
     * @return true
     */
    @Override
    public Boolean publish(Integer id) {
        RuleEngineDecisionTable ruleEngineDecisionTable = this.ruleEngineDecisionTableManager.getById(id);
        if (ruleEngineDecisionTable == null) {
            throw new ValidException("不存在决策表:{}", id);
        }
        if (ruleEngineDecisionTable.getStatus().equals(DataStatus.EDIT.getStatus())) {
            throw new ValidException("该决策表不可执行:{}", id);
        }
        // 如果已经是发布决策表了
        if (ruleEngineDecisionTable.getStatus().equals(DataStatus.PUBLISHED.getStatus())) {
            return true;
        }
        // 修改为已发布
        this.ruleEngineDecisionTableManager.lambdaUpdate()
                .set(RuleEngineDecisionTable::getStatus, DataStatus.PUBLISHED.getStatus())
                // 更新发布的版本号
                .set(RuleEngineDecisionTable::getPublishVersion, ruleEngineDecisionTable.getCurrentVersion())
                .eq(RuleEngineDecisionTable::getId, ruleEngineDecisionTable.getId())
                .update();
        /*
         * 删除原有的已发布决策表数据
         * 修改为，原有已发布为历史版本，后期准备回退
         */
        this.ruleEngineDecisionTablePublishManager.lambdaUpdate()
                .set(RuleEngineDecisionTablePublish::getStatus, DataStatus.HISTORY_PUBLISHED.getStatus())
                .eq(RuleEngineDecisionTablePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineDecisionTablePublish::getDecisionTableId, ruleEngineDecisionTable.getId())
                .update();
        // 更新待发布为已发布
        this.ruleEngineDecisionTablePublishManager.lambdaUpdate()
                .set(RuleEngineDecisionTablePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineDecisionTablePublish::getStatus, DataStatus.WAIT_PUBLISH.getStatus())
                .eq(RuleEngineDecisionTablePublish::getDecisionTableId, ruleEngineDecisionTable.getId())
                .update();
        // 加载规则
        DecisionTableMessageBody decisionTableMessageBody = new DecisionTableMessageBody();
        decisionTableMessageBody.setType(DecisionTableMessageBody.Type.LOAD);
        decisionTableMessageBody.setDecisionTableCode(ruleEngineDecisionTable.getCode());
        decisionTableMessageBody.setWorkspaceId(ruleEngineDecisionTable.getWorkspaceId());
        decisionTableMessageBody.setWorkspaceCode(ruleEngineDecisionTable.getWorkspaceCode());
        this.eventPublisher.publishEvent(new DecisionTableEvent(decisionTableMessageBody));
        return true;
    }

    /**
     * 处理决策表预览数据
     *
     * @param decisionTable 决策表
     * @return ViewDecisionTableResponse
     */
    private ViewDecisionTableResponse decisionTableResponseProcess(DecisionTable decisionTable) {
        ViewDecisionTableResponse decisionTableResponse = new ViewDecisionTableResponse();
        decisionTableResponse.setId(decisionTable.getId());
        decisionTableResponse.setName(decisionTable.getName());
        decisionTableResponse.setCode(decisionTable.getCode());
        decisionTableResponse.setDescription(decisionTable.getDescription());
        decisionTableResponse.setStrategyType(decisionTable.getStrategyType().getValue());
        decisionTableResponse.setWorkspaceId(decisionTable.getWorkspaceId());
        decisionTableResponse.setWorkspaceCode(decisionTable.getWorkspaceCode());
        TableData tableData = new TableData();
        List<CollHead> collHeads = decisionTable.getCollHeads();
        List<CollConditionHeads> collConditionHeads = new ArrayList<>();
        for (CollHead collHead : collHeads) {
            CollConditionHeads conditionHeads = new CollConditionHeads();
            conditionHeads.setName(collHead.getName());
            conditionHeads.setSymbol(collHead.getOperator().name());
            conditionHeads.setLeftValue(this.valueResolve.getConfigValue(collHead.getLeftValue()));
            collConditionHeads.add(conditionHeads);
        }
        tableData.setCollConditionHeads(collConditionHeads);
        Map<Integer, List<Row>> decisionTree = decisionTable.getDecisionTree();
        List<Rows> rows = new ArrayList<>();
        decisionTree.values().stream().flatMap(Collection::stream).sorted(Comparator.comparing(Row::getOrder)).forEach(f -> {
            Rows row = new Rows();
            row.setPriority(f.getPriority());
            List<Coll> colls = f.getColls();
            List<ConfigValue> conditions = new ArrayList<>();
            for (Coll coll : colls) {
                // 空的单元格
                if (coll.getRightValue() == null) {
                    conditions.add(new ConfigValue());
                } else {
                    ConfigValue configValue = this.valueResolve.getConfigValue(coll.getRightValue());
                    conditions.add(configValue);
                }
            }
            row.setConditions(conditions);
            ConfigValue result = this.valueResolve.getConfigValue(f.getAction());
            row.setResult(result);
            rows.add(row);
        });
        tableData.setRows(rows);
        CollResultHead collResultHead = new CollResultHead();
        Value defaultActionValue = decisionTable.getDefaultActionValue();
        DefaultAction defaultAction;
        if (defaultActionValue != null) {
            ConfigValue configValue = this.valueResolve.getConfigValue(defaultActionValue);
            defaultAction = new DefaultAction(configValue);
        } else {
            defaultAction = new DefaultAction();
        }
        collResultHead.setDefaultAction(defaultAction);
        tableData.setCollResultHead(collResultHead);
        decisionTableResponse.setTableData(tableData);
        decisionTableResponse.setParameters(this.parameterService.getParameters(decisionTable));
        return decisionTableResponse;
    }

}
