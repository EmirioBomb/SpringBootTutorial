package com.emirio.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
public class ConfigurationApplication {
    @Value("${app.name}")
    public static String _appName;

    @Value("${app.name}")
    public String appName;

    @Value("${app.version}")
    public String version;

    public static void main(String[] args) {
        // 静态字段在类加载时就初始化，而 Spring 容器可能尚未完全启动，导致无法提供配置值。
        log.warn("正在准备启动: {}", _appName);
        SpringApplication.run(ConfigurationApplication.class, args);
        // SpringBoot启动后的日志输出
        log.warn("成功启动系统: {}", _appName);
    }

    @PostConstruct
    public void initInfo() {
        log.info("正在初始化静态系统名称: {}", _appName);
        log.info("正在初始化非静态系统名称: {}", appName);
        log.info("正在初始化系统版本为: {}", version);
        _appName = appName;
    }

}
