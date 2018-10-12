package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.enums.ReportTypeEnum;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表执行代理
 * @author Andrew.xiao 2018/5/14
 */
@Service
public class ReportServiceProxy implements IReportService, ApplicationListener<ContextRefreshedEvent> {

    private Map<ReportTypeEnum,IReportService> targetReportServiceMap;
    private ApplicationContext applicationContext;

    @Override
    public List<Map<String, Object>> execute(ReportCondition reportCondition,ExecuteCallback executeCallback) {
        ReportTypeEnum type = reportCondition.getType();
        return targetReportServiceMap.get(type).execute(reportCondition,executeCallback);
    }

    private void registerTargetReportService(ContextRefreshedEvent contextRefreshedEvent){
        targetReportServiceMap = new HashMap<>();
        targetReportServiceMap.put(ReportTypeEnum.LEARNING_GROUP,getServiceByName("userGroupLearningReportService"));
        targetReportServiceMap.put(ReportTypeEnum.LEARNING_USER_STATISTICS,getServiceByName("userLearningStatisticsReportService"));
        targetReportServiceMap.put(ReportTypeEnum.LEARNING_USER_DETAILS,getServiceByName("userLearningDetailsReportService"));
        targetReportServiceMap.put(ReportTypeEnum.LEARNING_USER_EXAM,getServiceByName("userLearningExamResultService"));
        targetReportServiceMap.put(ReportTypeEnum.EXAM_LEARNING_STATISTICS,getServiceByName("examLearningStatisticsReportService"));
        targetReportServiceMap.put(ReportTypeEnum.EXAM_COMPLETION_STATISTICS,getServiceByName("examCompletionStatisticsReportService"));
    }

    private IReportService getServiceByName(String name){
        return (IReportService) applicationContext.getBean(name);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        applicationContext = contextRefreshedEvent.getApplicationContext();
        registerTargetReportService(contextRefreshedEvent);
    }
}
