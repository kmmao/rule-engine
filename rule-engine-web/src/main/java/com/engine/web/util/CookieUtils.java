/**
 * Copyright (c) 2020 dingqianwen (761945125@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.engine.web.util;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/8/14
 * @since 1.0.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtils {

    /**
     * 根据cookieName获取cookie信息
     *
     * @param cookieName cookieName
     * @return 如果有值就返回, 否则返回null
     */
    public static String get(String cookieName) {
        Cookie[] cookies = HttpServletUtils.getRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 设置cookie信息
     *
     * @param cookieName cookieName
     * @param value      value
     */
    public static void set(String cookieName, String value) {
        try {
            Cookie cookie = new Cookie(cookieName, URLEncoder.encode(value, Charset.defaultCharset().name()));
            cookie.setPath(StringPool.SLASH);
            cookie.setMaxAge(1800 + (60 * 60 * 8));
            HttpServletUtils.getResponse().addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            log.error("{1}", e);
        }
    }

    /**
     * 删除cookie
     *
     * @param cookieName cookieName
     */
    public static void delete(String cookieName) {
        Cookie cookie = new Cookie(cookieName, StringPool.EMPTY);
        cookie.setPath(StringPool.SLASH);
        cookie.setMaxAge(0);
        HttpServletUtils.getResponse().addCookie(cookie);
    }
}
