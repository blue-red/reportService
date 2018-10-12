package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.mapper.OnlineModuleLearningMapper;
import com.github.abel533.echarts.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 在线模块学习情况
 * @author jack 2018-05-18
 **/
@Service
public class OnlineModuleLearningReportService extends AbstractReportService{

    @Autowired
    private OnlineModuleLearningMapper onlineModuleLearningMapper;

    @Override
    public List<Map<String, Object>> fetchData(ReportCondition reportCondition, Map<String, Object> paramMap) {
        List<Map<String,Object>> result = onlineModuleLearningMapper.getOnlineModuleSituationByUser(paramMap);
        return result;
    }

    @Override
    public Map<String, Object> getChartData(List<Map<String, Object>> data, ReportCondition reportCondition) {
        return null;
    }

    @Override
    public Option getChartOption(Map<String, Object> chartData) {
        return null;
    }
}
