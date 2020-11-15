package com.application.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/7/25   11:14
 */
public class DeleteAllMethod extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // 执行的sql
        String sql = "delete from " + tableInfo.getTableName();
        // mapper 接口的方法名
        String method = "deleteAll";
        // 将SQL 注入语言中
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        // 将SQL 语言注入 语句中
        return addDeleteMappedStatement(mapperClass, method, sqlSource);
    }
}
