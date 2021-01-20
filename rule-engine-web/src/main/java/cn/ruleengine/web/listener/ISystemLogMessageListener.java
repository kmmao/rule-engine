package cn.ruleengine.web.listener;

import cn.ruleengine.web.store.entity.RuleEngineSystemLog;

/**
 * @ClassName ISystemLogMessageListener
 * @Description 引擎系统日志监听接口
 * @Author AaronPiUC
 * @Date 2021/1/20 9:17
 */
public interface ISystemLogMessageListener {

    /**
     *
     * @param ruleEngineSystemLog 引擎系统日志
     */
    void execute(RuleEngineSystemLog ruleEngineSystemLog);
}
