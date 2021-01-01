package cn.ruleengine.web.vo.decisiontable;

import cn.ruleengine.web.vo.condition.ConfigValue;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2021/1/1
 * @since 1.0.0
 */
@Data
public class CollConditionHeads {

    private String uuid;

    @NotBlank
    private String name = "条件";

    private Boolean visible;

    private String symbol;

    private ConfigValue leftValue = new ConfigValue();

}
