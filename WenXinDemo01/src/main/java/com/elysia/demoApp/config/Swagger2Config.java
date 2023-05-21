package com.elysia.demoApp.config;

/**
 * @author cxb
 * @ClassName Swagger2Config
 * @date 30/3/2023 下午7:03
 */

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置信息
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket imagApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("imageApi")
                .apiInfo(imageApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/api/img/.*")))
                .build()
                // 指定host(本地测试请注掉)
//                .host("139.9.208.233")
                ;

    }
//    @Bean
//    public Docket userApiConfig(){
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("userApi")
//                .apiInfo(userApiInfo())
//                .select()
//                //只显示admin路径下的页面
//                .paths(Predicates.and(PathSelectors.regex("/user/.*")))
//                .build()
//                // 指定host(本地测试请注掉)
//                .host("139.9.208.233")
//                ;

//    }

    private ApiInfo imageApiInfo(){
        return new ApiInfoBuilder()
                .title("后台系统-AI图像API文档")
                .description("本文档描述了图像相关的接口定义")
                .version("1.0")
                .contact(new Contact("cxb", "http://159.75.137.185/home", "mlp52013140@163.com"))
                .build();
    }
//    private ApiInfo userApiInfo(){
//        return new ApiInfoBuilder()
//                .title("后台管理系统-用户相关操作API文档")
//                .description("本文档描述了后台与用户信息相关的接口定义")
//                .version("1.0")
//                .contact(new Contact("cxb", "http://www.elysia233.xyz", "mlp52013140@163.com"))
//                .build();
//    }


}
