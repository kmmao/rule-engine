package cn.ruleengine.web.vo.decisiontable;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2021/1/3
 * @since 1.0.0
 */
@Data
public class GenerationReleaseRequest {

    @NotNull(message = "决策表Id不能为空")
    private Integer id;

    /**
     * 决策表执行策略类型
     */
    @NotNull(message = "决策表执行策略不能为空")
    private Integer strategyType = 1;

    /**
     * 决策表数据
     */
    @Valid
    private TableData tableData;

}
