package cn.ruleengine.core.value;

import cn.ruleengine.core.DefaultInput;
import cn.ruleengine.core.Input;
import org.junit.Test;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2021/2/6
 * @since 1.0.0
 */
public class FormulaTest {

    @Test
    public void test() {
        Formula formula = new Formula("(#element1 - #element2) * 3", ValueType.NUMBER);
        Input input = new DefaultInput();
        input.put("element1", 3);
        input.put("element2", 1);
        Object value = formula.getValue(input, null);
        System.out.println(value);
    }

}
