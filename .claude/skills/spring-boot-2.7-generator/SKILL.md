---
name: spring-boot-2.7-generator
description: 根据项目描述自动生成 Spring Boot 2.7.x + JDK 8 单体项目，包含全套企业级配置（Web、MyBatis-Plus、Redis、消息队列、定时任务、日志）、Docker 配置、单元测试模板和代码生成器。当用户提到"生成Spring Boot项目"、"创建Spring Boot项目"、"搭建Spring Boot"、"新起一个Spring Boot项目"时使用。
---

# Spring Boot 2.7.x 项目生成器

## 概述

根据用户的自然语言描述，自动生成一个完整、可运行的 Spring Boot 2.7.x + JDK 8 单体项目。项目直接创建在用户指定的目录下，包含所有必要的配置文件、业务代码骨架、Docker 部署配置和单元测试模板。

## 核心原则

1. **生成即能跑**：项目生成后 `mvn clean package` 必须成功，`mvn spring-boot:run` 必须能启动
2. **中文注释**：所有生成的代码注释、日志消息、README 文档一律使用中文
3. **规范整洁**：遵循阿里巴巴 Java 开发手册规范，代码风格统一
4. **灵活定制**：根据用户具体描述灵活调整，不强行塞入用户没要的组件

## 工作流程

### 第一步：信息收集

在生成项目之前，必须收集以下信息。对于用户描述中已明确的部分，无需重复询问。

**必须确认的信息：**
- 项目名称（Maven artifactId）
- 项目包名（如 `com.example.demo`）
- 项目存放路径（用户希望在哪里创建项目文件夹）

**按需确认的信息（用户没提就问，提了就跳过）：**
- 需要哪些 Spring Boot Starter（Web、JPA、MyBatis-Plus、Redis、RabbitMQ/Kafka、Quartz/XXL-Job 等）
- 数据库类型及连接信息（MySQL/PostgreSQL，主机、端口、库名、用户名、密码）
- 是否需要注册中心/配置中心（Nacos/Eureka/Apollo）
- 需要生成哪些业务模块（用户管理、订单管理、商品管理等）
- 包结构偏好（分层架构 / DDD 领域驱动）

使用 `AskUserQuestion` 工具以选择题方式询问，减少用户打字负担。

### 第二步：生成项目骨架

参考 `references/spring-boot-2.7-configs.md` 中的版本配置，使用 `assets/templates/pom.xml.template` 生成 `pom.xml`。

**关键版本约束：**
- Spring Boot: 2.7.x（最高 2.7.18）
- JDK: 1.8
- MyBatis-Plus: 3.5.x
- 其他依赖版本参见参考文件

**目录结构标准：**
```
{project-name}/
├── pom.xml
├── README.md
├── Dockerfile
├── docker-compose.yml
├── src/
│   ├── main/
│   │   ├── java/{package-path}/
│   │   │   ├── {ProjectName}Application.java    # 启动类
│   │   │   ├── common/                           # 公共类
│   │   │   │   ├── config/                       # 配置类
│   │   │   │   ├── exception/                    # 异常处理
│   │   │   │   ├── result/                       # 统一返回
│   │   │   │   └── utils/                        # 工具类
│   │   │   ├── modules/                          # 业务模块
│   │   │   │   └── {module-name}/
│   │   │   │       ├── controller/
│   │   │   │       ├── service/
│   │   │   │       │   └── impl/
│   │   │   │       ├── mapper/
│   │   │   │       └── entity/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── logback-spring.xml
│   └── test/
│       └── java/{package-path}/
│           └── {ProjectName}ApplicationTests.java
```

### 第三步：生成配置文件

**application.yml** — 参考 `assets/templates/application.yml.template`：
- 服务端口、应用名
- 激活的 profile
- 各环境差异化配置放 `application-dev.yml` / `application-prod.yml`

**logback-spring.xml** — 包含控制台输出和文件滚动策略，日志格式使用中文友好格式。

### 第四步：生成基础代码

根据 `references/best-practices.md` 中的规范，生成以下基础类：

1. **启动类** — 标准 Spring Boot 入口，包含 `@MapperScan` 等必要注解
2. **统一返回类** — `Result<T>`，包含 code、message、data，提供静态成功/失败方法
3. **全局异常处理** — `@RestControllerAdvice`，捕获常见异常
4. **基础配置类** — 跨域、日期格式化、分页插件等

### 第五步：生成业务模块代码

根据用户的功能描述，为每个业务模块生成标准四层代码：

- **Controller 层**：RESTful 风格，`@RequestMapping` 使用复数路径
- **Service 层**：接口 + 实现类，实现类使用 `@Service` 注解
- **Mapper 层**：继承 MyBatis-Plus `BaseMapper<T>`，复杂查询写在 XML 中
- **Entity 层**：使用 `@TableName`、`@TableId` 等 MyBatis-Plus 注解

每个方法都需要中文注释说明功能。

### 第六步：生成配套配置

**必须生成：**

1. **Dockerfile** — 参考 `assets/templates/Dockerfile.template`，使用多阶段构建，基础镜像 `openjdk:8-jre-alpine`
2. **docker-compose.yml** — 参考 `assets/templates/docker-compose.yml.template`，根据实际需要的中间件生成对应服务定义
3. **单元测试模板** — 参考 `assets/templates/BaseTest.java.template`，为每个 Service 生成基础测试类

**按需生成（用户提出才生成）：**

4. **MyBatis-Plus 代码生成器** — 参考 `assets/templates/code-generator.yml.template`，在 `src/test` 下生成代码生成器类

### 第七步：生成 README.md

生成项目 README，包含：
- 项目简介
- 技术栈列表
- 快速启动指南（含 Docker 和本地两种方式）
- 项目结构说明
- 主要功能介绍

全部使用中文。

### 第八步：最终验证

生成完毕后，执行以下检查并汇报结果：
- 文件数量统计（.java、.xml、.yml 等）
- 关键配置完整性检查
- 给出启动命令提示：`mvn clean package -DskipTests && mvn spring-boot:run`
- 如用户提供了数据库信息，提醒先执行 SQL 初始化脚本

## 参考资源

- **版本配置**: `references/spring-boot-2.7-configs.md` — Spring Boot 2.7.x 依赖版本速查
- **编码规范**: `references/best-practices.md` — 代码风格、命名约定、异常处理规范
- **模板文件**: `assets/templates/` — POM、配置、Docker、代码模板
