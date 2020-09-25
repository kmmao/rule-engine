package com.engine.web.config;


import com.engine.web.interceptor.AuthInterceptor;
import com.engine.web.interceptor.MDCLogInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;


/**
 * 〈一句话功能简述〉<br>
 * 〈mvc Interceptor〉
 *
 * @author 丁乾文
 * @create 2019/8/13
 * @since 1.0.0
 */
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private MDCLogInterceptor mdcLogInterceptor;
    @Resource
    private AuthInterceptor authInterceptor;

    /**
     * 静态资源不拦截
     */
    private static final List<String> STATIC_RESOURCE = Arrays.asList(
            "/swagger-ui.html/**", "/swagger-resources/**", "/webjars/**", "/v2/**", "/druid/**", "/csrf/**", "/error/**", "/doc.html/**");

    /**
     * @param registry 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mdcLogInterceptor).addPathPatterns("/**")
                .excludePathPatterns(STATIC_RESOURCE);
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
    }

}
