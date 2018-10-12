package com.cwn.wizbank;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * Mybatis相关代码生成器
 * @author Andrew.xiao 2018/5/7
 */
public class CodeGenerator {

    public String packageName = "com.cwn.wizbank.report";
    public boolean serviceNameStartWithI = false;
    public  String dbUrl = "jdbc:postgresql://192.168.3.222:5432/jack";
    public  String dbUserName = "postgres";
    public  String dbPassword = "12345678";
    public  String dbDriver = "org.postgresql.Driver";
    public  String controllerPackage = "web.controller";
    public  String entityPackage = "entity";
    public  String codeUser = "TODO author";
    public  String codeOutputDir = "d:\\codeGen";
    public  String serviceName = "%sService";
    //修改替换成你需要的表名，多个表名传数组
    public  String[] tableNames = new String[]{"persotn"};


    public static void main(String[] args) {
        new CodeGenerator().generateCode();
    }

    private void generateCode() {
        new AutoGenerator()
                .setGlobalConfig(globalConfig())
                .setDataSource(dataSourceConfig())
                .setStrategy(strategyConfig())
                .setPackageInfo(packageConfig())
                .execute();
    }

    public GlobalConfig globalConfig(){
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(false)
                .setAuthor(codeUser)
                .setOutputDir(codeOutputDir)
                .setFileOverride(true);
        if (!serviceNameStartWithI) {
            config.setServiceName(serviceName);
        }
        return config;
    }

    public DataSourceConfig dataSourceConfig(){
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.POSTGRE_SQL)
                .setUrl(dbUrl)
                .setUsername(dbUserName)
                .setPassword(dbPassword)
                .setDriverName(dbDriver);
        return dataSourceConfig;
    }

    public StrategyConfig strategyConfig(){
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(false)
                .setDbColumnUnderline(true)
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames);
        return strategyConfig;
    }

    public PackageConfig packageConfig() {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(packageName);
        packageConfig.setController(controllerPackage);
        packageConfig.setEntity(entityPackage);
        return packageConfig;
    }
}
