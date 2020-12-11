package cn.ruleengine.web.vo.base.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/9/30
 * @since 1.0.0
 */
@Data
@ApiModel
public class PageBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("每页数量")
    private long pageSize = 10;

    @ApiModelProperty("当前页")
    private long pageIndex = 1;
}
