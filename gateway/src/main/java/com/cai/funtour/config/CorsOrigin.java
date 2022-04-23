package com.cai.funtour.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/22 14:16
 * @description：配置跨域请求
 */
@Configuration
public class CorsOrigin {
    @Bean
    public CorsWebFilter corsWebFilter(){
        CorsConfiguration config = new CorsConfiguration();
        // 放行所有原始域
        config.addAllowedOrigin("*");
        // 发送cookie
        config.setAllowCredentials(true);
        // 放行所有请求方式
        config.addAllowedMethod("*");
        // 放行所有请求头
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
