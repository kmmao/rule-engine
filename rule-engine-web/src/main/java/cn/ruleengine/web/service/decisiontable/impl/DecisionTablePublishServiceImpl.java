package cn.ruleengine.web.service.decisiontable.impl;

import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.decisiontable.DecisionTable;
import cn.ruleengine.web.enums.DataStatus;
import cn.ruleengine.web.service.decisiontable.DecisionTablePublishService;
import cn.ruleengine.web.store.entity.RuleEngineDecisionTablePublish;
import cn.ruleengine.web.store.manager.RuleEngineDecisionTablePublishManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/27
 * @since 1.0.0
 */
@Slf4j
@Service
public class DecisionTablePublishServiceImpl implements DecisionTablePublishService {

    @Resource
    private RuleEngineDecisionTablePublishManager ruleEngineDecisionTablePublishManager;

    @Override
    public List<DecisionTable> getAllPublishDecisionTable() {
        List<RuleEngineDecisionTablePublish> decisionTablePublishes = this.ruleEngineDecisionTablePublishManager.lambdaQuery()
                .eq(RuleEngineDecisionTablePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .list();
        if (CollUtil.isEmpty(decisionTablePublishes)) {
            return Collections.emptyList();
        }
        List<DecisionTable> decisionTables = new ArrayList<>(decisionTablePublishes.size());
        for (RuleEngineDecisionTablePublish publish : decisionTablePublishes) {
            try {
                log.info("parse decisionTable for workspace code: {} decisionTable code: {}", publish.getWorkspaceCode(), publish.getDecisionTableCode());
                DecisionTable decisionTable = DecisionTable.buildDecisionTable(publish.getData());
                decisionTables.add(decisionTable);
            } catch (Exception e) {
                log.error("parse decisionTable error ", e);
            }
        }
        return decisionTables;
    }

    @Override
    public DecisionTable getPublishDecisionTable(String workspaceCode, String decisionCode) {
        RuleEngineDecisionTablePublish rulePublish = this.ruleEngineDecisionTablePublishManager.lambdaQuery()
                .eq(RuleEngineDecisionTablePublish::getStatus, DataStatus.PUBLISHED.getStatus())
                .eq(RuleEngineDecisionTablePublish::getDecisionTableCode, decisionCode)
                .eq(RuleEngineDecisionTablePublish::getWorkspaceCode, workspaceCode)
                .one();
        return DecisionTable.buildDecisionTable(rulePublish.getData());
    }

}
