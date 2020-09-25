package com.engine.web.function;

import cn.hutool.core.lang.Validator;
import com.engine.core.annotation.Executor;
import com.engine.core.annotation.Function;
import com.engine.core.annotation.Param;
import lombok.extern.slf4j.Slf4j;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 是否为身份证
 *
 * @author dingqianwen
 * @date 2020/8/30
 * @since 1.0.0
 */
@Slf4j
@Function
public class IsCitizenIdFunction {

    @Executor
    public Boolean executor(@Param("citizenId") String citizenId) {
        return Validator.isCitizenId(citizenId);
    }

}
