package org.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(
						"path_route",
						r -> r
								.path(
										"/api/v1/users/**",
										"/api/v1/auth/**")
								.uri("lb://users-service")
				)
				.route(
						"path_route",
						r -> r.path("/api/v1/products/**")
								.uri("lb://product-service")
				)
				.route(
						"path_route",
						r -> r.path("/api/v1/orders/**")
								.uri("lb://order-service")
				)
				.build();
	}

}
