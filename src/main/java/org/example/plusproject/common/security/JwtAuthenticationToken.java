package org.example.plusproject.common.security;

import org.example.plusproject.domain.user.dto.security.AuthUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthUser authUser;

    public JwtAuthenticationToken(AuthUser authUser) {
        super(authUser.getAuthorities());
        this.authUser = authUser;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // Credentials are not needed as JWT is self-contained
    }

    @Override
    public Object getPrincipal() {
        return authUser;
    }
}
