package com.erp;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ERP 管理系统启动类
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
@MapperScan("com.erp.modules.*.mapper")
public class ErpSysApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(ErpSysApplication.class, args);

        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String activeProfile = env.getProperty("spring.profiles.active", "");

        log.info("\n----------------------------------------------------------\n" +
                "  应用 '{}' 启动成功！\n" +
                "  环境：{}\n" +
                "  访问地址：\n" +
                "    本地：http://localhost:{}{}\n" +
                "    网络：http://{}:{}{}\n" +
                "  API 文档：http://localhost:{}{}/doc.html\n" +
                "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                activeProfile,
                port, contextPath,
                ip, port, contextPath,
                port, contextPath);
    }
}
