package com.engine.client;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/11/7
 * @since 1.0.0
 */
@lombok.Data
public class Result {
    /**
     * 200 执行成功
     */
    private Integer code;
    private Data data;
    private String message;
    private String timestamp;

    @lombok.Data
    public static class Data {
        private Object value;
        private String dataType;
    }

}
