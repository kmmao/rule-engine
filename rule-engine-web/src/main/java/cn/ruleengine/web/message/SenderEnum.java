package cn.ruleengine.web.message;

import lombok.Getter;

/**
 * @ClassName SenderEnum
 * @Description 日志发送方式
 * @Author AaronPiUC
 * @Date 2020/12/10 11:38
 */
public enum SenderEnum {

    /**
     * RULE 99990100 error code enum.
     */
    RABBITMQ("rabbitmq");

    @Getter
    private String senderType;

    SenderEnum(String senderType) {
        this.senderType = senderType;
    }

}
