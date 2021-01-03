package cn.ruleengine.web.vo.decisiontable;

import lombok.Data;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2021/1/1
 * @since 1.0.0
 */
@Data
public class TableData {

    /**
     * 条件头
     */
    @Valid
    private List<CollConditionHeads> collConditionHeads = new ArrayList<>();

    /**
     * 结果头
     */
    @Valid
    private CollResultHead collResultHead = new CollResultHead();
    /**
     * 规则行
     */
    private List<Rows> rows = new ArrayList<>();

}
