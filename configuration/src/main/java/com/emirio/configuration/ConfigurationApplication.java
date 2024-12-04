package com.emirio.configuration;

import com.emirio.configuration.domain.config.AppConfig;
import com.emirio.configuration.domain.properties.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
public class ConfigurationApplication {

    // @Value 不支持静态字段注入，但不会阻止程序执行，会有对应提醒
    @Value("${app.name}")
    private static String appName;

    private final AppConfig appConfig;
    private final AppProperties appProperties;

    public ConfigurationApplication(AppConfig appConfig, AppProperties appProperties) {
        this.appConfig = appConfig;
        this.appProperties = appProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigurationApplication.class, args);
        // 若未在logAppInfo方法中，手动赋值给 appName，该值一直为 null
        log.warn("成功启动系统: {}", appName);
    }

    @PostConstruct
    public void logAppName() {
        // 静态字段在类加载时就初始化，而 Spring 容器可能尚未完全启动，导致无法提供配置值。
        log.info("正在初始化静态系统名称: {}", appName);
        logAppInfo();
        logAppProperties();
    }

    public void logAppInfo() {
        log.info("系统名称: {}", appConfig.getAppName());
        log.info("系统版本: {}", appConfig.getAppVersion());
        log.info("系统作者: {}", appConfig.getAuthor());
        // 不推荐给静态变量赋值, 本次仅做测试
        appName = appConfig.getAppName();
    }

    public void logAppProperties() {
        log.info("系统配置名称: {}", appProperties.getAppName());
        log.info("系统配置版本: {}", appProperties.getAppVersion());
        log.info("系统配置作者: {}", appProperties.getAppAuthor());
        appName = appProperties.getAppName();
    }

}
