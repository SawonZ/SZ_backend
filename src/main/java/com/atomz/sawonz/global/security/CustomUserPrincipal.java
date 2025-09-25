package com.atomz.sawonz.global.security;

import java.util.List;
import java.util.Objects;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Builder
@AllArgsConstructor
public class CustomUserPrincipal implements UserDetails {
    private final Long id;
    private final String userName;
    private final String email;
    private final String role;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override public String getUsername() { return userName; } // ← 규약 메서드
    @Override public String getPassword() { return ""; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // authorities가 비어있으면 role로 대체 생성 (하위 호환/보호 로직)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities != null && !authorities.isEmpty()) return authorities;
        return Objects.nonNull(role) ? List.of(new SimpleGrantedAuthority(role)) : List.of();
    }
}