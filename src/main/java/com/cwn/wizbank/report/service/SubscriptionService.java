package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.entity.ReportTemplate;
import com.cwn.wizbank.report.task.ReportSchedulerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 订阅服务
 * @author Andrew.xiao 2018/5/3
 */
@Service
public class SubscriptionService {

    @Autowired
    private QuartzManagement quartzManagement;
    /***
     * 添加订阅job
     * @param reportTemplate
     */
    public void subscription(ReportTemplate reportTemplate){
        //获取订阅对象 [{'name','12345678@email.com'},{'name1','123456789@email.com'}]
        Map<String,String> map = reportTemplate.getSubscribers();
        String subscriptionTime = reportTemplate.getSubscriptionTime();
        if (!CollectionUtils.isEmpty(map) && !StringUtils.isEmpty(subscriptionTime)){
            //添加订阅任务
            quartzManagement.addJob(ReportSchedulerTask.class,reportTemplate);
        }
    }
}
