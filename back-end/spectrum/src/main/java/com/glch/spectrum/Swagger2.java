package com.glch.spectrum;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class Swagger2 {
    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.enable(true);
        docket.apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.glch.spectrum.controller")).paths(PathSelectors.any()).build();
        return docket;
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("频谱管理软件")
                .contact(new Contact("WZ", "", ""))
                .description("频谱管理软件 web项目 api接口规范")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}
