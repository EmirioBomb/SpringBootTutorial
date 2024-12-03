package com.emirio.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ConfigurationApplication {

    public static void main(String[] args) {
        // SpringBoot未初始化前的日志输出
        log.warn("正在准备启动SpringBootTutorial程序...");
        SpringApplication.run(ConfigurationApplication.class, args);
        // SpringBoot启动后的日志输出
        log.warn("成功启动SpringBootTutorial程序...");
    }

}
