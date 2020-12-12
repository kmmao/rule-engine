package cn.ruleengine.web.enums;

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
     * RULE 99990100 error code enum.
     */
    RULE99990100(9999100, "参数异常"),
    /**
     * RULE 99990401 error code enum.
     */
    RULE4009(4009, "用户未登录"),
    /**
     * RULE 99990401 error code enum.
     */
    RULE99990401(99990401, "无访问权限"),
    /**
     * RULE 99990402 error code enum.
     */
    RULE99990402(99990402, "验证信息已失效"),
    /**
     * RULE 000500 error code enum.
     */
    RULE500(500, "未知异常"),
    /**
     * RULE 000501 error code enum.
     */
    RULE99990501(501, "客户中止异常"),
    /**
     * RULE 000403 error code enum.
     */
    RULE99990403(9999403, "无权访问"),
    /**
     * RULE 000404 error code enum.
     */
    RULE9999404(9999404, "找不到指定资源"),
    /**
     * RULE 000405 error code enum.
     */
    RULE9999405(9999405, "请求方法不匹配"),
    /**
     * RULE 99990001 error code enum.
     */
    RULE99990001(99990001, "不支持的内容类型"),
    /**
     * RULE 99990002 error code enum.
     */
    RULE99990002(99990002, "方法参数无效"),
    /**
     * RULE 10010002 error code enum.
     */
    RULE10010002(10010002, "TOKEN解析失败"),
    /**
     * RULE 10010002 error code enum.
     */
    RULE10010004(10010004, "TOKEN为空"),
    /**
     * RULE 10010003 error code enum.
     */
    RULE10010003(10010003, "缺少所需的请求正文"),
    /**
     * RULE 10011032 error code enum.
     */
    RULE10011032(10011032, "不存在此邮箱"),
    /**
     * RULE 10011033 error code enum.
     */
    RULE10011033(10011033, "邮箱格式错误"),
    /**
     * RULE 10011034 error code enum.
     */
    RULE10011034(10011034, "邮箱发送出错"),
    /**
     * RULE 10011035 error code enum.
     */
    RULE10011035(10011035, "异常警告"),
    /**
     * RULE 10011036 error code enum.
     */
    RULE10011036(10011036, "OSS上传文件异常"),
    /**
     * RULE 10011038 error code enum.
     */
    RULE10011038(10011038, "请勿重复操作"),
    /**
     * RULE 10011039 error code enum.
     */
    RULE10011039(10011039, "验证Token失败"),

    RULE8900(8900, "规则引擎异常"),
    RULE8910(8910, "规则函数异常"),
    RULE8920(8920, "条件配置异常"),
    RULE8930(8930, "无数据权限异常");

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
