package com.erp.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.erp.common.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 配置
 *
 * 配置分页插件、自动填充创建/更新时间和操作人ID。
 * createBy / updateBy 通过 SecurityUtils 从 SecurityContext 中获取当前登录用户ID，
 * 实现在 MetaObjectHandler 中统一填充，无需在业务代码中手动设置。
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@Slf4j
@Configuration
public class MyBatisPlusConfig {

    /**
     * MyBatis-Plus 拦截器 — 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 自动填充处理器
     *
     * INSERT 时自动填充：createTime、updateTime、createBy、updateBy
     * UPDATE 时自动填充：updateTime、updateBy
     *
     * createBy / updateBy 通过 SecurityUtils.getCurrentUserId() 获取当前登录用户ID，
     * 如果无法获取（如定时任务、未登录接口），则填充为 null（不为 null 时会覆盖手动设置的值）。
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {

            @Override
            public void insertFill(MetaObject metaObject) {
                LocalDateTime now = LocalDateTime.now();
                String currentUserId = SecurityUtils.getCurrentUserId();

                // 时间字段
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
                this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);

                // 操作人字段（仅当实体包含该字段时才填充）
                if (metaObject.hasSetter("createBy") && currentUserId != null) {
                    this.strictInsertFill(metaObject, "createBy", String.class, currentUserId);
                }
                if (metaObject.hasSetter("updateBy") && currentUserId != null) {
                    this.strictInsertFill(metaObject, "updateBy", String.class, currentUserId);
                }

                log.debug("INSERT 自动填充：createTime={}, updateTime={}, createBy={}, updateBy={}",
                        now, now, currentUserId, currentUserId);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                String currentUserId = SecurityUtils.getCurrentUserId();

                // 时间字段
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

                // 操作人字段
                if (metaObject.hasSetter("updateBy") && currentUserId != null) {
                    this.strictUpdateFill(metaObject, "updateBy", String.class, currentUserId);
                }

                log.debug("UPDATE 自动填充：updateTime={}, updateBy={}",
                        LocalDateTime.now(), currentUserId);
            }
        };
    }
}
