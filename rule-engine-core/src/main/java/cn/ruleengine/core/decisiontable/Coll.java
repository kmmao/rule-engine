package cn.ruleengine.core.decisiontable;


import cn.ruleengine.core.value.Constant;
import cn.ruleengine.core.value.Value;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/12/18
 * @since 1.0.0
 */
@Data
public class Coll {

    /**
     * 条件右值
     */
    private Value rightValue;

    public Coll(@NonNull Constant rightValue) {
        Objects.requireNonNull(rightValue);
        this.rightValue = rightValue;
    }

}
