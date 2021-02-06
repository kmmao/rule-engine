package cn.ruleengine.core.value;

import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import lombok.Getter;
import lombok.ToString;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * <p>
 * 注意：目前可能会引起被攻击风险，还未经过安全测试，暂时无法使用以及配置Formula
 * 敬请期待
 * <p>
 * T(java.lang.Runtime).getRuntime().exec("...")
 *
 * @author 丁乾文
 * @create 2021/2/6
 * @since 1.0.0
 */
@ToString
public class Formula implements Value {

    /**
     * spel表达式解析器
     * <p>
     * 存在安全问题，待改进测试通过后可以使用，也仅限公司内部使用
     */
    private final static ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    /**
     * 表达式
     */
    @Getter
    private String value;

    private Expression expression;
    /**
     * 表达式返回类型
     */
    private ValueType valueType;

    Formula() {

    }

    public Formula(@Nullable String value, @NonNull ValueType valueType) {
        Objects.requireNonNull(valueType);
        this.value = value;
        this.valueType = valueType;
        this.expression = EXPRESSION_PARSER.parseExpression(value);
    }

    /**
     * 表达式
     * <p>
     * 注意：目前表达式只能引用元素
     * <p>
     * 使用方式：
     * <blockquote>
     * <pre>
     *      expression = "#element1 - #element2 * 3"
     *      input = {"element1":3,"element2":1}
     *      return 6;
     * </pre>
     * </blockquote>
     *
     * @param input         上下文
     * @param configuration 规则配置信息
     * @return value
     */
    @Override
    public Object getValue(Input input, RuleEngineConfiguration configuration) {
        EvaluationContext context = new StandardEvaluationContext();
        Map<String, Object> params = input.getAll();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            context.setVariable(param.getKey(), param.getValue());
        }
        Class<?> classType = this.valueType.getClassType();
        return this.expression.getValue(context, classType);
    }

    @Override
    public ValueType getValueType() {
        return this.valueType;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Formula)) {
            return false;
        }
        Formula formula = (Formula) other;
        if (this.getValueType() != formula.getValueType()) {
            return false;
        }
        return Objects.equals(this.getValue(), formula.getValue());
    }

}
