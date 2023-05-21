package com.nhjclxc.chatgpt4nhjclxc.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 *  这个注解用于测试某个方法的速度（花费时间）
 * @author LuoXianchao
 * @since 2023/5/21 10:30
 */
@Component
@Aspect
public class SpeedAdvice {

    //定义切点  这里的连接的是一个注解
    //匹配对应的注解
    @Pointcut("@annotation(com.nhjclxc.chatgpt4nhjclxc.aop.SpeedAnnotation)")
    private void speedPC(){}

    //匹配切面里面执行切点的方法（即具体的增强方法）
    @Around("SpeedAdvice.speedPC()")
    public Object speedAround(ProceedingJoinPoint pjp) throws Throwable {
        //args就是你的这个注解SpeedAnnotation所使用在的方法上的入参
        Object[] args = pjp.getArgs();
        Signature signature = pjp.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        long start = System.currentTimeMillis();

        //继续去执行原来的方法
        Object proceed = pjp.proceed(args);

        //原方法执行完之后再一次跳回来
        System.out.println("AOP执行---> " + className + "." + methodName + "() 方法，耗时：" + (System.currentTimeMillis() - start) + " ms.");
        //将执行完的对象返回
        return proceed;
    }
}
