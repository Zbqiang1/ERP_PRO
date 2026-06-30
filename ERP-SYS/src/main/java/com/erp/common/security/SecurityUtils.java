package com.erp.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring Security 工具类
 *
 * 从 SecurityContext 中获取当前登录用户的信息。
 * 配合 JwtAuthenticationFilter 使用，该拦截器已将 JWT 中解析的用户信息写入 SecurityContext。
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@Slf4j
public class SecurityUtils {

    private SecurityUtils() {
        // 工具类，禁止实例化
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID，未登录或无法获取时返回null
     */
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof JwtUserDetails) {
            String userId = ((JwtUserDetails) principal).getUserId();
            return userId != null ? userId : null;
        }
        // 如果 principal 是字符串（anonymousUser），返回null
        if (principal instanceof String && "anonymousUser".equals(principal)) {
            return null;
        }
        log.debug("无法从 SecurityContext 获取用户ID，principal 类型：{}",
                principal != null ? principal.getClass().getName() : "null");
        return null;
    }

    /**
     * 获取当前登录用户名
     *
     * @return 用户名，未登录时返回null
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof JwtUserDetails) {
            return ((JwtUserDetails) principal).getUsername();
        }
        if (principal instanceof String && !"anonymousUser".equals(principal)) {
            return (String) principal;
        }
        return null;
    }

    /**
     * 判断当前请求是否已认证
     *
     * @return true-已登录，false-未登录
     */
    public static boolean isAuthenticated() {
        return getCurrentUserId() != null;
    }
}
