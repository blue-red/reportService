package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.mapper.AeItemRelationMapper;
import com.cwn.wizbank.report.mapper.UserLearningDetailsMapper;
import com.github.abel533.echarts.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 学员学习明细报表
 * @author jack 2018-05-16
 **/
@Service
public class UserLearningDetailsReportService extends AbstractReportService{

    @Autowired
    private AeItemRelationMapper aeItemRelationMapper;

    @Autowired
    private UserLearningDetailsMapper userLearningDetailsMapper;

    @Override
    public List<Map<String, Object>> fetchData(ReportCondition reportCondition, Map<String, Object> paramMap) {
        List<Map<String,Object>> result = new ArrayList<>();
        //网上课程和考试学习情况
        List<Map<String,Object>> itemList = userLearningDetailsMapper.getUserOnlineItemLearningDetails(paramMap);
        //学员面授学习情况
        List<Map<String,Object>> offlineItemList = userLearningDetailsMapper.getUserOffItemLearningDetails(paramMap);
        result.addAll(itemList);
        result.addAll(offlineItemList);
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
