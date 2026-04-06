package org.example.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final KeycloakJwtConverter keycloakJwtConverter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers(
                                "/",
                                "/api/v1/auth/signup",
                                "/api/v1/auth/login"
                        )
                        .permitAll()
                        .pathMatchers("/actuator/**")
                            .hasRole("SUPER_AMIN")
                        .pathMatchers("/api/v1/users/**")
                            .hasAnyRole("SUPER_ADMIN", "USER_MANAGE", "USER_VIEW")
                        .pathMatchers("/api/v1/products/**")
                            .hasAnyRole("SUPER_ADMIN", "PRODUCT_MANAGE", "PRODUCT_VIEW")
                        .anyExchange().authenticated()
                )
//                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtConverter))
                )
                .build();
    }

//    @Bean
//    public ReactiveJwtDecoder reactiveJwtDecoder() {
//        return NimbusReactiveJwtDecoder
//                .withJwkSetUri("http://localhost:9090/realms/myrealm/protocol/openid-connect/certs")
//                .build();
//    }
}
