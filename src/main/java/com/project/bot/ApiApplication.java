package com.project.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class ApiApplication {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
        log.info("swagger json url:{}", "http://localhost:8250/v3/api-docs");
        log.info("swagger doc.html url:{}", "http://localhost:8250/doc.html");
    }

    public static void shutdown() {
        if (context != null) {
            context.close(); // 关闭应用上下文
        }
    }

}
