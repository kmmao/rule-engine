package cn.ruleengine.web.vo.function;

import cn.ruleengine.web.vo.variable.ParamValue;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/13
 * @since 1.0.0
 */
@Data
public class RunFunction {

    /**
     * 函数id
     */
    @NotNull
    private Integer id;

    /**
     * 函数运行入参
     */
    private List<ParamValue> paramValues = new ArrayList<>();

}
