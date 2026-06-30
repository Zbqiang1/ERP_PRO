# Spring Boot 2.7.x 版本配置速查

## 版本约束

| 组件 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.18（最高） | 2.7.x 系列最终版本，建议默认使用 |
| Spring Cloud | 2021.0.9 | 与 Boot 2.7.x 兼容 |
| Spring Cloud Alibaba | 2021.0.6.1 | 微服务全家桶 |
| JDK | 1.8 (8) | 最低要求，2.7.x 最后一个支持 JDK 8 的大版本 |
| Maven | 3.5+ | 构建工具 |

## 常用依赖及版本

### 核心 Starter

```xml
<!-- Spring Boot 父 POM -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.18</version>
    <relativePath/>
</parent>

<properties>
    <java.version>1.8</java.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <mybatis-plus.version>3.5.5</mybatis-plus.version>
    <druid.version>1.2.20</druid.version>
    <hutool.version>5.8.25</hutool.version>
</properties>
```

### Web & 数据访问

```xml
<!-- Spring Web (含 Spring MVC + Tomcat) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- MyBatis-Plus（推荐使用，替代 MyBatis） -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>${mybatis-plus.version}</version>
</dependency>

<!-- MySQL 驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- PostgreSQL 驱动（备选） -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Druid 连接池 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>${druid.version}</version>
</dependency>
```

### Redis & 缓存

```xml
<!-- Spring Data Redis -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- Spring Cache（配合 Redis 使用） -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

<!-- 连接池（Redis 需要） -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```

### 消息队列

```xml
<!-- RabbitMQ -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>

<!-- Kafka -->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### 定时任务

```xml
<!-- Spring 内置定时任务（轻量级） -->
<!-- 无需额外依赖，使用 @EnableScheduling 即可 -->

<!-- Quartz（重量级，支持持久化） -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency>

<!-- XXL-Job（分布式任务调度） -->
<dependency>
    <groupId>com.xuxueli</groupId>
    <artifactId>xxl-job-core</artifactId>
    <version>2.4.0</version>
</dependency>
```

### 工具类

```xml
<!-- Hutool 工具集 -->
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>${hutool.version}</version>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

### Swagger / API 文档

```xml
<!-- Knife4j（Swagger 增强版，UI 更好） -->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi2-spring-boot-starter</artifactId>
    <version>4.3.0</version>
</dependency>
```

### 测试

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- H2 内存数据库（测试用） -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

## application.yml 配置模板

### 数据源配置

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/{database_name}?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
    druid:
      initial-size: 5
      min-idle: 5
      max-active: 20
      max-wait: 60000
      time-between-eviction-runs-millis: 60000
      min-evictable-idle-time-millis: 300000
      validation-query: SELECT 1
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
```

### Redis 配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password:
    database: 0
    timeout: 3000ms
    lettuce:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0
```

### RabbitMQ 配置

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
    listener:
      simple:
        acknowledge-mode: manual
        prefetch: 1
```

### MyBatis-Plus 配置

```yaml
mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: {base-package}.modules.*.entity
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: false
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

## 关键注解速查

| 注解 | 用途 | 示例 |
|------|------|------|
| `@SpringBootApplication` | 启动类 | 放在 Application 类上 |
| `@MapperScan` | 扫描 Mapper | `@MapperScan("com.xxx.modules.*.mapper")` |
| `@RestController` | REST 控制器 | 放 Controller 类上 |
| `@RequestMapping("/api/xxx")` | 路径映射 | 放 Controller 类/方法上 |
| `@Service` | 服务层 | 放 Service 实现类上 |
| `@TableName("t_xxx")` | 表名映射 | 放 Entity 类上 |
| `@TableId(type = IdType.AUTO)` | 主键策略 | 放 id 字段上 |
| `@TableLogic` | 逻辑删除 | 放 deleted 字段上 |
| `@ConfigurationProperties` | 配置绑定 | 放配置类上 |
| `@GlobalExceptionHandler` | 全局异常 | 放异常处理类上 |

## JDK 8 特别说明

1. **Lambda 表达式**可用，Stream API 可用
2. **Optional** 可用
3. **不支持** `var` 关键字（JDK 10+）
4. **不支持** `switch` 表达式（JDK 14+）
5. **不支持** 文本块 `""" """`（JDK 13+）
6. **不支持** Record 类（JDK 14+）
7. 日期使用 `java.util.Date` 或 `java.time.LocalDateTime`（JDK 8 已支持 JSR-310）

## 常见问题

### Maven 编译插件配置

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <excludes>
                    <exclude>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                    </exclude>
                </excludes>
            </configuration>
        </plugin>
    </plugins>
</build>
```
