package com.elysia.demoApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @ClassName DemoAppMain7001
 * @author   cxb
 * @date  17/5/2023 下午8:38
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.elysia"})
@EnableConfigurationProperties
public class DemoAppMain7001 {
    public static void main(String[] args) {
        SpringApplication.run(DemoAppMain7001.class,args);
    }
}
