package cn.ruleengine.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/12/21
 * @since 1.0.0
 */
@Configuration
public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //对actuator监控所用的访问全部需要认证
        http.formLogin().and().authorizeRequests().antMatchers( "/actuator/**")
                .authenticated()
                .anyRequest()
                .permitAll();
    }

}
