package cn.ruleengine.web.function;

import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 字母转大写
 *
 * @author 丁乾文
 * @create 2020/12/24
 * @since 1.0.0
 */
@Function
public class LetterToUpperCaseFunction {

    @Executor
    public String executor(String letter) {
        if (letter == null) {
            return null;
        }
        return letter.toUpperCase();
    }

}
