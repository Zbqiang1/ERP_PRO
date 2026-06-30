package com.erp.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 安全配置
 *
 * 配置 JWT 无状态认证、接口权限控制、密码加密等。
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF（基于 JWT 无状态认证，不需要）
            .csrf().disable()

            // 基于 JWT，不创建 HttpSession
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            // 未认证处理
            .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()

            // 接口权限配置
            .authorizeRequests()
                // 公开接口（无需认证）
                .antMatchers(
                    "/api/auth/login",
                    "/api/auth/register",
                    "/doc.html",
                    "/webjars/**",
                    "/v2/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/druid/**"
                ).permitAll()
                // 其他所有接口需要认证
                .anyRequest().authenticated()
            .and()

            // 在 UsernamePasswordAuthenticationFilter 之前插入 JWT 过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
