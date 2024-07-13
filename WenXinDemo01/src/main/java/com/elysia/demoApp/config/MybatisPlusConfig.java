package com.elysia.demoApp.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus的配置类，设置mybatis-plus的相关配置
 * @author cxb
 * @ClassName MybatisPlusConfig
 * @date 2023/2/7 22:54
 */
@Configuration
@MapperScan("com.elysia.demoApp.mapper")
public class MybatisPlusConfig {
    /**
     * 配置mybatis-plus的相关配置（详见官方文档）
     *
     * @ClassName MybatisPlusConfig
     * @author cxb
     * @date 2023/2/7 22:56
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加乐观锁的配置
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }
}
