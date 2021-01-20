package cn.ruleengine.web.vo.decisiontable;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/31
 * @since 1.0.0
 */
@Data
public class UpdateDecisionTableRequest {

    @NotNull
    private Integer id;

    /**
     * 决策表执行策略类型
     */
    private Integer strategyType = 1;


    /**
     * 决策表数据
     */
    private Object tableData;

}
