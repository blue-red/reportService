package com.cwn.wizbank.report.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Liquibase Bean
 * @author bill.lai 2018-05-02.
 */
@Configuration
public class LiquibaseConfig{

    @Bean
    public SpringLiquibase liquibase(@Qualifier("reportDataSource") DataSource dataSource){
        SpringLiquibase liquibase = new SpringLiquibase();
        //设置数据源
        liquibase.setDataSource(dataSource);
        //数据库更新语句的总配置文件地址
        liquibase.setChangeLog("classpath:config/liquibase.xml");
        //上下文
        liquibase.setContexts("development,test,production");
        liquibase.setShouldRun(false);
        return liquibase;
    }

}
