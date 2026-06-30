package com.erp.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger / Knife4j API 文档配置
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@Configuration
@EnableKnife4j
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.erp.modules"))
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ERP 管理系统 API 文档")
                .description("企业资源计划管理系统 — 涵盖财务、采购、销售、库存、生产、HR 等模块")
                .version("1.0.0")
                .contact(new Contact("ERP Team", "", ""))
                .build();
    }
}
