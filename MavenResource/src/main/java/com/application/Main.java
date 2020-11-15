package com.application;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/7/18   16:34
 */
@SpringBootApplication
@MapperScan("com.application.mapper")
@EnableTransactionManagement
@EnableKnife4j
@EnableSwagger2
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
