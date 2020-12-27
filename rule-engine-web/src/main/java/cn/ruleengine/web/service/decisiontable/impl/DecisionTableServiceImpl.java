package cn.ruleengine.web.service.decisiontable.impl;

import cn.ruleengine.web.service.decisiontable.DecisionTableService;
import cn.ruleengine.web.store.manager.RuleEngineDecisionTableManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

}
