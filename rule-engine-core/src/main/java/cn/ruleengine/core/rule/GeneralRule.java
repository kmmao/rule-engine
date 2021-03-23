package cn.ruleengine.core.rule;

import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.condition.Precondition;
import cn.ruleengine.core.value.Value;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 0.简单的规则可以单独使用，完成一些比较简单的业务。
 * 1.结果值类型一旦确认发布后，不可以再改变结果返回值类型（防止修改返回值被引用规则/条件类型不匹配的问题）。
 * 2.规则集可以引用简单的规则，条件也可以引用简单的规则，同时需要避免循环引用的问题。
 *
 * @author 丁乾文
 * @create 2020-12-28 16:14
 * @since 1.0.0
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class GeneralRule extends Rule {

    /**
     * 工作空间
     */
    private Integer workspaceId;
    /**
     * 工作空间code
     */
    private String workspaceCode;

    /**
     * 前提条件
     * <p>
     * 如果前提条件不满足，则直接返回默认结果
     */
    private Precondition precondition = new Precondition();

    /**
     * 规则默认值
     */
    private Value defaultActionValue;

    /**
     * 规则运行发生异常，邮件接收人
     */
    @JsonIgnore
    private AbnormalAlarm abnormalAlarm = new AbnormalAlarm();

    /**
     * 执行规则
     *
     * @param input         入参
     * @param configuration 规则引擎配置
     * @return 规则返回值
     */
    @Override
    @Nullable
    public Object execute(@NonNull Input input, @NonNull RuleEngineConfiguration configuration) {
        long startTime = System.currentTimeMillis();
        try {
            log.debug("开始计算前提条件");
            if (this.precondition.compare(input, configuration)) {
                log.debug("前提条件成立");
                Object action = super.execute(input, configuration);
                if (action != null) {
                    // 条件全部命中时候执行
                    return action;
                }
            }
            Value defaultValue = this.getDefaultActionValue();
            if (Objects.nonNull(defaultValue)) {
                log.debug("结果未命中，存在默认结果，返回默认结果");
                return defaultValue.getValue(input, configuration);
            }
            log.debug("结果未命中，不存在默认结果，返回:null");
            return null;
        } finally {
            long cost = System.currentTimeMillis() - startTime;
            if (log.isDebugEnabled()) {
                log.debug("普通规则计算耗时:{}ms", cost);
            }
            if (cost >= this.getAbnormalAlarm().getTimeOutThreshold()) {
                log.warn("警告：规则执行超过最大阈值，请检查规则配置，规则Code:" + this.getCode());
            }
        }
    }

    /**
     * 根据rule json字符串构建一个规则
     *
     * @param jsonString rule json字符串
     * @return rule
     */
    @SneakyThrows
    public static GeneralRule buildRule(@NonNull String jsonString) {
        return OBJECT_MAPPER.readValue(jsonString, GeneralRule.class);
    }

    @SneakyThrows
    @Override
    public void fromJson(@NonNull String jsonString) {
        GeneralRule generalRule = buildRule(jsonString);
        this.setId(generalRule.getId());
        this.setCode(generalRule.getCode());
        this.setName(generalRule.getName());
        this.setPrecondition(generalRule.getPrecondition());
        this.setDefaultActionValue(generalRule.getDefaultActionValue());
        this.setDescription(generalRule.getDescription());
        this.setWorkspaceId(generalRule.getWorkspaceId());
        this.setWorkspaceCode(generalRule.getWorkspaceCode());
        this.setConditionSet(generalRule.getConditionSet());
        this.setActionValue(generalRule.getActionValue());
    }

    public void setPrecondition(Precondition precondition) {
        this.precondition = Objects.requireNonNull(precondition);
    }

}
