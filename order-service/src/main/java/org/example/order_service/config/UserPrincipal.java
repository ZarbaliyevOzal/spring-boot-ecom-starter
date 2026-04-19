package org.example.order_service.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

@Getter
public class UserPrincipal {

    private final Long userId;
    private final String username;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        this.userId = jwt.getClaim("userId"); // or jwt.getSubject()
        this.username = jwt.getClaim("preferred_username");
        this.authorities = authorities;
        this.email = jwt.getClaimAsString("email");
    }
}
