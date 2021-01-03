package cn.ruleengine.web.vo.decisiontable;

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
public class CollCondition {

    private String value;
    private String valueName;
    private String valueType;
    private String variableValue;
    private Integer type;
    private Integer tempType;
    private Boolean visible;

}
