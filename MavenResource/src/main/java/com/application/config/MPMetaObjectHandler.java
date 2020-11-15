package com.application.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 * 审计功能
 *
 * @author yanghaiyong
 * 2020/7/24   23:51
 */
@Component
public class MPMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter("createdDate")) {
            setFieldValByName("createdDate", LocalDateTime.now(ZoneId.systemDefault()), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object lastModifiedDate = getFieldValByName("lastModifiedDate", metaObject);
        // 如果时间已设置值则不自动填充
        if (Objects.isNull(lastModifiedDate)) {
            setFieldValByName("lastModifiedDate", LocalDateTime.now(ZoneId.systemDefault()), metaObject);
        }
        // RequestContextHolder可以获取 request
    }
}
