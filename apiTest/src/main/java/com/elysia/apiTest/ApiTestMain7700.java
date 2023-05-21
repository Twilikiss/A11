package com.elysia.apiTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * 测试模块的主启动类
 * @ClassName ApiTestMain7700
 * @author   cxb
 * @date  17/5/2023 上午11:01
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.elysia"})
public class ApiTestMain7700 {
    public static void main(String[] args) {
        SpringApplication.run(ApiTestMain7700.class, args);
    }
}
