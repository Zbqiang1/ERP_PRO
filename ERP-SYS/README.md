# ERP系统

## 项目简介

ERP系统是一套基于Spring Boot开发的企业资源计划管理系统，涵盖企业核心业务流程，包括系统管理、财务管理、库存管理、采购管理、销售管理、人力资源管理、生产管理、报表与预警共八大模块。系统采用前后端分离架构，支持Docker容器化部署，适用于中小型制造企业的信息化管理需求。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| JDK | 8 | Java开发工具包 |
| Spring Boot | 2.7.18 | 基础框架 |
| MyBatis-Plus | 3.5.5 | ORM框架 |
| MySQL | 8.0 | 关系型数据库 |
| Redis | 7 | 缓存服务 |
| RabbitMQ | 3.12 | 消息队列 |
| Flowable | 6.8.0 | 工作流引擎 |
| XXL-Job | 2.4.0 | 分布式定时任务 |
| Spring Security | 5.7.x | 安全认证框架 |
| JWT | 0.11.5 | Token认证 |
| Knife4j | 4.3.0 | API文档 |
| Lombok | 1.18.30 | 代码简化 |
| Docker | - | 容器化部署 |

## 快速开始

### 环境要求

- JDK 8+
- Maven 3.6+
- MySQL 8.0+
- Redis 7+
- RabbitMQ 3.12+

### 本地启动

1. **克隆项目**

```bash
git clone <repository-url>
cd ERP-SYS
```

2. **创建数据库**

执行 `sql/init.sql` 脚本创建数据库及基础数据：

```bash
mysql -u root -p < sql/init.sql
```

3. **修改配置文件**

编辑 `src/main/resources/application.yml`，配置数据库、Redis、RabbitMQ连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/erp_db?useUnicode=true&characterEncoding=utf8mb4&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
  redis:
    host: localhost
    port: 6379
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

4. **启动项目**

```bash
mvn clean package -DskipTests
java -jar target/*.jar
```

5. **访问系统**

- 后端API文档：http://localhost:8080/doc.html
- 默认管理员账号：`admin` / `123456`

### Docker部署（推荐）

使用Docker Compose一键启动全套服务：

```bash
# 构建并启动所有服务
docker-compose up -d

# 查看服务运行状态
docker-compose ps

# 查看日志
docker-compose logs -f erp-sys

# 停止所有服务
docker-compose down
```

启动后将自动执行数据库初始化脚本，创建数据库和默认数据。

| 服务 | 端口 | 说明 |
|------|------|------|
| erp-sys | 8080 | ERP应用服务 |
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存 |
| RabbitMQ | 5672 | 消息队列 |
| RabbitMQ管理面板 | 15672 | 消息队列管理界面 |

## 项目结构

```
ERP-SYS/
├── sql/
│   └── init.sql                        # 数据库初始化脚本
├── src/
│   └── main/
│       ├── java/com/erp/
│       │   ├── common/                 # 公共组件
│       │   │   ├── config/             # 配置类
│       │   │   ├── exception/          # 全局异常处理
│       │   │   ├── result/             # 统一返回结果
│       │   │   └── security/           # 安全认证组件
│       │   └── modules/                # 业务模块
│       │       ├── system/             # 系统管理模块
│       │       │   ├── entity/         # 实体类
│       │       │   ├── mapper/         # 数据访问层
│       │       │   ├── service/        # 服务接口
│       │       │   └── controller/     # 控制器
│       │       ├── finance/            # 财务管理模块
│       │       ├── inventory/          # 库存管理模块
│       │       ├── purchase/           # 采购管理模块
│       │       ├── sales/              # 销售管理模块
│       │       ├── hr/                 # 人力资源管理模块
│       │       ├── production/         # 生产管理模块
│       │       └── report/             # 报表与预警模块
│       └── resources/
│           ├── application.yml         # 应用主配置
│           └── application-docker.yml  # Docker环境配置
├── Dockerfile                          # Docker构建文件
├── docker-compose.yml                  # Docker编排文件
├── pom.xml                             # Maven配置
└── README.md                           # 项目说明文档
```

## 模块介绍

### 1. 系统管理模块
系统的基础功能模块，负责用户、角色、菜单权限的管理。
- 用户管理：系统用户的增删改查、状态管理
- 角色管理：角色定义及权限分配
- 菜单管理：系统菜单和功能按钮的配置

### 2. 财务管理模块
企业财务核算功能，支持基础的会计记账业务。
- 会计科目：科目编码、分类（资产/负债/权益/成本/损益）
- 记账凭证：凭证录入、审核、过账流程
- 总账查询：按科目查询借贷余额

### 3. 库存管理模块
物料、仓库及库存量管理，支持出入库业务操作。
- 仓库管理：仓库信息维护
- 物料管理：物料编码、规格、分类、安全库存
- 库存管理：库存量查询、出入库单据处理
- 库存盘点：库存盘点及差异调整

### 4. 采购管理模块
供应商管理及采购订单的完整业务流程。
- 供应商管理：供应商信息维护
- 采购订单：订单创建、下达、收货跟踪
- 采购入库：与库存模块联动完成入库

### 5. 销售管理模块
客户管理及销售订单的全流程跟踪。
- 客户管理：客户信息维护
- 销售订单：订单创建、确认、发货跟踪
- 销售出库：与库存模块联动完成出库

### 6. 人力资源管理模块
员工信息、考勤及薪资管理。
- 员工管理：员工档案、入职离职管理
- 考勤管理：打卡记录、考勤统计
- 薪资管理：工资计算、发放记录

### 7. 生产管理模块
面向制造业的生产计划、执行与检验管理。
- BOM管理：物料清单（BOM）的维护与版本管理
- MPS主生产计划：计划排产与进度跟踪
- MRP运算：物料需求计算，生成采购/生产建议
- 工单管理：生产工单的创建、派工与完工管理
- 工艺路线：工序定义与加工进度跟踪
- 委外加工：委外合同、发出与回收管理
- 生产检验：首检、巡检、终检记录

### 8. 报表与预警模块
系统数据可视化展示与业务异常预警。
- 仪表盘：自定义组件（数字卡片/柱状图/折线图/饼图）
- 报表模板：日报、周报、月报、年报模板管理
- 业务预警：库存预警、应收账款预警、质量异常预警、交期预警

## API接口文档

项目集成Knife4j，启动后访问：

```
http://localhost:8080/doc.html
```

接口文档自动生成，支持在线调试。

## 默认账号

| 账号 | 密码 | 角色 |
|------|------|------|
| admin | 123456 | 超级管理员 |

## 项目特性

- **统一返回格式**：所有接口返回 `Result<T>` 统一结构
- **全局异常处理**：`@RestControllerAdvice` 统一拦截业务异常
- **逻辑删除**：使用MyBatis-Plus `@TableLogic` 实现数据软删除
- **字段自动填充**：`create_time`、`update_time` 自动填充
- **构造器注入**：使用Lombok `@RequiredArgsConstructor` 实现依赖注入
- **Docker容器化**：支持多阶段构建，一键部署
- **中间件集成**：Redis缓存、RabbitMQ消息队列、XXL-Job定时任务、Flowable工作流
