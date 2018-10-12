package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.repository.ReportConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 
 * @author Andrew.xiao 2018/5/3
 */
@Service
public class ReportConditionService {
    @Autowired
    private ReportConditionRepository reportConditionRepository;
    /***
     * 生成方式
     * @param reportCondition 报表查询条件基本信息
     * @return
     */
    public ReportCondition saveReportCondition(ReportCondition reportCondition) {
        Date current = new Date();
        reportCondition.setOwnerId(3l);
        reportCondition.setCreateTime(current);
        reportCondition.setUpdateTime(current);
        reportConditionRepository.save(reportCondition);
        return reportCondition;
    }

    public ReportCondition get(Long reportConditionId) {
        return reportConditionRepository.findOne(reportConditionId);
    }
}
