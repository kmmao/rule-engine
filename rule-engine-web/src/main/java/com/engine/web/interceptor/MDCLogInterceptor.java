package com.engine.web.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/12/6
 * @since 1.0.0
 */
@Slf4j
@Component
public class MDCLogInterceptor extends HandlerInterceptorAdapter {

    public final static String REQUEST_ID = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uuid = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID, REQUEST_ID.concat(StringPool.COLON).concat(uuid));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        MDC.clear();
    }
}
