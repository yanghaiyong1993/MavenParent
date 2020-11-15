package com.application.config.authorization;

import com.google.common.collect.Maps;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(value = "dynamicSecurityService")
public class DynamicSecurityServiceImpl implements DynamicSecurityService {
    @Override
    public Map<String, ConfigAttribute> loadDataSource() {
        // 加载已存入数据库中的资源路径
        Map<String, ConfigAttribute> attributeMap = Maps.newConcurrentMap();
        attributeMap.put("/login", new SecurityConfig("/login"));
        return attributeMap;
    }
}
