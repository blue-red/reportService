package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.common.NumberUtil;
import com.cwn.wizbank.report.common.ReportChartOptionTemplateUtil;
import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.enums.ReportParamEnum;
import com.cwn.wizbank.report.enums.ReportTypeEnum;
import com.cwn.wizbank.report.mapper.ExamStatisticsMapper;
import com.github.abel533.echarts.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考试测试完成统计业务逻辑类
 * @author bill.lai 2018/5/23.
 */
@Service
public class ExamCompletionStatisticsReportService extends AbstractReportService{

    @Autowired
    private ExamStatisticsMapper examStatisticsMapper;

    @Autowired
    private QueryParamsService queryParamsService;


    @Override
    public List<Map<String, Object>> fetchData(ReportCondition reportCondition, Map<String, Object> paramMap){
        //查询统计信息
        List<Map<String, Object>> examCompletionStatisticsList = examStatisticsMapper.queryExamCompletionStatisticsInfo(paramMap);
        //不为空时
        if(!CollectionUtils.isEmpty(examCompletionStatisticsList)){
            //循环考试完成统计信息
            for(Map<String, Object> examCompletionStatisticsInfo : examCompletionStatisticsList){
                //必选列
                List<String> requireField = (List<String>)reportCondition.getParams().get(ReportParamEnum.SHOW_FIELD_REQUIRE.getValue());
                if(!CollectionUtils.isEmpty(requireField)){
                    //可选列
                    List<String> optionalField = (List<String>)reportCondition.getParams().get(ReportParamEnum.SHOW_FIELD_OPTIONAL.getValue());
                    if(!CollectionUtils.isEmpty(optionalField)){
                        requireField.addAll(optionalField);
                    }
                    for(String key : requireField){
                        if(examCompletionStatisticsInfo.get(key) instanceof BigDecimal){
                            //格式化数字，保留两位小数
                            examCompletionStatisticsInfo.put(key, NumberUtil.formatDecimal(((BigDecimal)examCompletionStatisticsInfo.get(key)).doubleValue(),2));
                        }
                    }
                }
                //参与人数
                Integer involvementQuantity = (Integer)examCompletionStatisticsInfo.get("involvementQuantity");
                if(involvementQuantity != null && involvementQuantity > 0){
                    //学员合格率为合格人数÷参与人数
                    Integer passQuantity = (Integer)examCompletionStatisticsInfo.get("passQuantity");
                    if(passQuantity != null){
                        //合格率
                        double passRate = NumberUtil.formatDecimal((passQuantity.floatValue() / involvementQuantity) * 100,2);
                        examCompletionStatisticsInfo.put("passRate", passRate + "%");
                    }
                    //平均分为各个学员得到的最高测验分数之和÷参与人数
                    Double totalLearnerScore = (Double)examCompletionStatisticsInfo.get("totalLearnerScore");
                    if(totalLearnerScore != null){
                        //平均分
                        examCompletionStatisticsInfo.put("averageScore", NumberUtil.formatDecimal(totalLearnerScore/involvementQuantity, 2));
                    }
                }
            }
        }
        return examCompletionStatisticsList;
    }

    @Override
    public Map<String, Object> getChartData(List<Map<String, Object>> data, ReportCondition reportCondition){
        //解析参数
        Map<String, Object> paramMap = queryParamsService.getParamMap(reportCondition);
        Map<String, Object> achievementsLevelStatisticsInfo = examStatisticsMapper.queryAchievementsLevelStatisticsInfo(paramMap);
        if(!CollectionUtils.isEmpty(achievementsLevelStatisticsInfo)){
            //需要显示的数据
            String[] names = new String[]{"involvementQuantity", "excellentQuantity", "goodQuantity", "ordinaryQuantity", "qualifiedQuantity", "unqualifiedQuantity"};
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> statisticsInfo;
            for(int i = 0; i < names.length; i++){
                statisticsInfo = new HashMap<>();
                statisticsInfo.put("name", names[i]);
                statisticsInfo.put("value", achievementsLevelStatisticsInfo.get(names[i]));
                list.add(statisticsInfo);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("list", list);
            return result;
        }
        return null;
    }

    @Override
    public Option getChartOption(Map<String, Object> chartData){
        return ReportChartOptionTemplateUtil.getPieChartOption(ReportTypeEnum.EXAM_COMPLETION_STATISTICS.name(), chartData);
    }
}
