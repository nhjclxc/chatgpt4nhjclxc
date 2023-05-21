// package com.nhjclxc.chatgpt4nhjclxc.interceptor;
//
// import lombok.extern.slf4j.Slf4j;
// import org.apache.ibatis.executor.Executor;
// import org.apache.ibatis.mapping.BoundSql;
// import org.apache.ibatis.mapping.MappedStatement;
// import org.apache.ibatis.mapping.ParameterMapping;
// import org.apache.ibatis.plugin.*;
// import org.apache.ibatis.reflection.MetaObject;
// import org.apache.ibatis.session.Configuration;
// import org.apache.ibatis.session.ResultHandler;
// import org.apache.ibatis.session.RowBounds;
// import org.apache.ibatis.type.TypeHandlerRegistry;
// import org.springframework.stereotype.Component;
//
// import java.util.Properties;
// import java.util.concurrent.ExecutorService;
//
// /**
//  * MybatisInterceptor 配置
//  *
//  * @author LuoXianchao
//  * @since 2023/5/21 9:50
//  */
// @Intercepts({
//         @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
//         @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
// })
// @Slf4j
// @Component
// public class MybatisInterceptor implements Interceptor {
//
//     private SqlMessageService sqlMessageService;
//
//     private final ExecutorService pool;
//
//     public MybatisInterceptor() {
//         CustomThreadPoolExecutor exec = new CustomThreadPoolExecutor();
//         exec.init();
//         pool = exec.getCustomThreadPoolExecutor();
//     }
//
//     @Override
//     public Object intercept(Invocation invocation) throws Throwable {
//         MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
//         Object parameter = null;
//         if (invocation.getArgs().length > 1) {
//             parameter = invocation.getArgs()[1];
//         }
//
//         BoundSql boundSql = mappedStatement.getBoundSql(parameter);
//         Configuration configuration = mappedStatement.getConfiguration();
//
//         long startTime = System.currentTimeMillis();
//         Object returnVal = invocation.proceed();
//         long costTime = System.currentTimeMillis() - startTime;
//
//         // 排除自身
//         if (mappedStatement.getId().contains(ApplicationConst.SQL_DAO_NAME)) {
//             return returnVal;
//         }
//         try {
//             // 拦截到所有sql请求做的一些事情
//
//
//         } catch (Exception e) {
//             log.error(e.getMessage());
//         }
//
//         return returnVal;
//     }
//
//
//     @Override
//     public Object plugin(Object target) {
//         return Plugin.wrap(target, this);
//     }
//
//     @Override
//     public void setProperties(Properties arg0) {
//     }
//
// }
