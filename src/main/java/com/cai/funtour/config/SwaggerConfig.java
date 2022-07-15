package com.cai.funtour.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/7/13 16:18
 * @description：swagger微服务路由
 */
@Component
@Primary
public class SwaggerConfig implements SwaggerResourcesProvider {

    private final RouteLocator routeLocator;
    // gateway配置文件
    private final GatewayProperties gatewayProperties;


    @Autowired
    public SwaggerConfig(RouteLocator routeLocator, GatewayProperties gatewayProperties) {
        this.routeLocator = routeLocator;
        this.gatewayProperties = gatewayProperties;
    }


    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        //从配置文件中获取并配置SwaggerResource
        gatewayProperties.getRoutes().stream()
                //过滤路由
                .filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                //循环添加，从路由的断言中获取，一般来说路由都会配置断言Path信息，这就不多说了
                .forEach(route -> {
                    route.getPredicates().stream()
                            //获取Path信息
                            .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                            //开始添加SwaggerResource
                            .forEach(predicateDefinition -> resources.add(swaggerResource(route.getId(),
                                    predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                            .replace("**", "v2/api-docs?group=" + route.getId()))));
                });

        return resources;
    }


    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(DocumentationType.SWAGGER_2.getVersion());
        return swaggerResource;
    }

    private Object swaggerResource(String name, String location, String version) {

        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setUrl(location + "/swagger-ui/index.html");
        swaggerResource.setLocation(location + "/swagger-ui/index.html");
        swaggerResource.setSwaggerVersion(DocumentationType.SWAGGER_2.getVersion());

        return swaggerResource;
    }
}
