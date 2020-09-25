package com.engine.web.vo.rule;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/22
 * @since 1.0.0
 */
@Data
public class BatchExecuteRuleRequest {

    /**
     * 指定一个线程处理多少规则
     */
    @Min(100)
    @Max(2000)
    private Integer threadSegNumber = 100;

    /**
     * 执行超时时间，-1永不超时
     */
    @NotNull
    private Long timeout = -1L;

    /**
     * 规则执行信息，规则code以及规则入参
     */
    @Size(max = 2000)
    @NotNull
    private List<ExecuteInfo> executeInfos;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ExecuteInfo extends ExecuteRuleRequest {

        /**
         * 标记规则使用，防止传入规则与规则输出结果顺序错误时
         * 通过此标记区分
         */
        @Nullable
        private String symbol;
    }

}
