package com.engine.web.vo.rule;

import com.engine.web.vo.condition.ConfigBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/3
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultAction extends ConfigBean.Value {

    /**
     * 0启用 1不启用
     */
    private Integer enableDefaultAction;

}
