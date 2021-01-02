package cn.ruleengine.web.vo.decisiontable;

import cn.ruleengine.web.vo.generalrule.DefaultAction;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2021/1/1
 * @since 1.0.0
 */
@Data
public class CollResultHead {
    /**
     * 结果类型 固定值 变量
     */
    private Integer type;
    /**
     * 值类型 STRING NUMBER...
     */
    private String valueType;
    /**
     * 默认结果
     */
    private DefaultAction defaultAction = new DefaultAction();
}
