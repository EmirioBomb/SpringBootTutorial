package com.emirio.configuration;

import com.emirio.configuration.domain.config.AppConfig;
import com.emirio.configuration.domain.properties.AppConfigProperties;
import com.emirio.configuration.domain.properties.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
@PropertySource("classpath:environment.properties")
public class ConfigurationApplication {

    // @Value 不支持静态字段注入，但不会阻止程序执行，会有对应提醒
    @Value("${app.name}")
    private static String appName;

    // 根据 properties 文件中获取配置信息
    @Value("${jdk.version}")
    private String jdkVersion;

    private final AppConfig appConfig;
    private final AppProperties appProperties;
    private final AppConfigProperties appConfigProperties;

    public ConfigurationApplication(AppConfig appConfig, AppProperties appProperties, AppConfigProperties appConfigProperties) {
        this.appConfig = appConfig;
        this.appProperties = appProperties;
        this.appConfigProperties = appConfigProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigurationApplication.class, args);
    }

    @PostConstruct
    public void logAppName() {
        // 静态字段在类加载时就初始化，而 Spring 容器可能尚未完全启动，导致无法提供配置值。
        log.info("正在初始化静态系统名称: {}", appName);
        log.info("JDK版本: {}", jdkVersion);
        logAppInfo();
        logAppProperties();
        logAppConfigProperties();
    }

    public void logAppInfo() {
        log.info("系统名称: {}", appConfig.getAppName());
        log.info("系统版本: {}", appConfig.getAppVersion());
        log.info("系统作者: {}", appConfig.getAuthor());
    }

    public void logAppProperties() {
        log.info("系统配置名称: {}", appProperties.getAppName());
        log.info("系统配置版本: {}", appProperties.getAppVersion());
        log.info("系统配置作者: {}", appProperties.getAppAuthor());
    }

    public void logAppConfigProperties() {
        log.info("系统配置类名称: {}", appConfigProperties.getName());
        log.info("系统配置类版本: {}", appConfigProperties.getVersion());
        log.info("系统配置类作者: {}", appConfigProperties.getAuthor());
    }

}
