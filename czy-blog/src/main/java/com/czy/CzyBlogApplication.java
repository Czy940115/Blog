package com.czy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * ClassName: CzyBlogApplication
 * Package: com.czy
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("com.czy.mapper")
@EnableScheduling//@EnableScheduling是spring提供的定时任务的注解
@EnableSwagger2 // 开启swagger文档
public class CzyBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(CzyBlogApplication.class, args);
    }
}
