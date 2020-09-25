package com.engine.web.function;

import cn.hutool.core.lang.Validator;
import com.engine.core.annotation.Executor;
import com.engine.core.annotation.Function;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/4/7
 * @since 1.0.0
 */
@Function
public class IsEmptyFunction {


    @Executor
    public Boolean executor(String value) {
        return Validator.isEmpty(value);
    }

}
