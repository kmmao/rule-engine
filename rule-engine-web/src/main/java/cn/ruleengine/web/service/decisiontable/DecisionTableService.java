package cn.ruleengine.web.service.decisiontable;

import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageResult;
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
}
