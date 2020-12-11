package cn.ruleengine.web.vo.rule;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/24
 * @since 1.0.0
 */
@Data
public class ListRuleRequest {
    private String name;
    private String code;
    private Integer status;
}
