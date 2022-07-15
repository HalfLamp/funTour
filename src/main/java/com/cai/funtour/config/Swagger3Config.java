package com.cai.funtour.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/21 13:52
 * @description：swagger配置类
 */

@Configuration
public class Swagger3Config {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("public-consumer")
                .globalRequestParameters(globalRequestParameters())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cai.funtour.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("趣旅游系统公共接口API文档")
                .contact(new Contact("残灯半盏", "", "caizhiyuan826@163.com"))
                .version("1.0")
                .description("消费者公共接口API（非对外提供的http接口）")
                .build();
    }

    private List<RequestParameter> globalRequestParameters() {
        List<RequestParameter> requestParameters = new ArrayList<>();
        RequestParameterBuilder builder = new RequestParameterBuilder();
        requestParameters.add(
                builder.name("Authorization")
                        .required(false)
                        .in(ParameterType.HEADER)
                        .build()
        );
        return requestParameters;
    }

}
