package cn.ruleengine.client;

import cn.ruleengine.client.fegin.BaseInterface;
import cn.ruleengine.client.result.Output;
import org.springframework.lang.NonNull;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020-12-29 15:51
 * @since 1.0.0
 */
public class DecisionTable extends Executor {


    public DecisionTable(RuleEngineProperties ruleEngineProperties, BaseInterface decisionTableInterface) {
        super(ruleEngineProperties, decisionTableInterface);
    }

    @Override
    public Output execute(@NonNull Object model) {
        return super.execute(model);
    }

}
