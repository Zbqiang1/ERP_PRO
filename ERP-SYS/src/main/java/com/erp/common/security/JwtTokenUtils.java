package com.erp.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 令牌工具类
 *
 * 负责生成、解析和验证 JWT 令牌。
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@Slf4j
@Component
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:7200}")
    private Long expiration;

    @Value("${jwt.header:Authorization}")
    private String header;

    @Value("${jwt.token-prefix:Bearer }")
    private String tokenPrefix;

    /**
     * 获取签名密钥
     */
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 JWT 令牌
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return JWT 令牌字符串
     */
    public String generateToken(String userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从令牌中解析 Claims
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证令牌是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            log.warn("JWT 令牌验证失败：{}", e.getMessage());
            return false;
        }
    }

    /**
     * 从令牌中获取用户名
     */
    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * 从令牌中获取用户ID
     */
    public String getUserIdFromToken(String token) {
        Object userId = parseToken(token).get("userId");
        return userId != null ? String.valueOf(userId) : null;
    }

    /**
     * 检查令牌是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = parseToken(token).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 从请求头中提取令牌（去除前缀）
     */
    public String extractToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(tokenPrefix)) {
            return bearerToken.substring(tokenPrefix.length());
        }
        return bearerToken;
    }
}
