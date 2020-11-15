package com.application.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 创建自定义的SQL 注入器
 *
 * @author yanghaiyong
 * 2020/7/25   11:19
 */
@Component
public class MySQLInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        // 自定义的 SQL 注入
        methodList.add(new DeleteAllMethod());
        // 使用官方的SQL 注入(如果不是逻辑删除字段则进行批量插入,也就是说这里可以过滤想要的字段)
        // TODO 如果数据库有默认值,但是插入的实体字段值为null,则最终插入时是null
        methodList.add(new InsertBatchSomeColumn(predicate
                -> !predicate.isLogicDelete()));
        /*methodList.add(new InsertBatchSomeColumn(predicate
                -> !predicate.isLogicDelete()
                && !predicate.getColumn().equals("age")));// 同时排除2个字段*/
        /**
         * <li> 例1: t -> !t.isLogicDelete() , 表示不要逻辑删除字段 </li>
         * <li> 例2: t -> !t.getProperty().equals("version") , 表示不要字段名为 version 的字段 </li>
         * <li> 例3: t -> t.getFieldFill() != FieldFill.UPDATE) , 表示不要填充策略为 UPDATE 的字段 </li>
         *
         */
        return methodList;
    }
}
