package com.nhjclxc.chatgpt4nhjclxc.config;

import com.nhjclxc.chatgpt4nhjclxc.interceptor.RequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 请求拦截器配置类
 *
 * @author LuoXianchao
 * @since 2023/5/21 9:50
 */
@Configuration
@AllArgsConstructor
@ComponentScan("com.nhjclxc.chatgpt4nhjclxc.web")
public class InterceptorConfig extends WebMvcConfigurationSupport {

    private final RequestInterceptor requestInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 把刚刚写的拦截器类对象传进去    //添加拦截器的路径
        registry.addInterceptor(requestInterceptor)
                .addPathPatterns("/**");                  //拦截所有请求
                 // .excludePathPatterns("/swagger-ui.html");//不拦截登录的请求
        super.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }
}
