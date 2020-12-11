package cn.ruleengine.web.vo.rule;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/27
 * @since 1.0.0
 */
@Data
public class RuleCountInfo {
    /**
     * 哪个规则
     */
    private Integer ruleId;

    private Set<Integer> elementIds = new HashSet<>();

    private Set<Integer> variableIds = new HashSet<>();

    private Set<Integer> conditionIds = new HashSet<>();

    public void addElementId(Integer elementId) {
        this.elementIds.add(elementId);
    }

    public void addVariableId(Integer variableId) {
        this.variableIds.add(variableId);
    }

}
