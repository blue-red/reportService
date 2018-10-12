package com.cwn.wizbank.report.config;

import com.cwn.wizbank.report.enums.DBTypeEnum;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库相关配置
 * @author Andrew.xiao 2018/5/7
 */
@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource(@Qualifier("reportDataSource") DataSource reportDataSource, @Qualifier("coreDataSource")DataSource coreDataSource){
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object,Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.core.getValue(),coreDataSource);
        targetDataSources.put(DBTypeEnum.report.getValue(),reportDataSource);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(reportDataSource);
        return dynamicDataSource;
    }

    @Bean
    public DataSource reportDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.password("12345678");
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.type(HikariDataSource.class);
        dataSourceBuilder.url("jdbc:postgresql://192.168.3.222:5432/wizbank_report");
        return dataSourceBuilder.build();
    }

    /*@Bean
    public DataSource coreDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.password("123456");
        dataSourceBuilder.username("root");
        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://127.0.0.1:3306/core?useSSL=false");
        dataSourceBuilder.type(HikariDataSource.class);
        return dataSourceBuilder.build();
    }*/

    @Bean
    public DataSource coreDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.password("wizbank");
        dataSourceBuilder.username("sa");
        dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceBuilder.url("jdbc:sqlserver://192.168.3.45:1433;DatabaseName=bgy");
        dataSourceBuilder.type(HikariDataSource.class);
        HikariDataSource dataSource = (HikariDataSource) dataSourceBuilder.build();
        dataSource.setMaximumPoolSize(45);
        return dataSource;
    }

}
