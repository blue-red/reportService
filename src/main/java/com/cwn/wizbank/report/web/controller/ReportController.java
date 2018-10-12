package com.cwn.wizbank.report.web.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.cwn.wizbank.report.entity.ReportArchive;
import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.entity.ReportHistory;
import com.cwn.wizbank.report.service.*;
import com.cwn.wizbank.report.vo.ReportTemplateVo;
import com.cwn.wizbank.report.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 报表公共控制器，定义了公共的请求接口
 * @author Andrew.xiao 2018/5/14
 */
@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportTemplateService reportTemplateService;

    @Autowired
    private ReportServiceProxy reportServiceProxy;

    @Autowired
    private ReportConditionService reportConditionService;

    @Autowired
    private ReportHistoryService reportHistoryService;

    @Autowired
    private ReportArchiveService reportArchiveService;

    /***
     * 将报表生成参数保存为模板
     * @param reportTemplateVo 报表生成参数
     */
    @RequestMapping(value = "/template", method = RequestMethod.POST)
    public void saveReportTemplate(@RequestBody ReportTemplateVo reportTemplateVo){
        reportTemplateService.saveTemplate(reportTemplateVo);
    }

    /***
     * 生成报表
     * @param reportCondition 请求参数
     */
    @RequestMapping(value = "/generation", method = RequestMethod.POST)
    public Collection<Map<String,Object>> generateGroupReport(@RequestBody ReportCondition reportCondition){
        Collection<Map<String,Object>> data = reportServiceProxy.execute(reportCondition,null);
        return data;
    }

    /***
     * 生成报表
     * @param reportConditionId ReportCondition id
     */
    @RequestMapping(value = "/generation", method = RequestMethod.GET)
    public Collection<Map<String,Object>> generateGroupReport(@RequestParam Long reportConditionId){
        ReportCondition reportCondition = reportConditionService.get(reportConditionId);
        return generateGroupReport(reportCondition);
    }

    /***
     * 获取结果页面条件详细信息
     * @param historyId
     * @return
     */
    @RequestMapping(value = "/conditionInfo", method = RequestMethod.GET)
    public Map<String,Object> getResultConditionInfo(@RequestParam Long historyId){
        Map<String,Object> result = reportHistoryService.getResultConditionInfo(historyId);
        return result;
    }

    /***
     * 获取我的模板
     * @param pageSize 条数
     * @param pageNo 页码
     * @param sort 排序字段
     * @param order 降序/升序
     * @return
     */
    @RequestMapping(value = "/my-template", method = RequestMethod.GET)
    public Page getAllReportTemplate(@RequestParam Integer pageSize,
                                     @RequestParam Integer pageNo,
                                     @RequestParam(defaultValue = "rec_create_time") String sort,
                                     @RequestParam(defaultValue = "desc") String order){
        Page result = reportTemplateService.getAllReportTemplate(pageSize,pageNo,sort,order);
        return result;
    }

    /***
     * 获取我的历史查询
     * @param pageSize 条数
     * @param pageNo 页码
     * @param sort 排序字段
     * @param order 降序/升序
     * @param reportType 报表类型
     * @return
     */
    @RequestMapping(value = "/my-history", method = RequestMethod.GET)
    public Page getAllHistoryRecord(@RequestParam Integer pageSize,
                                    @RequestParam Integer pageNo,
                                    @RequestParam(defaultValue = "reh_create_time") String sort,
                                    @RequestParam(defaultValue = "desc") String order,
                                    @RequestParam(required = false) String reportType){

        Page page = reportHistoryService.getAllHistoryRecord(pageSize,pageNo,sort,order,reportType);
        return page;
    }


    /**
     * 获取报表模板
     * @param id
     * @return
     */
    @RequestMapping(value = "/template/{id}", method = RequestMethod.GET)
    public ReportTemplateVo getReportTemplate(@PathVariable("id") Long id){
        return reportTemplateService.get(id);
    }

    /**
     * 获取报表结果参数
     * @param reportHistoryId
     * @return
     */
    @RequestMapping(value = "/result/params", method = RequestMethod.GET)
    public Map<String,Object> getReportResultParams(Long reportHistoryId){
        ReportHistory reportHistory = reportHistoryService.get(reportHistoryId);
        if (reportHistory != null && reportHistory.getParams() != null) {
            return reportHistory.getParams();
        }else{
            throw new ResourceNotFoundException("report history params", reportHistoryId);
        }
    }

    /**
     * 获取报表结果数据
     * @param reportHistoryId
     * @return
     */
    @RequestMapping(value = "/result/data", method = RequestMethod.GET)
    public List<Map<String, Object>> getReportResultData(Long reportHistoryId){
        ReportArchive reportArchive = reportArchiveService.getByReportHistoryId(reportHistoryId);
        if (reportArchive != null) {
            return reportArchive.getData();
        } else {
            throw new ResourceNotFoundException("report result data",reportHistoryId);
        }
    }

}
