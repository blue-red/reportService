package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.entity.ReportArchive;
import com.cwn.wizbank.report.entity.ReportHistory;
import com.cwn.wizbank.report.repository.ReportArchiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Andrew.xiao 2018/5/3
 */
@Service
public class ReportArchiveService {

    @Autowired
    private ReportArchiveRepository reportArchiveRepository;

    /***
     * 保存归档数据
     * @param reportHistory
     * @param data
     * @return
     */
    public ReportArchive saveReportArchiveInfo(ReportHistory reportHistory, List<Map<String, Object>> data){
        ReportArchive reportArchive = new ReportArchive();
        reportArchive.setData(data);
        reportArchive.setReportHistoryId(reportHistory.getId());
        reportArchiveRepository.save(reportArchive);
        return reportArchive;
    }

    public ReportArchive getByReportHistoryId(Long reportHistoryId){
        return reportArchiveRepository.getByReportHistoryId(reportHistoryId);
    }
}
