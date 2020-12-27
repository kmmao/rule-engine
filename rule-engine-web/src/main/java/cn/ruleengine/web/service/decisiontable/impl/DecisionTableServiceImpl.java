package cn.ruleengine.web.service.decisiontable.impl;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.DecisionTableEngine;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.service.decisiontable.DecisionTableService;
import cn.ruleengine.web.store.entity.RuleEngineDecisionTable;
import cn.ruleengine.web.store.manager.RuleEngineDecisionTableManager;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageBase;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.decisiontable.ListDecisionTableRequest;
import cn.ruleengine.web.vo.decisiontable.ListDecisionTableResponse;
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

}
