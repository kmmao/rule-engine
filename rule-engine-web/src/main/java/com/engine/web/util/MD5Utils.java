package com.engine.web.util;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.DigestUtils;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/8/14
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MD5Utils {

    /**
     * 盐，用于混交md5
     */
    private static final String SLAT = "&%5123***&&%%$$#@";

    /**
     * 生成md5
     *
     * @param str 需要生成的字符串
     * @return 加密后的字符串
     */
    public static String encrypt(String str) {
        String base = str + StringPool.SLASH + SLAT;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

}
