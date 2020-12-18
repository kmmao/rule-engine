package cn.ruleengine.web.vo.base.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
@ApiModel
public class PageResponse extends PageBase {

    private static final long serialVersionUID = -7378163356547441491L;

    @ApiModelProperty("总记录数")
    private Long total;

    public PageResponse(long pageIndex, long pageSize, long total) {
        super.setPageIndex(pageIndex);
        super.setPageSize(pageSize);
        this.total = total;
    }

}
