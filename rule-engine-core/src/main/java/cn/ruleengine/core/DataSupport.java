package cn.ruleengine.core;

import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2021/2/5
 * @since 1.0.0
 */
@Data
public abstract class DataSupport {

    private Integer id;

    private String code;

    private String name;

    private String description;

    /**
     * 工作空间
     */
    private Integer workspaceId;
    /**
     * 工作空间code
     */
    private String workspaceCode;

    /**
     * 执行决策表/规则
     *
     * @param input         输入参数
     * @param configuration 配置信息
     * @return 执行结果
     */
    @Nullable
    public abstract Object execute(@NonNull Input input, @NonNull RuleEngineConfiguration configuration);


//    @Data
//    public static class AbnormalAlarm {
//        /**
//         * 是否启用
//         */
//        private Boolean enable = false;
//        /**
//         * 邮件接收人
//         */
//        private String[] email;
//
//        /**
//         * 规则执行超时阈值，默认3秒
//         */
//        private long timeOutThreshold = 3000;
//    }

}
