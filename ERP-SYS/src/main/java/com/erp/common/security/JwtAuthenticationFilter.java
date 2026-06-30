package com.erp.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器
 *
 * 在每次请求时拦截并验证 JWT 令牌，将认证信息写入 SecurityContext。
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        // 从请求头中获取 JWT 令牌
        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token) && jwtTokenUtils.validateToken(token)) {
            // 解析用户信息
            String username = jwtTokenUtils.getUsernameFromToken(token);
            String userId = jwtTokenUtils.getUserIdFromToken(token);

            // 构建认证对象
            JwtUserDetails userDetails = new JwtUserDetails(userId, username);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, token, Collections.emptyList());

            // 设置到安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("JWT 认证成功：username={}, userId={}", username, userId);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求中提取 JWT 令牌
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
