package com.cwn.wizbank.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 应用入口启动类
 * @author Andrew.xiao 2018/4/24
 */
@SpringBootApplication
@EnableTransactionManagement
public class ReportApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReportApplication.class,args);
    }
}
