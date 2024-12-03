# SpringBootTutorial
SpringBoot tutorial project

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
    // 定义的日志对象
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
    root: info
    com.emirio.configuration: info
  file:
    name: logs/log.log
```
