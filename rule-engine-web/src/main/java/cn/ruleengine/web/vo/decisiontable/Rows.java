package cn.ruleengine.web.vo.decisiontable;

import lombok.Data;

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
public class Rows {

    private Integer id;

    /**
     * 默认值为1
     */
    private Integer priority = 1;

    private List<CollCondition> conditions = new ArrayList<>();

    private Result result = new Result();

}
