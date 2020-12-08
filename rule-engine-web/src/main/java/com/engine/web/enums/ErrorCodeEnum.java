package com.engine.web.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈通用错误码〉
 *
 * @author 丁乾文
 * @create 2019/8/13
 * @since 1.0.0
 */
public enum ErrorCodeEnum {

    /**
     * BOOT 99990100 error code enum.
     */
    BOOT99990100(9999100, "参数异常"),
    /**
     * BOOT 99990401 error code enum.
     */
    BOOT4009(4009, "用户未登录"),
    /**
     * BOOT 99990401 error code enum.
     */
    BOOT99990401(99990401, "无访问权限"),
    /**
     * BOOT 99990402 error code enum.
     */
    BOOT99990402(99990402, "验证信息已失效"),
    /**
     * BOOT 000500 error code enum.
     */
    BOOT500(500, "未知异常"),
    /**
     * BOOT 000501 error code enum.
     */
    BOOT99990501(501, "客户中止异常"),
    /**
     * BOOT 000403 error code enum.
     */
    BOOT99990403(9999403, "无权访问"),
    /**
     * BOOT 000404 error code enum.
     */
    BOOT9999404(9999404, "找不到指定资源"),
    /**
     * BOOT 000405 error code enum.
     */
    BOOT9999405(9999405, "请求方法不匹配"),
    /**
     * BOOT 99990001 error code enum.
     */
    BOOT99990001(99990001, "不支持的内容类型"),
    /**
     * BOOT 99990002 error code enum.
     */
    BOOT99990002(99990002, "方法参数无效"),
    /**
     * BOOT 10010002 error code enum.
     */
    BOOT10010002(10010002, "TOKEN解析失败"),
    /**
     * BOOT 10010002 error code enum.
     */
    BOOT10010004(10010004, "TOKEN为空"),
    /**
     * BOOT 10010003 error code enum.
     */
    BOOT10010003(10010003, "缺少所需的请求正文"),
    /**
     * BOOT 10011032 error code enum.
     */
    BOOT10011032(10011032, "不存在此邮箱"),
    /**
     * BOOT 10011033 error code enum.
     */
    BOOT10011033(10011033, "邮箱格式错误"),
    /**
     * BOOT 10011034 error code enum.
     */
    BOOT10011034(10011034, "邮箱发送出错"),
    /**
     * BOOT 10011035 error code enum.
     */
    BOOT10011035(10011035, "异常警告"),
    /**
     * BOOT 10011036 error code enum.
     */
    BOOT10011036(10011036, "OSS上传文件异常"),
    /**
     * BOOT 10011038 error code enum.
     */
    BOOT10011038(10011038, "请勿重复操作"),
    /**
     * BOOT 10011039 error code enum.
     */
    BOOT10011039(10011039, "验证Token失败"),

    RULE8900(8900, "规则引擎异常"),
    RULE8910(8910, "规则函数异常");

    @Getter
    private int code;
    @Getter
    private String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    static Map<Integer, ErrorCodeEnum> map = new HashMap<>();

    static {
        ErrorCodeEnum[] values = values();
        for (ErrorCodeEnum value : values) {
            map.put(value.getCode(), value);
        }
    }

    public static String getMagByCode(Integer code) {
        return map.get(code).getMsg();
    }

    public static ErrorCodeEnum get(Integer code) {
        return map.get(code);
    }

}
