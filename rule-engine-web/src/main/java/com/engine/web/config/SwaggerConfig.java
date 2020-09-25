package com.engine.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 〈一句话功能简述〉<br>
 * 〈SwaggerConfig〉
 *
 * @author 丁乾文
 * @create 2019/8/13
 * @since 1.0.0
 */
@Component
public class SwaggerConfig {

    /**
     * swaggerConfig
     *
     * @return Docket
     */
    @Bean
    public Docket buildSwaggerConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.engine.web.controller")).build();
    }

    /**
     * 构建 api文档的详细信息函数
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("rule-engine")
                .contact(new Contact("简易的规则引擎", "http://www.ruleengine.cn", "761945125@qq.com"))
                .version("1.2.0")
                .description("简易的规则引擎接口文档")
                .build();
    }
}
