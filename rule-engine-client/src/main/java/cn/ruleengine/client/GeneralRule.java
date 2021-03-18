package cn.ruleengine.client;

import cn.ruleengine.client.fegin.GeneralRuleInterface;
import cn.ruleengine.client.result.Output;
import org.springframework.lang.NonNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020-12-29 16:15
 * @since 1.0.0
 */
public class GeneralRule extends Executor {

    public GeneralRule(RuleEngineProperties ruleEngineProperties,
                       GeneralRuleInterface generalRuleInterface) {
        super(ruleEngineProperties, generalRuleInterface);
    }

    @Override
    public Output execute(@NonNull Object model) {
        return super.execute(model);
    }

    @Override
    public boolean isExists(String code) {
        return super.isExists(code);
    }

}
