package com.engine.web.util;

import com.alibaba.fastjson.JSON;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/8/25
 * @since 1.0.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {
    /**
     * 页面响应json
     *
     * @param object 数据
     */
    public static void responseJson(Object object) {
        HttpServletResponse response = HttpServletUtils.getResponse();
        response.setCharacterEncoding(Charset.defaultCharset().name());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSONString(object));
            writer.flush();
        } catch (Exception e) {
            log.error("{1}", e);
        }
    }
}
