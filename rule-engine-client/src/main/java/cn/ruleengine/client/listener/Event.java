package cn.ruleengine.client.listener;

import cn.ruleengine.client.result.OutPut;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/2/1
 * @since 1.0.0
 */
public interface Event {

    /**
     * 当监听的规则code 有输出时触发
     *
     * @param code   规则/决策表/规则集 code
     * @param outPut 监听结果
     */
    void onOut(String code, OutPut outPut);

}
