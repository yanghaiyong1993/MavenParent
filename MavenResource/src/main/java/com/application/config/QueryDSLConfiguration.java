package com.application.config;

import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Persistence;
import javax.sql.DataSource;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/7/19   10:40
 */
@Configuration
public class QueryDSLConfiguration {

    @Bean
    public com.querydsl.sql.Configuration configuration() {
        return new com.querydsl.sql.Configuration(sqlTemplates());
    }

    @Bean
    public SQLQueryFactory sqlQueryFactory(DataSource dataSource) {
        return new SQLQueryFactory(configuration(), dataSource);
    }

    @Bean
    public SQLTemplates sqlTemplates() {
        return MySQLTemplates
                .builder()
                //.printSchema()
                .quote()
                .newLineToSingleSpace().build();
    }
}
