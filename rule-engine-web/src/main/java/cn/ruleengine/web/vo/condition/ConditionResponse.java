package cn.ruleengine.web.vo.condition;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/7/14
 * @since 1.0.0
 */
@Data
public class ConditionResponse {

    private Integer id;

    private String name;

    private String description;

    private ConfigBean config;
}
