package cn.ruleengine.client.param;

import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/11
 * @since 1.0.0
 */
@Data
public class IsExistsParam {

    private String ruleCode;

    private String workspaceCode;

    private String accessKeyId;

    private String accessKeySecret;

}
