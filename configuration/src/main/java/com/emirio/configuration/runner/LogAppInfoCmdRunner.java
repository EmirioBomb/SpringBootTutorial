package com.emirio.configuration.runner;

import com.emirio.configuration.domain.properties.AppConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * SpringBoot 启动后执行初始化操作
 */
@Slf4j
@Component
public class LogAppInfoCmdRunner implements CommandLineRunner {
    // 初始化启动参数样例
    @Value("${args}")
    private String[] args;

    private final AppConfigProperties appConfigProperties;

    public LogAppInfoCmdRunner(AppConfigProperties appConfigProperties) {
        this.appConfigProperties = appConfigProperties;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("成功启动系统CmdRunner: {}", appConfigProperties.getName());
        log.info("系统启动CmdRunner参数为: {}", Arrays.toString(this.args));
    }
}
