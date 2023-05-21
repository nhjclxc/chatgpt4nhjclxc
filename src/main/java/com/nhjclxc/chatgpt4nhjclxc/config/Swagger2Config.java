package com.nhjclxc.chatgpt4nhjclxc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

/**
 * Swagger2Config配置类，可以添加token请求头
 *
 * 在与spring boot集成时，放在与Application.java同级的目录下或子目录下
 * 通过@Configuration注解，让Spring来加载该类配置。
 * 再通过@EnableSwagger2注解来启用Swagger2。
 *
 * @author LuoXianchao
 * @since 2023/5/7 16:25
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
public class Swagger2Config implements WebMvcConfigurer {

    private static final String AUTHORIZATION = "AUTHORIZATION";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("swagger生成的本接口文档的说明")
                .description("本接口文档的描述")
                .termsOfServiceUrl("http://localhost:8080/swagger-ui.html")
                .contact(new Contact("所属组织","http://localhost:8080/swagger-ui.html","nhjclxc@163.com"))
                .version("1.0")
                .build();
    }

    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 扫描controller层所在的位置
                .apis(RequestHandlerSelectors.basePackage("com.nhjclxc.chatgpt4nhjclxc.web"))
                .paths(PathSelectors.any())
                .build()
                // 提供token输入框
                .securitySchemes(securitySchemes())
                // 获取token输入框的数据
                .securityContexts(securityContexts())
                ;
    }

    /**
     * 提供token输入框，
     */
    private List<SecurityScheme> securitySchemes(){
        //HttpServletRequest 在request.getHeader("token");里面获取token数据
        // ApiKey的第一个参数Authorization必须与defaultAuth()方法里面的new SecurityReference()的第一个参数匹配才能获取到数据
        // ApiKey的第二个参数token，就是你项目的jwt验证中header里面的key，根据实际情况修改
        // ApiKey的第三个参数header，表示将这个参数放在哪里
        return Collections.singletonList(new ApiKey(AUTHORIZATION, "token", "header"));
    }

    /**
     * 获取验证数据
     */
    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .build()
        );
    }

    /**
     * 关联数据，设置生效范围
     */
    private List<SecurityReference> defaultAuth() {
        // global表示对全局所有请求生效
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        return Collections.singletonList(new SecurityReference(AUTHORIZATION, new AuthorizationScope[]{authorizationScope}));
    }

}