package cn.ruleengine.web.function.json;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ValidationException;
import java.math.BigDecimal;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 获取JSON中指定的值返回NUMBER类型
 *
 * @author 丁乾文
 * @create 2020/12/13
 * @since 1.0.0
 */
@Slf4j
@Function
public class ParseJsonNumberFunction implements JsonEval {

    @Executor
    public BigDecimal executor(String jsonString, String jsonValuePath) {
        Object value = this.eval(jsonString, jsonValuePath);
        // 返回null 而不是String.valueOf后的null字符
        if (Validator.isEmpty(value)) {
            return null;
        }
        String string = String.valueOf(value);
        if (!NumberUtil.isNumber(string)) {
            throw new ValidationException("从JSON获取的值无法转为NUMBER类型，请检查！");
        }
        return new BigDecimal(string);
    }

}
