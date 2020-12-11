package cn.ruleengine.web.function;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/11/19
 * @since 1.0.0
 */
@Function
public class CurrentDateFunction {

    @Executor
    public String executor(String pattern, String timeZone) {
        ZoneId zoneId = Optional.ofNullable(timeZone).filter(Validator::isNotEmpty).map(ZoneId::of).orElseGet(ZoneId::systemDefault);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();
        Date date = Date.from(instant);
        if (Validator.isEmpty(pattern)) {
            return date.toString();
        }
        return new SimpleDateFormat(pattern).format(date);
    }

}
