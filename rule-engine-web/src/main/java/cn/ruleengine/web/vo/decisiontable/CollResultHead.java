package cn.ruleengine.web.vo.decisiontable;

import cn.ruleengine.web.vo.condition.ConfigValue;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2021/1/1
 * @since 1.0.0
 */
@Data
public class CollResultHead {
    private Integer type;
    private String valueType;
    private ConfigValue defaultAction = new ConfigValue();
}
