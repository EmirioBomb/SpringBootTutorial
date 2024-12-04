package com.emirio.configuration.runner;

import com.emirio.configuration.domain.properties.AppConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * SpringBoot 启动后执行初始化操作
 */
@Slf4j
@Component
public class LogAppInfoCmdRunner implements CommandLineRunner {
    private final AppConfigProperties appConfigProperties;

    public LogAppInfoCmdRunner(AppConfigProperties appConfigProperties) {
        this.appConfigProperties = appConfigProperties;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("成功启动系统CmdRunner: {}", appConfigProperties.getName());
    }
}
