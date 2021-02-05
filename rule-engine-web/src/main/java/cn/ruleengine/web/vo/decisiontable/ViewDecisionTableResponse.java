package cn.ruleengine.web.vo.decisiontable;

import cn.ruleengine.web.vo.common.Parameter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2021/1/1
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ViewDecisionTableResponse extends GetDecisionTableResponse {

    /**
     * 规则入参
     */
    private Set<Parameter> parameters;

}
