/**
 * Copyright @2020 dingqianwen
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
package com.engine.web.console;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.hutool.core.thread.ThreadUtil;
import com.engine.web.annotation.RoleAuth;
import com.engine.web.console.ConsoleLog;
import com.engine.web.console.ConsoleLogQueue;
import com.engine.web.exception.NoLoginException;
import com.engine.web.interceptor.AuthInterceptor;
import com.engine.web.store.entity.RuleEngineUser;
import com.engine.web.util.CookieUtils;
import com.engine.web.util.HttpServletUtils;
import com.engine.web.util.JWTUtils;
import com.engine.web.util.ResponseUtils;
import com.engine.web.vo.base.response.BaseResult;
import io.jsonwebtoken.Claims;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import lombok.extern.slf4j.Slf4j;

import static com.engine.web.enums.ErrorCodeEnum.*;
import static com.engine.web.enums.ErrorCodeEnum.BOOT10011039;

/**
 * 配置WebSocket消息代理端点，即stomp服务端
 *
 * @author dingqianwen
 */
@Slf4j
@EnableWebSocketMessageBroker
@Configuration
public class WebSocketAutoConfiguration implements WebSocketMessageBrokerConfigurer {

    @Resource
    private SimpMessagingTemplate messagingTemplate;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private AuthInterceptor authInterceptor;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                        String token = CookieUtils.get(AuthInterceptor.TOKEN);
                        if (token == null) {
                            log.warn("Token为空");
                            ResponseUtils.responseJson(BaseResult.err(BOOT10010004.getCode(), BOOT10010004.getMsg()));
                            return false;
                        }
                        Claims claims;
                        try {
                            claims = JWTUtils.verifyToken(token);
                        } catch (Exception e) {
                            log.warn("Token验证不通过!Token:{}", token);
                            ResponseUtils.responseJson(BaseResult.err(BOOT10011039.getCode(), BOOT10011039.getMsg()));
                            return false;
                        }
                        String[] codes = {RoleAuth.DEFAULT_CODE};
                        if (!authInterceptor.auth(codes, false, Integer.valueOf(claims.getId()))) {
                            log.warn("无权限连接WebSocket,UserId:{}", claims.getId());
                            ResponseUtils.responseJson(BaseResult.err(BOOT99990401.getCode(), BOOT99990401.getMsg()));
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

                    }
                }).setAllowedOrigins("*")
                .withSockJS();
    }

    /**
     * 推送日志
     */
    @PostConstruct
    public void pushLogger() {
        Runnable runnable = () -> {
            for (; ; ) {
                try {
                    ConsoleLog log = ConsoleLogQueue.getInstance().poll();
                    if (log != null) {
                        this.messagingTemplate.convertAndSend("/consoleLog", log);
                    }
                } catch (Exception e) {
                    log.warn("推送日志失败:{}", e.getMessage());
                }
            }
        };
        this.threadPoolTaskExecutor.submit(runnable);
    }
}
