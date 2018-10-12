package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.entity.ReportTemplate;
import com.cwn.wizbank.report.enums.CycleTypeEnum;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 定时器管理类
 * @author jack.wang 2018-04-26
 **/
@Service
public class QuartzManagement {

    private static final String JOB_GROUP_NAME = "reportExportJob";
    private static final String WEEK = "0 0 1 ? * MON";//每周一凌晨1点执行一次
    private static final String MONTH = "0 0 1 1 * ?";//每个月1号凌晨1点执行
    private static final String QUARTER = "0 0 1 1 1/3 ?";//每个季度的1号执行一次
    private static final String YEAR = "0 0 1 1 1 ? *";//每年的1号执行一次

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    private Scheduler scheduler;

    @PostConstruct
    public void initScheduler(){
        scheduler = schedulerFactoryBean.getScheduler();
    }

    /***
     * 初始化
     */
    public void init(){
        //TODO

    }

    /***
     * 添加定时任务
     * @param targetClass 具体业务类
     * @param reportTemplate 报表模板
     */
    public void addJob(Class targetClass, ReportTemplate reportTemplate) {
        JobDetail jobDetail = JobBuilder.newJob(targetClass)
                .withIdentity(String.valueOf(reportTemplate.getId()),JOB_GROUP_NAME)
                .usingJobData("reportConditionId",reportTemplate.getReportConditionId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(String.valueOf(reportTemplate.getId()),JOB_GROUP_NAME)
                .withSchedule(CronScheduleBuilder.cronSchedule(getCronExpression(reportTemplate.getSubscriptionTime())))
                .build();
        try {
            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /***
     * 修改定时任务
     */
    public void updateJob(){


    }

    /***
     * 删除定时任务
     */
    public void deleteJob(){


    }

    private String getCronExpression(String subscriptionTime){
        String result = "";
        CycleTypeEnum cycleTypeEnum = CycleTypeEnum.valueOf(subscriptionTime);
        switch (cycleTypeEnum){
            case WEEK:
                result = QuartzManagement.WEEK;
                break;
            case MONTH:
                result = QuartzManagement.MONTH;
                break;
            case QUARTER:
                result = QuartzManagement.QUARTER;
                break;
            case YEAR:
                result = QuartzManagement.YEAR;
                break;
        }
        return result;
    }
}
