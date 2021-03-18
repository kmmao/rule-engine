package cn.ruleengine.web.timer;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * 移除引擎中长时间未使用规则/决策表/规则集
 *
 * @author dingqianwen
 * @date 2021/1/18
 * @since 1.0.0
 */
@Slf4j
@Component
public class RemoveIdleEngineDataTimer {


    /**
     * 一小时检测一次长时间未使用规则/决策表/规则集 然后从引擎中移除
     * <p>
     * 并且规则加载方式增加懒加载功能
     * <p>
     * 待修改流程：当规则被执行时，先从引擎中寻找对应的规则，找不到再去数据库load下，如果load不到则抛出异常
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void execute() {
        log.info("开始清除长时间未使用规则/决策表/规则集");

        // TODO: 2021/3/18
        ThreadUtil.sleep(2000);

        log.info("清除完毕");
    }


}
