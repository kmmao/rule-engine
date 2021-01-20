package cn.ruleengine.web.vo.ruleset;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/1/15
 * @since 1.0.0
 */
@Data
public class GenerationReleaseRequest {

    @NotNull
    private Integer id;

    /**
     * 决策表执行策略类型
     */
    @NotNull
    private Integer strategyType = 1;

    /**
     * 规则集
     */
    @Valid
    @NotNull
    private List<RuleBody> ruleSet = new ArrayList<>();

    private Integer enableDefaultRule;

    private RuleBody defaultRule = new RuleBody();

}
