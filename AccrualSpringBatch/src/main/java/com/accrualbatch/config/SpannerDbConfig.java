package com.accrualbatch.config;

import javax.sql.DataSource;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SpannerDbConfig {
	
	private final DataSourceSpannerProperties properties;
	
    public SpannerDbConfig(DataSourceSpannerProperties properties) {
        this.properties = properties;
    }

	 @Bean(name = "applicationDataSource")
    public DataSource applicationDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setUrl(properties.getUrl());
        return dataSource;
    }
	 
	    @Bean
	    @Qualifier("applicationTransactionManager")
	    public PlatformTransactionManager transactionManager() {
	        return new DataSourceTransactionManager(applicationDataSource());
	    }
}
