package cn.ruleengine.web.vo.rule;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/24
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GenerationReleaseRequest extends UpdateRuleRequest {

    @NotNull
    @Valid
    private Action action;

}
