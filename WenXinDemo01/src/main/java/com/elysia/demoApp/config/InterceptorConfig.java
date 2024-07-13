package com.elysia.demoApp.config;

import com.elysia.demoApp.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 拦截器的相关配置
 * @author cxb
 * @ClassName InterceptorConfig
 * @date 23/5/2023 下午3:38
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource
    private JwtInterceptor jwtInterceptor;

    @Resource
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                // 放行相关资源
                .excludePathPatterns(ignoreUrlsConfig.getUrls());
    }
}
