package com.accrualbatch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.application")
public class DataSourceSpannerProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}

