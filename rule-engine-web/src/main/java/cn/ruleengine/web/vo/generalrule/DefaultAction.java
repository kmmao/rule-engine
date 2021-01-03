package cn.ruleengine.web.vo.generalrule;

import cn.hutool.core.lang.Validator;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.web.enums.EnableEnum;
import cn.ruleengine.web.vo.condition.ConfigValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/9/3
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultAction extends ConfigValue {

    public DefaultAction() {

    }

    public DefaultAction(ConfigValue configValue) {
        this.setValue(configValue.getValue());
        this.setType(configValue.getType());
        this.setValueName(configValue.getValueName());
        this.setVariableValue(configValue.getVariableValue());
        this.setValueType(configValue.getValueType());
    }

    /**
     * 0启用 1不启用
     */
    @NotNull
    private Integer enableDefaultAction;

    public void valid() {
        if (EnableEnum.ENABLE.getStatus().equals(this.getEnableDefaultAction())) {
            if (Validator.isEmpty(this.getType())) {
                throw new ValidException("决策表默认结果类型不能为空");
            }
            if (Validator.isEmpty(this.getValueType())) {
                throw new ValidException("决策表默认结果值类型不能为空");
            }
            if (Validator.isEmpty(this.getValue())) {
                throw new ValidException("决策表默认结果值不能为空");
            }
        }
    }

}
