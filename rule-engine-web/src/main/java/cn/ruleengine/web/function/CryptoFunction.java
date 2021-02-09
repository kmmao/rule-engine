package cn.ruleengine.web.function;

import cn.hutool.core.util.StrUtil;
import cn.ruleengine.core.annotation.Executor;
import cn.ruleengine.core.annotation.Function;
import cn.ruleengine.core.annotation.Param;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 * <p>
 * 对field的值进行加密。
 * <p>
 * 第二个参数String为算法字符串。可选：MD2、MD5、SHA1、SHA-256、SHA-384、SHA-512
 *
 * @author dingqianwen
 * @date 2021/2/9
 * @since 1.0.0
 */
@Slf4j
@Function
public class CryptoFunction {

    /**
     * 对field的值进行加密
     *
     * @param string     加密字符串
     * @param cryptoType 加密类型，默认md5
     * @return 密文
     */
    @Executor
    public String executor(@Param(value = "string", required = false) String string,
                           @Param(value = "cryptoType", required = false) String cryptoType) {
        if (StrUtil.isBlank(string)) {
            return string;
        }
        switch (cryptoType) {
            case "SHA-512":
                // TODO: 2021/2/9 待完成
            case "SHA1":
                // TODO: 2021/2/9 待完成
            case "SHA-256":
                // TODO: 2021/2/9 待完成
            case "MD5":
                // MD5 ..
            default:
                // 默认MD5
                return DigestUtils.md5DigestAsHex(string.getBytes());
        }
    }

}
