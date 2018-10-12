package com.cwn.wizbank.report.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * quartz配置
 * @author Andrew.xiao 2018/5/16
 */
@Configuration
public class QuartzConfig {

    @Bean
    public QuartzJobFactory quartzJobFactory(){
        return new QuartzJobFactory();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("quartzJobFactory") QuartzJobFactory quartzJobFactory){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(quartzJobFactory);
        return schedulerFactoryBean;
    }
}
