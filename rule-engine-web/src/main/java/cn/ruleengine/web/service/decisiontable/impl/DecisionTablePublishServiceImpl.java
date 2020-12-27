package cn.ruleengine.web.service.decisiontable.impl;

import cn.ruleengine.core.decisiontable.DecisionTable;
import cn.ruleengine.web.service.decisiontable.DecisionTablePublishService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class DecisionTablePublishServiceImpl implements DecisionTablePublishService {

    @Override
    public List<DecisionTable> getAllPublishDecisionTable() {
        return new ArrayList<>();
    }

    @Override
    public DecisionTable getPublishDecisionTable(String workspaceCode, String decisionCode) {
        return null;
    }

}
