package com.cmxz.snakesmartmemo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * 启动说明：
 *
 * 在Run->EdidConfiguations...->Modify options-> Add VM options里添加如下命令：
 * --add-opens java.base/java.lang=ALL-UNNAMED
 * --add-opens java.base/java.util=ALL-UNNAMED
 * --add-opens java.base/java.nio=ALL-UNNAMED
 * --add-opens java.base/sun.nio.ch=ALL-UNNAMED
 * 从而解决java.lang.reflect.InaccessibleObjectException
 *
 * !!! 还有记得添加application.yml,并将其加入.gitignore !!!
 * 从而解决 Cannot determine embedded database driver class for database type NONE，
 * 因为springboot启动时会自动注入数据源和配置jpa，需要设置参数
 */

@SpringBootApplication(exclude={ HibernateJpaAutoConfiguration.class})
public class Starter {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}
