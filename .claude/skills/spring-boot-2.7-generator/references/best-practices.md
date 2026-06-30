# 代码规范与最佳实践

## 目录

1. [包结构规范](#包结构规范)
2. [命名约定](#命名约定)
3. [注释规范](#注释规范)
4. [Controller 层规范](#controller-层规范)
5. [Service 层规范](#service-层规范)
6. [Mapper 层规范](#mapper-层规范)
7. [Entity 层规范](#entity-层规范)
8. [异常处理规范](#异常处理规范)
9. [日志规范](#日志规范)
10. [测试规范](#测试规范)

---

## 包结构规范

### 分层架构（推荐）

```
{base-package}/
├── {ProjectName}Application.java          # 启动类，放在根包下
├── common/                                 # 公共模块
│   ├── config/                             # 配置类
│   │   ├── WebMvcConfig.java               # Web MVC 配置（跨域、拦截器等）
│   │   ├── MyBatisPlusConfig.java          # MyBatis-Plus 配置（分页插件等）
│   │   ├── RedisConfig.java                # Redis 配置
│   │   └── SwaggerConfig.java              # API 文档配置
│   ├── exception/                          # 全局异常处理
│   │   ├── GlobalExceptionHandler.java     # 异常拦截器
│   │   └── BusinessException.java          # 自定义业务异常
│   ├── result/                             # 统一返回体
│   │   └── Result.java                     # 返回结果封装
│   └── utils/                              # 工具类
│       └── ...
└── modules/                                # 业务模块（按功能拆分）
    ├── system/                             # 系统管理模块
    │   ├── controller/
    │   ├── service/
    │   │   └── impl/
    │   ├── mapper/
    │   └── entity/
    └── business/                           # 业务模块（示例）
        ├── controller/
        ├── service/
        │   └── impl/
        ├── mapper/
        └── entity/
```

---

## 命名约定

### 类命名

| 层级 | 命名规则 | 示例 |
|------|----------|------|
| Controller | `{功能}Controller` | `UserController`, `OrderController` |
| Service 接口 | `I{功能}Service` | `IUserService`, `IOrderService` |
| Service 实现 | `{功能}ServiceImpl` | `UserServiceImpl`, `OrderServiceImpl` |
| Mapper | `{功能}Mapper` | `UserMapper`, `OrderMapper` |
| Entity | `{表名驼峰}` | `User`, `Order`, `OrderItem` |
| Config | `{功能}Config` | `WebMvcConfig`, `RedisConfig` |
| Utils | `{功能}Utils` | `DateUtils`, `StringUtils` |
| DTO | `{功能}{场景}DTO` | `UserLoginDTO`, `OrderQueryDTO` |
| VO | `{功能}VO` | `UserVO`, `OrderDetailVO` |

### 方法命名

| 操作 | 命名规则 | 示例 |
|------|----------|------|
| 新增 | `add{Entity}` | `addUser(User user)` |
| 删除 | `delete{Entity}ById` | `deleteUserById(Long id)` |
| 修改 | `update{Entity}` | `updateUser(User user)` |
| 单个查询 | `get{Entity}ById` | `getUserById(Long id)` |
| 列表查询 | `list{Entity}` | `listUser(UserQueryDTO dto)` |
| 分页查询 | `page{Entity}` | `pageUser(Page page, UserQueryDTO dto)` |
| 批量操作 | `batch{Action}` | `batchDeleteUsers(List<Long> ids)` |

### 变量命名

- Java 变量/方法：驼峰命名 `userName`、`getUserById`
- 常量：全大写+下划线 `USER_STATUS_ACTIVE`
- 包名：全小写 `com.example.demo`
- URL 路径：小写+连字符 `/api/user-orders`

---

## 注释规范

### 类注释（必须）

```java
/**
 * 用户管理控制器
 * 
 * 提供用户的新增、删除、修改、查询等 RESTful API 接口。
 * 
 * @author {author}
 * @since {date}
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    // ...
}
```

### 方法注释（必须）

```java
/**
 * 根据用户ID查询用户信息
 * 
 * @param id 用户ID，不能为null
 * @return 用户信息，用户不存在时返回null
 */
@GetMapping("/{id}")
public Result<UserVO> getUserById(@PathVariable Long id) {
    // ...
}
```

### 字段注释（必须）

```java
/** 用户主键ID */
@TableId(type = IdType.AUTO)
private Long id;

/** 用户名，全局唯一 */
private String username;

/** 手机号 */
private String phone;

/** 创建时间 */
private LocalDateTime createTime;
```

### 业务逻辑注释

复杂业务逻辑必须添加行内注释说明：

```java
// 检查用户名是否已存在，保证全局唯一
User existUser = userMapper.selectOne(
    new LambdaQueryWrapper<User>().eq(User::getUsername, username)
);
if (existUser != null) {
    throw new BusinessException("用户名已存在");
}
```

---

## Controller 层规范

### 标准模板

```java
/**
 * {模块名称}控制器
 * 
 * @author {author}
 * @since {date}
 */
@RestController
@RequestMapping("/api/{module-url}")
public class {Module}Controller {

    private final I{Module}Service {module}Service;

    // 使用构造器注入（推荐）
    public {Module}Controller(I{Module}Service {module}Service) {
        this.{module}Service = {module}Service;
    }

    /**
     * 分页查询{模块名称}
     */
    @GetMapping("/page")
    public Result<IPage<{Module}VO>> page{Module}(@{Module}QueryDTO dto) {
        // 实现
    }

    /**
     * 根据ID查询{模块名称}
     */
    @GetMapping("/{id}")
    public Result<{Module}VO> get{Module}ById(@PathVariable Long id) {
        // 实现
    }

    /**
     * 新增{模块名称}
     */
    @PostMapping
    public Result<Void> add{Module}(@RequestBody @Valid {Module}DTO dto) {
        // 实现
    }

    /**
     * 修改{模块名称}
     */
    @PutMapping("/{id}")
    public Result<Void> update{Module}(@PathVariable Long id, @RequestBody @Valid {Module}DTO dto) {
        // 实现
    }

    /**
     * 删除{模块名称}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete{Module}ById(@PathVariable Long id) {
        // 实现
    }
}
```

### 关键规范

1. URL 使用 RESTful 风格，资源名用复数 `/api/users` 而非 `/api/user`
2. 使用 `@Valid` / `@Validated` 进行参数校验
3. 使用构造器注入，避免 `@Autowired` 字段注入
4. Controller 中不放业务逻辑，只做参数接收和结果返回

---

## Service 层规范

### 接口定义

```java
/**
 * {模块名称}服务接口
 * 
 * @author {author}
 * @since {date}
 */
public interface I{Module}Service extends IService<{Entity}> {

    /**
     * 分页查询{模块名称}
     */
    IPage<{Module}VO> page{Module}({Module}QueryDTO dto);

    /**
     * 根据ID查询{模块名称}
     */
    {Module}VO get{Module}ById(Long id);

    /**
     * 新增{模块名称}
     */
    void add{Module}({Module}DTO dto);

    /**
     * 修改{模块名称}
     */
    void update{Module}(Long id, {Module}DTO dto);

    /**
     * 删除{模块名称}
     */
    void delete{Module}ById(Long id);
}
```

### 实现类

```java
/**
 * {模块名称}服务实现
 * 
 * @author {author}
 * @since {date}
 */
@Service
public class {Module}ServiceImpl 
        extends ServiceImpl<{Module}Mapper, {Entity}> 
        implements I{Module}Service {

    @Override
    public IPage<{Module}VO> page{Module}({Module}QueryDTO dto) {
        Page<{Entity}> page = new Page<>(dto.getCurrent(), dto.getSize());
        // 构建查询条件
        LambdaQueryWrapper<{Entity}> wrapper = new LambdaQueryWrapper<>();
        // ...
        IPage<{Entity}> entityPage = this.page(page, wrapper);
        // 转换为 VO
        return entityPage.convert(this::toVO);
    }

    // 其他方法实现...
    
    /**
     * Entity 转 VO
     */
    private {Module}VO toVO({Entity} entity) {
        if (entity == null) return null;
        {Module}VO vo = new {Module}VO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
```

---

## Mapper 层规范

```java
/**
 * {Entity}数据访问层
 * 复杂查询请编写在对应的 XML 文件中
 * 
 * @author {author}
 * @since {date}
 */
@Mapper
public interface {Module}Mapper extends BaseMapper<{Entity}> {

    /**
     * 自定义查询：根据条件查询{模块}列表
     * 
     * @param dto 查询条件
     * @return 结果列表
     */
    List<{Module}VO> selectListByCondition(@Param("dto") {Module}QueryDTO dto);
}
```

对应的 XML 文件放在 `src/main/resources/mapper/{module}/{Module}Mapper.xml`。

---

## Entity 层规范

```java
/**
 * {功能}实体类
 * 对应数据库表 t_{table_name}
 * 
 * @author {author}
 * @since {date}
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_{table_name}")
public class {Entity} implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除标识：0-未删除，1-已删除 */
    @TableLogic
    private Integer deleted;
}
```

### 数据库表名约定

- 表名：`t_` 前缀 + 下划线分隔，如 `t_user`、`t_order_item`
- 字段名：下划线分隔，如 `user_name`、`create_time`
- 主键：统一使用 `id`，自增

---

## 异常处理规范

### 自定义业务异常

```java
/**
 * 业务异常类
 * 用于在业务逻辑中抛出可预见的异常
 * 
 * @author {author}
 * @since {date}
 */
public class BusinessException extends RuntimeException {

    /** 错误码 */
    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
```

### 全局异常拦截

```java
/**
 * 全局异常处理器
 * 统一处理所有未捕获的异常，返回标准 Result 格式
 * 
 * @author {author}
 * @since {date}
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /** 处理业务异常 */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /** 处理参数校验异常 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        return Result.fail(400, message);
    }

    /** 处理未知异常 */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.fail("系统繁忙，请稍后再试");
    }
}
```

---

## 日志规范

### 日志级别使用

| 级别 | 使用场景 |
|------|----------|
| ERROR | 系统错误，需要立即处理（如数据库连接失败） |
| WARN | 警告信息，潜在问题（如配置缺失使用默认值） |
| INFO | 重要业务节点（如订单创建、用户注册） |
| DEBUG | 调试信息（如方法入参出参、SQL 参数） |

### Logback 配置要点

- 使用 Lombok `@Slf4j` 注解
- 日志格式包含：时间、线程、级别、类名、消息
- 开发环境输出到控制台，生产环境输出到文件
- 按天滚动，保留 30 天

---

## 测试规范

### Service 层测试模板

```java
/**
 * {模块}服务测试类
 * 
 * @author {author}
 * @since {date}
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class {Module}ServiceTest {

    @Autowired
    private I{Module}Service {module}Service;

    @Test
    public void testGetById() {
        // 测试正常查询
        Long id = 1L;
        {Module}VO vo = {module}Service.get{Module}ById(id);
        assertNotNull(vo);
    }

    @Test
    public void testAdd() {
        // 测试新增
        {Module}DTO dto = new {Module}DTO();
        // 设置必要字段...
        {module}Service.add{Module}(dto);
    }
}
```
