package org.example.plusproject.domain.user.dto.security;

import lombok.Getter;
import org.example.plusproject.domain.user.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
public class AuthUser {

    private final Long userId;
    private final String email;
    private final Role role;

    public AuthUser(Long userId, String email, Role role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security expects roles to have the "ROLE_" prefix.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
