package com.engine.web.config;

import com.engine.web.BaseTest;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/21
 * @since 1.0.0
 */
public class EncryptorTest extends BaseTest {

    @Resource
    private StringEncryptor encryptor;

    @Test
    public void encrypt() {
        log.info("密钥：{}", encryptor.encrypt("123456"));
    }

}
