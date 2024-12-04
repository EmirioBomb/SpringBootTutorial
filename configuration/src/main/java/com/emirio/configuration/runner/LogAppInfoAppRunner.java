package com.emirio.configuration.runner;

import com.emirio.configuration.domain.properties.AppConfigProperties;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Setter
public class LogAppInfoAppRunner implements ApplicationRunner {
    private AppConfigProperties appConfigProperties;

    public LogAppInfoAppRunner(AppConfigProperties appConfigProperties) {
        this.appConfigProperties = appConfigProperties;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("成功启动系统AppRunner: {}", appConfigProperties.getName());
    }
}
