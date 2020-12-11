package cn.ruleengine.web.vo.base.request;

import cn.ruleengine.web.vo.base.response.PageBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/9/30
 * @since 1.0.0
 */
@ApiModel
@Data
public class PageRequest<T> {
    @ApiModelProperty("查询条件")
    private T query;

    @ApiModelProperty("分页参数")
    private PageBase page = new PageBase();

    @ApiModelProperty("排序条件")
    private List<OrderBy> orders = new ArrayList<>();

    @ApiModel
    @Data
    public static class OrderBy {
        @ApiModelProperty("排序列名")
        private String columnName;
        @ApiModelProperty("是否降序")
        private boolean desc;
    }
}
