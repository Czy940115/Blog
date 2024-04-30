package com.czy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: BlogAdminApplication
 * Package: com.czy
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("com.czy.mapper")
public class BlogAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogAdminApplication.class, args);
    }
}
