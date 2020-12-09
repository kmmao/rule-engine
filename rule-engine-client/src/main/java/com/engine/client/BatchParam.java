package com.engine.client;

import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/9
 * @since 1.0.0
 */
@Data
public class BatchParam {

    /**
     * 指定一个线程处理多少规则
     */
    private Integer threadSegNumber = 100;

    /**
     * 执行超时时间，-1永不超时
     */
    private Long timeout = -1L;

    /**
     * 规则执行信息，规则code以及规则入参
     */
    private List<Object> models;

}
