package com.application.config;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/7/25   0:10
 */
@Configuration
public class MPConfiguration {
    public static ThreadLocal<String> myTableName = new ThreadLocal<>();


    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        // 开启乐观锁插件
        return new OptimisticLockerInterceptor();
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));

        // 通用的多租户配置
        List<ISqlParser> sqlParserList = new ArrayList<>();
        /*TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId(boolean select) {
                // select since: 3.3.2，参数 true 表示为 select 下的 where 条件,false 表示 insert/update/delete 下的条件
                // 只有 select 下才允许多参(ValueListExpression),否则只支持单参
                if (!select) {
                    return new LongValue(1);
                }
                ValueListExpression expression = new ValueListExpression();
                ExpressionList list = new ExpressionList(new LongValue(1), new LongValue(2));
                expression.setExpressionList(list);
                return expression;
            }

            @Override
            public String getTenantIdColumn() {
                return "manage_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 这里可以判断是否过滤表
            *//*
            if ("user".equals(tableName)) {
                return true;
            }*//*
                return false;
            }
        });

        sqlParserList.add(tenantSqlParser);*/

        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
            @Override
            public boolean doFilter(MetaObject metaObject) {
                // 返回false 表示所有方法都会使用多租户的条件
                // 返回true 表示过滤掉某类型的方法,不使用多租户的概念
                MappedStatement ms = SqlParserHelper.getMappedStatement(metaObject);
                if ("com.application.mapper.UserMapper.selectById".equals(ms.getId())) {
                    // 表示此方法不会使用的租户条件查询
                    return true;
                }
                return false;
            }
        });


        // 动态表名解析器----> 用于解决分库分表查询,一般在resource 层就可以确定插入数据库的表名,所以可以用本地线程进行管理
        DynamicTableNameParser parser = new DynamicTableNameParser();
        HashMap<String, ITableNameHandler> tableNameHandlerMap = new HashMap<>();
        tableNameHandlerMap.put("sys_user", new ITableNameHandler() {
            @Override
            public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
                // sql 为执行前的sql,和表名
                // 返回值是执行后的表名
                // 如果返回值为null 则不进行替换
                // 其次 TODO，影响该处的还有多租户的过滤也会影响到此,也就是说如果多租户的某个方法被过滤了,那么此处也不会进行替换
                return myTableName.get();
            }
        });
        parser.setTableNameHandlerMap(tableNameHandlerMap);

        sqlParserList.add(parser);

        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }
}
