package com.cwn.wizbank.report.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.entity.ReportTemplate;
import com.cwn.wizbank.report.mapper.ReportTemplateMapper;
import com.cwn.wizbank.report.repository.ReportTemplateRepository;
import com.cwn.wizbank.report.vo.ReportTemplateVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Andrew.xiao 2018/5/3
 */
@Service
public class ReportTemplateService {

    @Autowired
    private ReportTemplateRepository reportTemplateRepository;

    @Autowired
    private ReportConditionService reportConditionService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ReportTemplateMapper reportTemplateMapper;

    /**
     * 保存报表模板
     * @param reportTemplateVo
     */
    public void saveTemplate(ReportTemplateVo reportTemplateVo) {
        ReportTemplate reportTemplate = ReportTemplate.build(reportTemplateVo);
        ReportCondition reportCondition = reportConditionService.saveReportCondition(reportTemplateVo.getReportCondition());
        reportTemplate.setOwnerId(reportCondition.getOwnerId());
        reportTemplate.setReportConditionId(reportCondition.getId());
        reportTemplateRepository.save(reportTemplate);

        subscriptionService.subscription(reportTemplate);
    }

    /**
     * 保存报表模板
     * @param ownerId 模板所属用户的id
     * @param reportTemplateVo
     */
    public void saveTemplate(Long ownerId,ReportTemplateVo reportTemplateVo) {
        reportTemplateVo.setOwnerId(ownerId);
        saveTemplate(reportTemplateVo);
    }

    /***
     * 获取我的模板
     * @param pageSize 条数
     * @param pageNo 页码
     * @param sort 排序字段
     * @param order 降序/升序
     * @return
     */
    public Page getAllReportTemplate(Integer pageSize, Integer pageNo, String sort, String order) {
        boolean isAsc = true;
        if("desc".equals(order)){
            isAsc = false;
        }
        Page page = new Page(pageNo,pageSize,sort,isAsc);
        List<Map<String,Object>> result = reportTemplateMapper.getMyTemplatePage(page);
        page.setRecords(result);
        return page;
    }

    public ReportTemplateVo get(Long id) {
        ReportTemplateVo reportTemplateVo = new ReportTemplateVo();
        ReportTemplate reportTemplate = reportTemplateRepository.findOne(id);
        if (reportTemplate == null) {
            return null;
        }
        BeanUtils.copyProperties(reportTemplate,reportTemplateVo);
        Long reportConditionId = reportTemplate.getReportConditionId();
        reportTemplateVo.setReportCondition(reportConditionService.get(reportConditionId));
        return reportTemplateVo;
    }
}
