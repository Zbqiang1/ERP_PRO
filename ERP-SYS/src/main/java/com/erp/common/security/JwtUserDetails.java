package com.erp.common.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * JWT 用户详情
 *
 * 封装 JWT 中解析出的用户信息，供 Spring Security 使用。
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@Getter
@AllArgsConstructor
public class JwtUserDetails implements UserDetails {

    private String userId;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUserDetails(String userId, String username) {
        this.userId = userId;
        this.username = username;
        this.authorities = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
