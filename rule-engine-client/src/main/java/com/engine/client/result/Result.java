package com.engine.client.result;

import lombok.Data;

import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/9
 * @since 1.0.0
 */
@Data
public class Result {

    private static final int SUCCESS_CODE = 200;

    /**
     * 接口返回码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String message;
    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 是否请求成功
     *
     * @return true
     */
    public boolean isSuccess() {
        return Objects.equals(this.code, SUCCESS_CODE);
    }
}
