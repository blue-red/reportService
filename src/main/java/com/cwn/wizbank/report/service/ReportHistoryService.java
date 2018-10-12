package com.cwn.wizbank.report.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.entity.ReportHistory;
import com.cwn.wizbank.report.enums.GenerationTypeEnum;
import com.cwn.wizbank.report.mapper.ReportHistoryMapper;
import com.cwn.wizbank.report.repository.ReportHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Andrew.xiao 2018/5/3
 */
@Service
public class ReportHistoryService {

    @Autowired
    private ReportHistoryRepository reportHistoryRepository;

    @Autowired
    private ReportHistoryMapper reportHistoryMapper;

    /***
     * 保存历史记录
     * @param reportCondition
     * @return
     */
    public ReportHistory saveReportHistoryInfo(ReportCondition reportCondition){
        ReportHistory reportHistory = new ReportHistory();
        reportHistory.setOwnerId(3l);
        reportHistory.setCreateTime(new Date());
        reportHistory.setStatus(GenerationTypeEnum.PROGRESS);
        reportHistory.setType(reportCondition.getType());
        reportHistoryRepository.save(reportHistory);
        return reportHistory;
    }

    /***
     * 获取结果页面条件详细信息
     * @param historyId
     * @return
     */
    public Map<String,Object> getResultConditionInfo(Long historyId){
        ReportHistory reportHistory = reportHistoryRepository.findOne(historyId);
        return reportHistory.getParams();
    }

    /***
     * 我的历史查询
     * @param pageSize 条数
     * @param pageNo 页码
     * @param sort 排序字段
     * @param order 降序/升序
     * @param type 报表类型
     * @return
     */
    public Page getAllHistoryRecord(Integer pageSize, Integer pageNo, String sort, String order, String type){
        boolean isAsc = true;
        if("desc".equals(order)){
            isAsc = false;
        }
        Page page = new Page(pageNo,pageSize,sort,isAsc);
        List<Map<String,Object>> result = reportHistoryMapper.getMyHistoryRecord(page,type);
        page.setRecords(result);
        return page;
    }

    public ReportHistory get(Long id) {
        ReportHistory reportHistory = reportHistoryRepository.findOne(id);
        return reportHistory;
    }
}
