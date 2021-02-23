/**
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ruleengine.core.value;

import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 表达式
 * <p>
 * 注意：目前表达式只能引用元素
 * <p>
 * 使用方式：
 * <blockquote>
 * <pre>
 *      expression = "(#element1 - #element2) * 3"
 *      input = {"element1":3,"element2":1}
 *      return 6;
 * </pre>
 * </blockquote>
 *
 * @author 丁乾文
 * @create 2021/2/6
 * @since 1.0.0
 */
@Slf4j
@ToString
public class Formula implements Value {

    /**
     * spel表达式解析器
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

    /**
     * EvaluationContextType 默认使用 SIMPLE_EVALUATION_READ_ONLY
     */
    private EvaluationContextType evaluationContextType = EvaluationContextType.SIMPLE_EVALUATION_READ_ONLY;

    /**
     * json反序列化使用
     */
    Formula() {

    }

    public Formula(@NonNull String value, @NonNull ValueType valueType) {
        Objects.requireNonNull(valueType);
        this.value = value;
        this.valueType = valueType;
        this.expression = EXPRESSION_PARSER.parseExpression(value);
    }

    public enum EvaluationContextType {
        /**
         * 谨慎使用 STANDARD_EVALUATION
         */
        SIMPLE_EVALUATION_READ_ONLY,
        SIMPLE_EVALUATION_READ_WRITE,
        STANDARD_EVALUATION
    }

    public void setEvaluationContextType(EvaluationContextType evaluationContextType) {
        Objects.requireNonNull(evaluationContextType);
        if (log.isWarnEnabled()) {
            if (evaluationContextType == EvaluationContextType.STANDARD_EVALUATION) {
                log.warn("请谨慎使用：EvaluationContextType = STANDARD_EVALUATION，有可能引起被攻击风险！");
            }
        }
        this.evaluationContextType = evaluationContextType;
    }

    /**
     * 解析表达式值
     *
     * @param input         上下文
     * @param configuration 规则配置信息
     * @return value
     */
    @Override
    public Object getValue(Input input, RuleEngineConfiguration configuration) {
        log.debug("开始处理表达式：" + this.getValue());
        EvaluationContext context = this.getEvaluationContext();
        Map<String, Object> params = input.getAll();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            context.setVariable(param.getKey(), param.getValue());
        }
        Class<?> classType = this.valueType.getClassType();
        Object value = this.expression.getValue(context, classType);
        return this.dataConversion(value, this.valueType);
    }

    /**
     * get EvaluationContext
     *
     * @return EvaluationContext
     */
    private EvaluationContext getEvaluationContext() {
        switch (this.evaluationContextType) {
            case SIMPLE_EVALUATION_READ_ONLY:
                return SimpleEvaluationContext.forReadOnlyDataBinding().build();
            case SIMPLE_EVALUATION_READ_WRITE:
                return SimpleEvaluationContext.forReadWriteDataBinding().build();
            case STANDARD_EVALUATION:
                return new StandardEvaluationContext();
            default:
                throw new IllegalStateException("Unexpected value: " + evaluationContextType);
        }
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
