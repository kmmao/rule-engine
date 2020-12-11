package cn.ruleengine.web.vo.base.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
@AllArgsConstructor
@NoArgsConstructor
public class Rows<T> {

    @ApiModelProperty("分页数据")
    private List<T> rows;

    @ApiModelProperty("分页参数")
    private PageResponse page;

}
