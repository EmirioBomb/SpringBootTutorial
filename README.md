# SpringBootTutorial
> SpringBoot tutorial project

- [SpringBootTutorial](#springboottutorial)
   - [Basic](#basic)
      - [1. 日志](#1-日志)
         - [添加依赖](#添加依赖)
         - [使用](#使用)
         - [application.yml 配置](#applicationyml-配置)
         - [Logback](#logback)
            - [配置](#配置)
         - [异步日志](#异步日志)
            - [动态异步日志](#动态异步日志)
      - [2. 配置注解](#2-配置注解)
         - [2.1 @Value](#21-value)
         - [2.2 @ConfigurationProperties](#22-configurationproperties)
         - [2.3 @PropertySource](#23-propertysource)
         - [2.4 结论](#24-结论)
      - [3. Runner](#3-runner)
      - [4. Banner](#4-banner)
      - [5. application 配置文件](#5-application-配置文件)


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

#### Logback

**1. 优先级规则**
- **`application.yaml` 的 `logging` 配置优先级更高**：  
  Spring Boot 会先解析 `application.yaml` 中的 `logging` 配置，并将这些设置直接应用到日志系统。

- **`logback-spring.xml` 是更具体的配置**：  
  如果存在 `logback-spring.xml` 文件，Spring Boot 会使用它来定义更详细的日志记录规则，例如自定义 Appender 和 Logger。**但某些全局参数（如 `level` 和 `file`）可能会被 `application.yaml` 中的配置覆盖。**
- 若同时存在 **`logback-spring.xml` 与 `logback.xml`**：logback.xml 优先级高于 logback-spring.xml

##### 配置
```yml
# logback-spring.xml 日志配置管理
logging:
  # 手动指定 logback 配置
  # config: classpath:logback.xml
  config: classpath:logback-spring.xml
  file:
    name: logs/logback-spring.log
    max-size: 100MB
    max-history: 3
    total-size-cap: 1GB
    clean-history-on-start: false
  level:
    root: info
    com.emirio.configuration: info

# logback-spring.xml 通过配置动态指定日志文件
# logback:
#   file:
#     # 自定义日志文件名称
#     name: logs/custom-logback-spring.log
```

#### 异步日志

* **日志丢失:** 当应用程序在非正常退出时，异步日志可能尚未写入完全。
* **延迟:** 异步日志可能导致稍微延迟的日志输出，这可能会影响调试的实时性。
* **默认:** `ConsoleAppender` 和 `RollingFileAppender` 默认都是同步的。

```xml
 <!-- 异步写入文件配置，可提高日志TPS，一般更适用于高并发的生产环境或压测环境 -->
<appender name="ASYNC-FILE" class="ch.qos.logback.classic.AsyncAppender">
    <!-- 写入日志文件节点，一般无需处理控制台节点的异常 -->
    <appender-ref ref="FILE"/>
    <!-- discardingThreshold 设置为 0，表示即使队列达到最大容量，也不会丢弃任何日志（除非队列完全满了） -->
    <!-- 从源代码 AsyncAppenderBase.class 中可知，默认值在-1时会丢弃日志 -->
    <discardingThreshold>0</discardingThreshold>
    <!-- 默认队列大小: 256 -->
    <queueSize>1024</queueSize>
    <!-- 当队列满时丢弃日志，虽一定程序上可提升性能，但会丢失部分日志，个人不建议开启 -->
    <!-- <neverBlock>true</neverBlock> -->
</appender>
```

##### 动态异步日志
> **根据不同环境，决定是否启用异步日志节点**

```xml
<!-- 异步写入文件配置，可提高日志TPS，一般更适用于高并发的生产环境或压测环境 -->
<!-- 通过 SpringProfile 配置，根据不同环境开启异步日志 -->
<springProfile name="dev,prod">
    <!-- 异步写入文件配置，可提高日志TPS，一般更适用于高并发的生产环境或压测环境 -->
    <appender name="ASYNC-FILE" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 写入日志文件节点，一般无需处理控制台节点的异常 -->
        <appender-ref ref="FILE"/>
        <!-- discardingThreshold 设置为 0，表示即使队列达到最大容量，也不会丢弃任何日志（除非队列完全满了） -->
        <!-- 从源代码 AsyncAppenderBase.class 中可知，默认值在-1时会丢弃日志 -->
        <discardingThreshold>0</discardingThreshold>
        <!-- 默认队列大小: 256 -->
        <queueSize>1024</queueSize>
        <!-- 当队列满时丢弃日志，虽一定程序上可提升性能，但会丢失部分日志，个人不建议开启 -->
        <!-- <neverBlock>true</neverBlock> -->
    </appender>
</springProfile>

<!-- 日志节点 -->
<root level="INFO">
    <appender-ref ref="CONSOLE" />
    <appender-ref ref="FILE" />
    <!-- 只有生产、压测环境开启异步日志 -->
    <springProfile name="dev,prod">
        <appender-ref ref="ASYNC-FILE" />
    </springProfile>
</root>
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

### 3. Runner

**定义**

| 特性                 | `CommandLineRunner`                                   | `ApplicationRunner`                                   |
|----------------------|-------------------------------------------------------|-----------------------------------------------------|
| **接口定义**         | `public interface CommandLineRunner { void run(String... args); }` | `public interface ApplicationRunner { void run(ApplicationArguments args); }` |
| **参数类型**         | 接受 `String... args` 参数（数组形式）。              | 接受 `ApplicationArguments` 对象（封装形式）。      |
| **引入版本**         | Spring 4.0。                                           | Spring Boot 1.3。                                   |

**区别**

| 特性                   | `CommandLineRunner`                          | `ApplicationRunner`                          |
|------------------------|----------------------------------------------|---------------------------------------------|
| **参数类型**           | 原始的字符串数组，简单但灵活性有限。         | 封装的 `ApplicationArguments`，更方便解析。 |
| **参数解析**           | 手动解析（例如拆分 `--key=value` 格式参数）。 | 提供解析方法，直接获取选项和非选项参数。    |
| **适用场景**           | 简单的命令行任务。                          | 更复杂的参数处理任务。                      |

**ApplicationRunner高级用法**

```yaml
# 启动参数模拟值, 重复的选项参数对应的值会被汇总
args: arg1,arg2,--port=8080,--port=9091,--active=true
```

```java
@Value("${args}")
private String[] args;  // 模拟系统参数列表

@Override
public void run(ApplicationArguments args) throws Exception {
    log.info("成功启动系统AppRunner: {}", appConfigProperties.getName());
    // 此处为模拟启动参数
    // 在 Lambda 表达式中，无法直接使用非 final 类型的局部变量（如 args），这是因为 Lambda 捕获外部变量时要求它们是 final 或者等效于 final
    final ApplicationArguments appArgs = new DefaultApplicationArguments(this.args);
    log.info("系统启动AppRunner参数列表为: {}", Arrays.toString(appArgs.getSourceArgs()));
    // 遍历选项参数
    log.info("系统启动的非选项参数列表为: {}", appArgs.getNonOptionArgs());
    log.info("系统启动的选项参数列表为: {}", appArgs.getOptionNames());
    appArgs.getOptionNames().forEach(key -> log.info("选项参数: {}, 值: {}", key, appArgs.getOptionValues(key)));
}
```

### 4. Banner

**`Banner 相关配置`**

```yml
# application.yml 配置文件

spring:
   main:
      # off 关闭
      banner-mode: console

# 通过 banner 输出相关信息
application:
   title: SpringBootTutorial
   group: TOE
   author: emirio
   version: 1.0.2
   formatted-version: v1.0.2
```

### 5. application 配置文件

**applicatioin配置文件命名方式:**

* **`application.properties`**
* **`application.yml`**
* **`application.yaml`**

```txt
配置文件在项目中的位置如下:

./
├── config
│   ├── application1.yml
│   └── child
│       └── application1.yml
├── application.yml
├── configuration
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── emirio
│   │   │   │           └── configuration
│   │   │   │               ├── ConfigurationApplication.java
│   │   │   └── resources
│   │   │       ├── application.yml
│   │   │       ├── banner.txt
│   │   │       ├── config
│   │   │       │   └── application.yml
│   │   │       ├── environment.properties
│   │   │       ├── logback-spring.xml
│   │   │       └── logback.xml
```

> **加载顺序依次从上到下，所有文件都会加载，高优先级的内容会覆盖低优先级的内容**

|位置|优先级 (数字越小优先级越高)|
|--|:--:|
|./config/application.yml|1|
|./config/child/application.yml|2|
|./application.yml|3|
|./configuration/src/main/resources/config/application.yml|4|
|./configuration/src/main/resources/application.yml|5|