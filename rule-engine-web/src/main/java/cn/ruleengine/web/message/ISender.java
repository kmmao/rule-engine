package cn.ruleengine.web.message;

import cn.ruleengine.web.store.entity.RuleEngineSystemLog;

public interface ISender extends ISenderSpi<String> {

    default void execute(String method, RuleEngineSystemLog log) {
        send(log);
    }

    void send(RuleEngineSystemLog log);
}
