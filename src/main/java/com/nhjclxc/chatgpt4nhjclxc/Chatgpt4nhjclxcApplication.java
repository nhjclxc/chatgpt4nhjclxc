package com.nhjclxc.chatgpt4nhjclxc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//开启注解开发AOP功能
@EnableAspectJAutoProxy
// @SpringBootApplication(scanBasePackages = {"com.nhjclxc.chatgpt4nhjclxc"})
@SpringBootApplication
public class Chatgpt4nhjclxcApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chatgpt4nhjclxcApplication.class, args);
        System.out.println("App is started !!!");
    }

}
