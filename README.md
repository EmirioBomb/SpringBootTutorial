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

#### 配置

```yaml
logging:
  level:
    # 若想隐藏或禁用日志，可全局调整日志等级为: error, fatal
    root: info
    com.emirio.configuration: info
  file:
    name: logs/log.log
```

### 2. @Value
#### 配置
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

### 3. @ConfigurationProperties
`注意点`
1. 使用该注解前提是 类被标记为一个 Spring 的组件，使其被 Spring 容器管理，如 @Component, @Service 等
```java
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfigProperties {
    private String name;
    private String version;
    private String author;
}
```

2. 类必须有 Setter 方法
```java
@Setter
public class AppConfigProperties {
    private String name;
    private String version;
    private String author;
}
```