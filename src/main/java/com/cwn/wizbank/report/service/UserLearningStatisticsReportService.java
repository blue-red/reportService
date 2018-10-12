package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.mapper.AeApplicationMapper;
import com.cwn.wizbank.report.mapper.AeItemRelationMapper;
import com.cwn.wizbank.report.mapper.UserLearningDetailsMapper;
import com.github.abel533.echarts.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 学员学习统计报表
 * @author jack 2018-05-15
 **/
@Service
public class UserLearningStatisticsReportService extends AbstractUserLearningService{

    @Autowired
    private AeItemRelationMapper aeItemRelationMapper;

    @Autowired
    private UserLearningDetailsMapper userLearningDetailsMapper;

    @Autowired
    private AeApplicationMapper aeApplicationMapper;

    @Override
    public List<Map<String, Object>> fetchData(ReportCondition reportCondition, Map<String, Object> paramMap) {
        //获取面授课程和离线考试下的班级和场次
        if(null != paramMap.get("aeItemIds")){
            List<Long> childIds = aeItemRelationMapper.getChildClassList((List<Long>) paramMap.get("aeItemIds"));
            paramMap.put("aeItemIds",((List<Long>) paramMap.get("aeItemIds")).addAll(childIds));
        }
        //获取学员学习统计情况
        List<Map<String,Object>> result = userLearningDetailsMapper.getUserLearningStatistics(paramMap);
        for (int i = 0; i < result.size(); i++) {
            Map<String, Object> info = result.get(i);
            paramMap.put("userId",info.get("userId"));
            //学习总数
            Integer learnQuantity = aeApplicationMapper.getLearningQuantityByUserId(paramMap);
            info.put("learnQuantity",null == learnQuantity ? 0 : learnQuantity);
            //平均成绩
            info.put("averageMark",null == info.get("allScore") ? 0 : Double.valueOf(info.get("allScore").toString()) / learnQuantity);
        }
        return result;
    }

    @Override
    public Option getChartOption(Map<String, Object> chartData) {
        return null;
    }

}
