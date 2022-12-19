package com.cmxz.snakesmartmemo.util;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
/**
 *       将自定义的Druid数据源添加到容器中，不再让Spring Boot 自动创建
 *       这样做的目的是： 绑定全局配置文件中的druid 数据源属性注入到 com.alibaba.druid.pool.DruidDataSource
 *       从而让它们生效
 *
 *       @ConfigurationProperties(prefix = "spring.datasource"):
 *         作用就是将 全局配置文件中前缀为Spring.dataSource的属性值注入到com.alibaba.druid.pool.DruidDataSource的同名参数中
 */

@Configuration
public class DruidConfig {
    /**
     * 将所有前缀为spring.datasource下的配置项都加载DataSource中
     */

//@ConfigurationProperties("spring.datasource"),这样注入，无法将druid配置文件下的连接池配置自动注入，所以我加一个.druid，这样就是将spring.datasource.druid的属性值注入到com.alibaba.druid.pool.DruidDataSource的同名参数中，而不是spring.datasource
    @ConfigurationProperties("spring.datasource.druid")
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }
}

