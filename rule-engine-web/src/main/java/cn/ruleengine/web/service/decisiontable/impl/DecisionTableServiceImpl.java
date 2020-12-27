package cn.ruleengine.web.service.decisiontable.impl;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.DecisionTableEngine;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.enums.DataStatus;
import cn.ruleengine.web.service.decisiontable.DecisionTableService;
import cn.ruleengine.web.store.entity.RuleEngineDecisionTable;
import cn.ruleengine.web.store.manager.RuleEngineDecisionTableManager;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageBase;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.decisiontable.DecisionTableDefinition;
import cn.ruleengine.web.vo.decisiontable.ListDecisionTableRequest;
import cn.ruleengine.web.vo.decisiontable.ListDecisionTableResponse;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.Workspace;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/27
 * @since 1.0.0
 */
@Service
public class DecisionTableServiceImpl implements DecisionTableService {

    @Resource
    private RuleEngineDecisionTableManager ruleEngineDecisionTableManager;
    @Resource
    private DecisionTableEngine decisionTableEngine;

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
            PageUtils.defaultOrder(orders, wrapper);

            ListDecisionTableRequest query = pageRequest.getQuery();
            if (Validator.isNotEmpty(query.getName())) {
                wrapper.lambda().like(RuleEngineDecisionTable::getName, query.getName());
            }
            if (Validator.isNotEmpty(query.getCode())) {
                wrapper.lambda().like(RuleEngineDecisionTable::getCode, query.getCode());
            }
            if (Validator.isNotEmpty(query.getStatus())) {
                wrapper.lambda().eq(RuleEngineDecisionTable::getStatus, query.getStatus());
            }
            return wrapper;
        }, m -> {
            ListDecisionTableResponse decisionTableResponse = new ListDecisionTableResponse();
            decisionTableResponse.setId(m.getId());
            decisionTableResponse.setName(m.getName());
            decisionTableResponse.setCode(m.getCode());
            decisionTableResponse.setIsPublish(this.decisionTableEngine.isExists(m.getWorkspaceCode(), m.getCode()));
            decisionTableResponse.setCreateUserName(m.getCreateUserName());
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
                throw new ValidException("规则Code：{}已经存在", decisionTableDefinition.getCode());
            }
            Workspace workspace = Context.getCurrentWorkspace();
            UserData userData = Context.getCurrentUser();
            ruleEngineDecisionTable.setCreateUserId(userData.getId());
            ruleEngineDecisionTable.setCreateUserName(userData.getUsername());
            ruleEngineDecisionTable.setWorkspaceId(workspace.getId());
            ruleEngineDecisionTable.setWorkspaceCode(workspace.getCode());
        } else {
            Integer count = this.ruleEngineDecisionTableManager.lambdaQuery()
                    .eq(RuleEngineDecisionTable::getId, decisionTableDefinition.getId())
                    .count();
            if (count == null || count == 0) {
                throw new ValidException("不存在规则:{}", decisionTableDefinition.getId());
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

}
