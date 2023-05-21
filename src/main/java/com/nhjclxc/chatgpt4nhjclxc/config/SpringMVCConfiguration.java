// package com.nhjclxc.chatgpt4nhjclxc.config;
//
// import org.springframework.context.annotation.Configuration;
// import org.springframework.format.FormatterRegistry;
// import org.springframework.stereotype.Component;
// import org.springframework.web.servlet.config.annotation.*;
//
// /**
//  * 系统适配器
//  *
//  * @author LuoXianchao
//  * @since 2023/5/21 10:35
//  */
// @Component
// @EnableWebMvc
// @Configuration
// public class SpringMVCConfiguration implements WebMvcConfigurer {
//
//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//                 .allowedOriginPatterns("*")
//                 .allowedHeaders("*")
//                 .allowCredentials(true)
//                 .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE");
//     }
//
//     /**
//      * 设置静态资源映射
//      */
//     @Override
//     public void addResourceHandlers(ResourceHandlerRegistry registry) {
//         // System.out.println("开始进行静态资源映射...");
//         // registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
//         // registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
//         // registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//         // registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//         // registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//     }
//
//     @Override
//     public void addFormatters(FormatterRegistry registry) {
//         // registry.addConverter(new LocalDateTimeConverter());
//     }
//
// }
