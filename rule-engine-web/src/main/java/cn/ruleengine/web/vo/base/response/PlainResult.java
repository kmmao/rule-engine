package cn.ruleengine.web.vo.base.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/9/30
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("返回普通数据")
public class PlainResult<T> extends BaseResult{

    private static final long serialVersionUID = 8794822903345524683L;

    @ApiModelProperty("数据")
    private T data;

}
