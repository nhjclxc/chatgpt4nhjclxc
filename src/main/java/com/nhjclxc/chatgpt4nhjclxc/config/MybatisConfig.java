// package com.nhjclxc.chatgpt4nhjclxc.config;
//
// import com.nhjclxc.chatgpt4nhjclxc.interceptor.MybatisInterceptor;
// import org.apache.ibatis.plugin.Interceptor;
// import org.mybatis.spring.SqlSessionFactoryBean;
// import org.mybatis.spring.annotation.MapperScan;
// import org.mybatis.spring.annotation.MapperScans;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.context.EnvironmentAware;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.EnableAspectJAutoProxy;
// import org.springframework.context.annotation.Primary;
// import org.springframework.core.env.Environment;
// import org.springframework.core.io.Resource;
// import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
// import org.springframework.transaction.PlatformTransactionManager;
// import org.springframework.transaction.annotation.EnableTransactionManagement;
//
// import javax.sql.DataSource;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collection;
// import java.util.Properties;
//
// /**
//  * MybatisConfig配置
//  *
//  * @author LuoXianchao
//  * @since 2023/5/21 9:50
//  */
// @Configuration
// @EnableTransactionManagement(proxyTargetClass = true)
// @EnableAspectJAutoProxy(exposeProxy = true)
// @MapperScan(basePackages = {
//         "com.nhjclxc.chatgpt4nhjclxc.dao"
// })
// public class MybatisConfig implements EnvironmentAware {
//
//     private Environment environment;
//
//     @Override
//     public void setEnvironment(Environment environment) {
//         this.environment = environment;
//     }
//
//     @Bean("mybatisProDataSource")
//     @Primary
//     public DataSource mybatisProDataSource() {
//         HikariDataSource hikariDataSource = new HikariDataSource();
//         hikariDataSource.setDriverClassName(environment.getProperty("datasource.driver-class-name"));
//         hikariDataSource.setJdbcUrl(environment.getProperty("product.datasource.jdbc-url"));
//         hikariDataSource.setUsername(environment.getProperty("product.datasource.username"));
//         hikariDataSource.setPassword(environment.getProperty("product.datasource.password"));
//         hikariDataSource.setMaximumPoolSize(100);
//         hikariDataSource.setIdleTimeout(30000);
//         hikariDataSource.setConnectionTestQuery("select 1");
//         hikariDataSource.setPoolName("proHikariCP");
//         hikariDataSource.setAutoCommit(false);
//         return hikariDataSource;
//     }
//
//     @Primary
//     public PlatformTransactionManager transactionProManager(@Qualifier("mybatisProDataSource") DataSource dataSource) {
//         DataSourceTransactionManager transactionManagerBean = new DataSourceTransactionManager();
//         transactionManagerBean.setDataSource(dataSource);
//         return transactionManagerBean;
//     }
//
//     @Bean("proSqlSessionFactory")
//     @Primary
//     public SqlSessionFactoryBean proSqlSessionFactory(@Qualifier("mybatisProDataSource") DataSource dataSource) throws IOException {
//         SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//         sessionFactoryBean.setDataSource(dataSource);
//         org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//         configuration.setCallSettersOnNulls(true);
//         configuration.setMapUnderscoreToCamelCase(true);
//         sessionFactoryBean.setConfiguration(configuration);
//         PageInterceptor pageInterceptor = new PageInterceptor();
//         Properties pageprop = new Properties();
//         pageprop.setProperty("helperDialect", "mysql");
//         pageprop.setProperty("reasonable", "true");
//         pageInterceptor.setProperties(pageprop);
//         Interceptor[] interceptors = new Interceptor[]{pageInterceptor, new MybatisInterceptor()};
//         sessionFactoryBean.setPlugins(interceptors);
//         return sessionFactoryBean;
//     }
//
//     @Bean("mybatisCenDataSource")
//     public DataSource mybatisCenDataSource() {
//         HikariDataSource hikariDataSource = new HikariDataSource();
//         hikariDataSource.setDriverClassName(environment.getProperty("datasource.driver-class-name"));
//         hikariDataSource.setJdbcUrl(environment.getProperty("center.datasource.jdbc-url"));
//         hikariDataSource.setUsername(environment.getProperty("center.datasource.username"));
//         hikariDataSource.setPassword(environment.getProperty("center.datasource.password"));
//         hikariDataSource.setMaximumPoolSize(20);
//         hikariDataSource.setIdleTimeout(30000);
//         hikariDataSource.setConnectionTestQuery("select 1");
//         hikariDataSource.setPoolName("cenHikariCP");
//         hikariDataSource.setAutoCommit(false);
//         return hikariDataSource;
//     }
//
//     public PlatformTransactionManager transactionCenManager(@Qualifier("mybatisCenDataSource") DataSource dataSource) {
//         DataSourceTransactionManager transactionManagerBean = new DataSourceTransactionManager();
//         transactionManagerBean.setDataSource(dataSource);
//         return transactionManagerBean;
//     }
//
//     @Bean("cenSqlSessionFactory")
//     public SqlSessionFactoryBean cenSqlSessionFactory(@Qualifier("mybatisCenDataSource") DataSource dataSource) {
//         SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//         sessionFactoryBean.setDataSource(dataSource);
//         org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//         configuration.setCallSettersOnNulls(true);
//         configuration.setMapUnderscoreToCamelCase(true);
//         sessionFactoryBean.setConfiguration(configuration);
//         PageInterceptor pageInterceptor = new PageInterceptor();
//         Properties pageprop = new Properties();
//         pageprop.setProperty("helperDialect", "mysql");
//         pageprop.setProperty("reasonable", "true");
//         pageInterceptor.setProperties(pageprop);
//         Interceptor[] interceptors = new Interceptor[]{pageInterceptor, new MybatisInterceptor()};
//         sessionFactoryBean.setPlugins(interceptors);
//         return sessionFactoryBean;
//     }
//
//     private Collection<? extends Resource> getResources(String location) {
//         try {
//             PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//             return Arrays.asList(resolver.getResources(location));
//         } catch (Exception e) {
//             return new ArrayList<>();
//         }
//     }
//
// }
