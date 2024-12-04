package com.emirio.configuration.runner;

import com.emirio.configuration.domain.properties.AppConfigProperties;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@Setter
public class LogAppInfoAppRunner implements ApplicationRunner {
    @Value("${args}")
    private String[] args;  // 模拟系统参数列表

    private AppConfigProperties appConfigProperties;

    public LogAppInfoAppRunner(AppConfigProperties appConfigProperties) {
        this.appConfigProperties = appConfigProperties;
    }

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
}
