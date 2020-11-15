package com.application.config.authorization;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于配置白名单资源路径
 *
 * @author YangHaiYong
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application.secure.ignored", ignoreInvalidFields = false, ignoreUnknownFields = false)
@Component
public class IgnoreUrlsConfig {
    private List<String> urls = new ArrayList<>(10);
}
