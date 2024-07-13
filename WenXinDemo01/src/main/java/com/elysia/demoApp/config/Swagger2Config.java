package com.elysia.demoApp.config;

/*
  @author cxb
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
 * @author cxb
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket imageClassifyApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("imageClassifyApi")
                .apiInfo(imageClassifyApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/api/img/image_classify/.*")))
                .build()
                // 指定host(本地测试请注掉)
//                .host("139.9.208.233")
                ;
    }

    @Bean
    public Docket aiPaintingApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("aiPaintingApi")
                .apiInfo(aiPaintingApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/api/ai_painting/.*")))
                .build();

    }

    @Bean
    public Docket ocrApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("ocrApi")
                .apiInfo(ocrApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/api/img/ocr/.*")))
                .build();

    }

    @Bean
    public Docket imageProcessApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("imageProcessApi")
                .apiInfo(imageProcessApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/api/img/image_process/.*")))
                .build();

    }

    @Bean
    public Docket textApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("textApi")
                .apiInfo(textApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/api/text/.*")))
                .build();

    }

    @Bean
    public Docket accountApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("accountApi")
                .apiInfo(accountApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/user/auth/.*")))
                .build();

    }

    @Bean
    public Docket userInfoApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("userInfoApi")
                .apiInfo(userInfoApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/user/info/.*")))
                .build();

    }

    @Bean
    public Docket modelApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("modelApi")
                .apiInfo(modelApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/api/model/.*")))
                .build();

    }
    @Bean
    public Docket articleApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("articleApi")
                .apiInfo(articleApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/api/article/.*")))
                .build();

    }

    @Bean
    public Docket galleryApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("galleryApi")
                .apiInfo(galleryApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/api/gallery/.*")))
                .build();

    }

    @Bean
    public Docket wenxinApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("wenxinApi")
                .apiInfo(wenxinApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/api/wenxin/.*")))
                .build();

    }



    private ApiInfo imageClassifyApiInfo(){
        return new ApiInfoBuilder()
                .title("后台管理系统-图像识别API文档")
                .description("本文档描述了图像识别相关的接口定义")
                .version("1.0")
                .contact(new Contact("cxb", "http://159.75.137.185/home", "mlp52013140@163.com"))
                .build();
    }

    private ApiInfo aiPaintingApiInfo(){
        return new ApiInfoBuilder()
                .title("后台管理系统-AI作画相关的API文档")
                .description("本文档描述了AI作画相关的接口定义")
                .version("1.0")
                .contact(new Contact("cxb", "http://159.75.137.185/home", "mlp52013140@163.com"))
                .build();
    }

    private ApiInfo ocrApiInfo(){
        return new ApiInfoBuilder()
                .title("后台管理系统-图像OCR相关的API文档")
                .description("本文档描述了图像OCR相关的接口定义")
                .version("1.0")
                .contact(new Contact("cxb", "http://159.75.137.185/home", "mlp52013140@163.com"))
                .build();
    }

    private ApiInfo imageProcessApiInfo(){
        return new ApiInfoBuilder()
                .title("后台管理系统-图像特效与增强相关的API文档")
                .description("本文档描述了图像特效与增强相关的接口定义")
                .version("1.0")
                .contact(new Contact("cxb", "http://159.75.137.185/home", "mlp52013140@163.com"))
                .build();
    }

    private ApiInfo accountApiInfo(){
        return new ApiInfoBuilder()
                .title("后台管理系统-用户登录相关操作API文档")
                .description("本文档描述了后台与用户登录相关的接口定义")
                .version("1.0")
                .contact(new Contact("cxb", "http://159.75.137.185/home", "mlp52013140@163.com"))
                .build();
    }

    private ApiInfo userInfoApiInfo(){
        return new ApiInfoBuilder()
                .title("后台管理系统-用户信息相关操作API文档")
                .description("本文档描述了后台与用户信息相关的接口定义，包含用户修改和获取个人信息，以及获取用户创作和OCR相关历史")
                .version("1.0")
                .contact(new Contact("cxb", "http://159.75.137.185/home", "mlp52013140@163.com"))
                .build();
    }

    private ApiInfo textApiInfo(){
        return new ApiInfoBuilder()
                .title("后台管理系统-文本相关操作API文档")
                .description("本文档描述了后台与文本资源相关的接口定义，包含用户可调用的文本相关的接口资源")
                .version("1.0")
                .contact(new Contact("cxb", "http://159.75.137.185/home", "mlp52013140@163.com"))
                .build();
    }

    private ApiInfo modelApiInfo(){
        return new ApiInfoBuilder()
                .title("后台管理系统-模型相关操作API文档")
                .description("本文档描述了模型评估和调试相关的接口资源")
                .version("1.0")
                .contact(new Contact("cxb", "http://159.75.137.185/home", "mlp52013140@163.com"))
                .build();
    }

    private ApiInfo articleApiInfo(){
        return new ApiInfoBuilder()
                .title("后台管理系统-文章相关操作API文档")
                .description("本文档描述了趣闻等推文的接口资源")
                .version("1.0")
                .contact(new Contact("cxb", "http://159.75.137.185/home", "mlp52013140@163.com"))
                .build();
    }

    private ApiInfo galleryApiInfo(){
        return new ApiInfoBuilder()
                .title("后台管理系统-AI画廊的相关操作API文档")
                .description("本文档描述了用户分享的AI画廊等推文的接口资源")
                .version("1.0")
                .contact(new Contact("cxb", "http://159.75.137.185/home", "mlp52013140@163.com"))
                .build();
    }

    private ApiInfo wenxinApiInfo(){
        return new ApiInfoBuilder()
                .title("后台管理系统-文心一言的相关操作API文档")
                .description("本文档描述了文心一言的接口资源")
                .version("1.0")
                .contact(new Contact("cxb", "http://159.75.137.185/home", "mlp52013140@163.com"))
                .build();
    }
}
