package com.nexus.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    RouteLocator gateway(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder
                .routes()
                .route(routeSpec -> routeSpec
                        .path("/aspyn")
                        .filters(gatewayFilterSpec ->
                                gatewayFilterSpec.setPath("/guides"))
                        .uri("https://spring.io")
                )
                .route("twitter", routeSpec -> routeSpec
                        .path("/twitter/**")
                        .filters(fs -> fs.rewritePath("/twitter/@(?<handle>.*)", "/${handle}"
                        ))
                        .uri("http://twitter.com/@")
                )
                .route("sample", routeSpec -> routeSpec
                        .path("/sample/**")
                        .filters(gatewayFilterSpec ->
                                gatewayFilterSpec.setPath("/actuator"))
                        .uri("http://localhost:8081"))
                .build();
    }

}
