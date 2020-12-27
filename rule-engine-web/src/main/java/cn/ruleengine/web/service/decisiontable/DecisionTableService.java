package cn.ruleengine.web.service.decisiontable;

import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.decisiontable.DecisionTableDefinition;
import cn.ruleengine.web.vo.decisiontable.ListDecisionTableRequest;
import cn.ruleengine.web.vo.decisiontable.ListDecisionTableResponse;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/27
 * @since 1.0.0
 */
public interface DecisionTableService {

    /**
     * 决策表列表
     *
     * @param pageRequest 分页查询参数
     * @return page
     */
    PageResult<ListDecisionTableResponse> list(PageRequest<ListDecisionTableRequest> pageRequest);

    /**
     * 保存或者更新决策表定义信息
     *
     * @param decisionTableDefinition 定义信息
     * @return 决策表id
     */
    Integer saveOrUpdateDecisionTableDefinition(DecisionTableDefinition decisionTableDefinition);

    /**
     * 决策表code是否存在
     *
     * @param code 规则code
     * @return true存在
     */
    Boolean decisionTableCodeIsExists(String code);

    /**
     * 查询决策表定义信息
     *
     * @param id 决策表id
     * @return DecisionTableDefinition
     */
    DecisionTableDefinition getDecisionTableDefinition(Integer id);

}