package cn.ruleengine.client.result;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/9
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExecuteRuleResult extends Result {

    /**
     * 返回数据
     */
    private OutPut data;

}
