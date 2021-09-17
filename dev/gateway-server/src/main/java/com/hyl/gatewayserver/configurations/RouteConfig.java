package com.hyl.gatewayserver.configurations;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Définition des routes
 */
@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/item/**")
                        .uri("lb://item-api")
                        .id("item-api"))
                .route(r -> r.path("/user/**")
                        .uri("lb://user-api")
                        .id("user-api"))
                .route(r -> r.path("/loan/**")
                        .uri("lb://loan-api")
                        .id("loan-api"))
                .route(r -> r.path("/mail/**")
                        .uri("lb://mail-server")
                        .id("mail-server"))
                .route(r -> r.path("/memo/**")
                        .uri("lb://memo-api")
                        .id("memo-api"))
                .build();
    }
}
