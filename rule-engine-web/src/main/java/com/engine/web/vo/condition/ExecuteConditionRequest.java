package com.engine.web.vo.condition;

import com.engine.web.vo.variable.ParamValue;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/12
 * @since 1.0.0
 */
@Data
public class ExecuteConditionRequest {

    @NotNull
    private Integer id;

    /**
     * 运行入参
     */
    private List<ParamValue> paramValues = new ArrayList<>();

}
