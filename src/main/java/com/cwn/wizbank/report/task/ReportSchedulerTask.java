package com.cwn.wizbank.report.task;

import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.service.IReportService;
import com.cwn.wizbank.report.service.ReportConditionService;
import com.cwn.wizbank.report.service.ReportServiceProxy;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 部门学员学习报表订阅job
 * @author jack.wang 2018-04-26
 **/
@Component
public class ReportSchedulerTask implements Job {

    @Autowired
    private ReportServiceProxy reportServiceProxy;

    @Autowired
    private ReportConditionService reportConditionService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        long reportConditionId = jobDataMap.getLong("reportConditionId");
        ReportCondition reportCondition = reportConditionService.get(reportConditionId);
        reportCondition.setIsBack(true);//订阅报表也是属于后台生成报表
        reportServiceProxy.execute(reportCondition, new IReportService.ExecuteCallback() {
            @Override
            public void onFinish(List<Map<String, Object>> data, String exportFileUrl) {
                logger.info(exportFileUrl);
            }
        });
    }
}
