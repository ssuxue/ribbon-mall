package com.chase.ribbon;

import com.chase.ribbon.app.redis.RedisSyncApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@SpringBootApplication
public class RibbonAdminApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RibbonAdminApplication.class, args);
        // 利用canal实现数据同步
        RedisSyncApp redisSyncApp = context.getBean(RedisSyncApp.class);
        redisSyncApp.work();
    }

}
