# SpringBootTutorial
> SpringBoot tutorial project

## Basic
### 1. 日志
> 添加日志 **@Slf4j** 相关配置

#### 添加依赖
`pom.xml, 添加Lombok依赖`
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
    <scope>provided</scope>
</dependency>
```

#### 使用
`在类上添加注解: @Slf4j，即可直接使用日志对象log`
```java
@Slf4j
@SpringBootApplication
public class ConfigurationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigurationApplication.class, args);
    }
}
```

`反编译后的.class文件`
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConfigurationApplication {
    // 由 @Slf4j 自动生成的日志对象
    private static final Logger log = LoggerFactory.getLogger(ConfigurationApplication.class);
    public ConfigurationApplication() {
    }
    public static void main(String[] args) {
        SpringApplication.run(ConfigurationApplication.class, args);
    }
}
```

#### application.yml 配置

```yaml
logging:
  level:
    # 若想隐藏或禁用日志，可全局调整日志等级为: error, fatal
    root: info
    com.emirio.configuration: info
  file:
    name: logs/log.log
```

### 2. 配置注解
#### 2.1 @Value
`application.yml配置`
```yaml
app:
  name: SpringBootTutorial
  version: 1.0.1
```

```java
// @Value 不支持静态字段注入，但不会阻止程序执行，会有对应提醒
@Value("${app.name}")
public static String _appName;

@Value("${app.name}")
public String appName;

@PostConstruct
public void initInfo() {
    log.info("正在初始化静态系统名称: {}", _appName);
    log.info("正在初始化非静态系统名称: {}", appName);
}
```

`日志输出结果:`
```log
2024-12-03 16:56:48.334  INFO 36286 --- [           main] f.a.AutowiredAnnotationBeanPostProcessor : Autowired annotation is not supported on static fields: public static java.lang.String com.emirio.configuration.ConfigurationApplication._appName
2024-12-03 16:56:48.337  INFO 36286 --- [           main] c.e.c.ConfigurationApplication           : 正在初始化静态系统名称: null
2024-12-03 16:56:48.337  INFO 36286 --- [           main] c.e.c.ConfigurationApplication           : 正在初始化非静态系统名称: SpringBootTutorial
```

`注:`
1. Spring 会在对象实例化后，通过反射为实例字段注入值，而 static 字段不属于实例，属于类本身，Spring 无法直接处理。
2. 静态字段在类加载时就初始化，而 Spring 容器可能尚未完全启动，导致无法提供配置值。

#### 2.2 @ConfigurationProperties
`注意点`
1. 使用该注解前提是 类被标记为一个 Spring 的组件，使其被 Spring 容器管理，如 @Component, @Service 等
   `方法1:`
```java
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfigProperties {
    private String name;
    private String version;
    private String author;
}
```

`方法2:`
```java
@Slf4j
@SpringBootApplication
// 主程序使用 EnableConfigurationProperties 添加指定类
@EnableConfigurationProperties(AppConfigProperties.class)
public class ConfigurationApplication {...}

// 配置类可不使用 @Component 等
@ConfigurationProperties(prefix = "app")
@Setter
public class AppConfigProperties {
    private String name;
    private String version;
    private String author;
```

2. 类必须有 Setter 方法
```java
// Lombok可直接使用 @Setter
@Setter
public class AppConfigProperties {
    private String name;
    private String version;
    private String author;

    // 未用 Lombok 直接生成 setter 方法, 内部 setter 会覆盖 Lombok @Setter
    public void setName(String name) {
        this.name = name;
    }
}
```

#### 2.3 @PropertySource
> `@PropertySource` 并非必须以 `classpath:` 开头，但推荐显式指定，以减少路径不明确的问题。如果文件在 `src/main/resources` 中，使用 `classpath:` 前缀是最佳实践。


```properties
#environment.properties 配置文件
jdk.version=1.8
```

```java
// 指定配置文件路径 
@PropertySource("classpath:environment.properties")
public class ConfigurationApplication {
    // 根据 properties 文件中获取配置信息
    @Value("${jdk.version}")
    private String jdkVersion;
}
```

| **路径类型**       | **格式**                        | **解释**                                                                 |
|--------------------|--------------------------------|-------------------------------------------------------------------------|
| **类路径**         | `classpath:custom.properties` | 从类路径加载，例如 `src/main/resources`                                |
| **相对路径**       | `custom.properties`           | 从当前工作目录加载文件（可能是项目根目录，通常不推荐直接使用相对路径） |
| **绝对文件路径**   | `file:/path/to/file.properties`| 从文件系统中的绝对路径加载                                             |

#### 2.4 结论

| **注解**                 | **适用场景**                                 | **优缺点**                                                                 |
|---------------------------|---------------------------------------------|-----------------------------------------------------------------------------|
| `@Value`                 | 注入单个配置值                              | 简单、直接，不适合复杂配置                                                  |
| `@ConfigurationProperties` | 绑定一组属性值到类                         | 适合复杂配置结构，支持分组管理                                              |
| `@PropertySource`        | 加载自定义 `properties` 文件                 | 适合非默认路径配置，不支持 `yaml`                                           |
| `@Environment`           | 动态获取配置值                              | 灵活，但代码复杂度较高                                                     |
| `@TestPropertySource`    | 测试环境下加载特定配置                      | 测试专用，用于覆盖默认配置                                                 |
| `@ImportResource`        | 加载 XML 文件中的属性占位符配置              | 适合需要兼容 XML 配置的场景                                                |

对于主流 Spring Boot 项目：
- 使用 **`@ConfigurationProperties`** 和 **`@Value`** 是最常见的处理配置的方式。
- `@PropertySource` 较少用，但在处理非默认路径配置时仍有用武之地。