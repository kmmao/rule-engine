package com.engine.web.function;

import cn.hutool.core.lang.Validator;
import com.engine.core.annotation.Executor;
import com.engine.core.annotation.Function;
import com.engine.core.annotation.Param;
import lombok.extern.slf4j.Slf4j;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 验证是否为手机号码
 *
 * @author dingqianwen
 * @date 2020/8/30
 * @since 1.0.0
 */
@Slf4j
@Function
public class IsMobileFunction {

    @Executor
    public Boolean executor(@Param("mobile") String mobile) {
        return Validator.isMobile(mobile);
    }

}
