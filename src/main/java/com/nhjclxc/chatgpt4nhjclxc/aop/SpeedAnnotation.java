package com.nhjclxc.chatgpt4nhjclxc.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 测试某个方法花费多少时间的注解，你的方法上面加上这个注解的话就会李彤AOP技术技术出这个方法执行的时间。
 *
 * @author LuoXianchao
 * @since 2023/5/21 10:20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpeedAnnotation {
    String value() default "";
}