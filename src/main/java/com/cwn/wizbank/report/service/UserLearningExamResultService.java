package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.mapper.AeItemRelationMapper;
import com.cwn.wizbank.report.mapper.SuperMapper;
import com.cwn.wizbank.report.mapper.UserLearningExamResultMapper;
import com.github.abel533.echarts.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 学员测验结果统计报表
 * @author jack 2018-05-23
 **/
@Service
public class UserLearningExamResultService extends AbstractReportService{

    @Autowired
    private UserLearningExamResultMapper userLearningExamResultMapper;

    @Autowired
    private SuperMapper superMapper;

    @Override
    public List<Map<String, Object>> fetchData(ReportCondition reportCondition, Map<String, Object> paramMap) {
        long start,end;
        start = System.currentTimeMillis();
        //获取学员测验结果
        List<Map<String,Object>> result = userLearningExamResultMapper.getUserLearningExamResult(paramMap);
        for (int i = 0; i < result.size(); i++) {
            Map<String,Object> info = result.get(i);
            //用户基本信息
            Map<String,Object> userInfo = superMapper.getUserInfoByUserId(Long.valueOf(info.get("userId").toString()));
            info.putAll(userInfo);
            //答对题数量
            int correctQuantity = 0;
            //答错题数量
            int errorQuantity = 0;
            //部分正确数量
            int partCorrectQuantity = 0;
            //尚未评分数量
            int notScoreQuantity = 0;
            paramMap.put("attempt",info.get("attempt"));
            paramMap.put("tkhId",info.get("tkhId"));
            paramMap.put("resId",info.get("resId"));
            List<Map<String,Object>> examInfoList = userLearningExamResultMapper.getExamInfoByExamIdAndAttemptAndTkhId(paramMap);
            for(int j = 0; j < examInfoList.size(); j++){
                Map<String,Object> examInfo = examInfoList.get(j);
                if(-1 == Integer.valueOf(examInfo.get("score").toString())){
                    notScoreQuantity += 1;
                }else if(Double.valueOf(examInfo.get("score").toString()) < Double.valueOf(examInfo.get("maxScore").toString())){
                    partCorrectQuantity += 1;
                }else if(0D == Double.valueOf(examInfo.get("score").toString())){
                    errorQuantity += 1;
                }else if(Double.valueOf(examInfo.get("score").toString()) == Double.valueOf(examInfo.get("maxScore").toString())){
                    correctQuantity += 1;
                }
            }
            //题目总数量
            info.put("questionQuantity",examInfoList.size());
            info.put("correctQuantity",correctQuantity);
            info.put("errorQuantity",errorQuantity);
            info.put("partCorrectQuantity",partCorrectQuantity);
            info.put("notScoreQuantity",notScoreQuantity);
        }
        end = System.currentTimeMillis();
        System.out.println(start);
        System.out.println(end);
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
