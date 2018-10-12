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
import java.util.*;

/**
 * 考试学习统计报表Service
 * @author bill.lai 2018/5/17.
 */
@Service
public class ExamLearningStatisticsReportService extends AbstractReportService{

    @Autowired
    private ExamStatisticsMapper examStatisticsMapper;

    /**
     * 获取报表数据
     * @param reportCondition 查询条件
     * @param paramMap
     * @return
     */
    @Override
    public List<Map<String, Object>> fetchData(ReportCondition reportCondition, Map<String, Object> paramMap){
        //获取考试学习统计信息
        List<Map<String, Object>> examLearningStatisticsList = examStatisticsMapper.queryExamLearningStatisticsInfo(paramMap);
        //不为空时
        if(!CollectionUtils.isEmpty(examLearningStatisticsList)){
            //获取所有目录信息
            List<Map<String, Object>> catalogList = examStatisticsMapper.queryCatalog();
            //循环考试学习统计信息
            for(Map<String, Object> examLearningStatisticsInfo : examLearningStatisticsList){
                //必选列
                List<String> requireField = (List<String>)reportCondition.getParams().get(ReportParamEnum.SHOW_FIELD_REQUIRE.getValue());
                if(!CollectionUtils.isEmpty(requireField)){
                    //可选列
                    List<String> optionalField = (List<String>)reportCondition.getParams().get(ReportParamEnum.SHOW_FIELD_OPTIONAL.getValue());
                    if(!CollectionUtils.isEmpty(optionalField)){
                        requireField.addAll(optionalField);
                    }
                    for(String key : requireField){
                        if(examLearningStatisticsInfo.get(key) instanceof BigDecimal){
                            //格式化数字，保留两位小数
                            examLearningStatisticsInfo.put(key, NumberUtil.formatDecimal(((BigDecimal)examLearningStatisticsInfo.get(key)).doubleValue(),2));
                        }
                    }
                }
                //获取课程目录
                getAeItemCatalog(examLearningStatisticsInfo, catalogList);
                //平均成绩
                Integer applyQuantity = (Integer)examLearningStatisticsInfo.get("applyQuantity");
                Double score = (Double)examLearningStatisticsInfo.get("score");
                if(applyQuantity != null && applyQuantity > 0 && score != null){
                    examLearningStatisticsInfo.put("averageScore", NumberUtil.formatDecimal(score/applyQuantity, 2));
                }else{
                    examLearningStatisticsInfo.put("averageScore", 0);
                }
            }
        }
        return examLearningStatisticsList;
    }

    @Override
    public Map<String, Object> getChartData(List<Map<String, Object>> data, ReportCondition reportCondition){
        //进行中
        int inProgressQuantity = 0;
        //已完成
        int completeQuantity = 0;
        //不合格
        int unqualifiedQuantity = 0;
        //已放弃
        int waiveQuantity = 0;
        for(Map<String, Object> examLearningStatisticsInfo : data){
            if(examLearningStatisticsInfo != null && !examLearningStatisticsInfo.isEmpty()){
                if(examLearningStatisticsInfo.get("inProgressQuantity") != null){
                    inProgressQuantity += (Integer)examLearningStatisticsInfo.get("inProgressQuantity");
                }
                if(examLearningStatisticsInfo.get("completeQuantity") != null){
                    completeQuantity += (Integer)examLearningStatisticsInfo.get("completeQuantity");
                }
                if(examLearningStatisticsInfo.get("unqualifiedQuantity") != null){
                    unqualifiedQuantity += (Integer)examLearningStatisticsInfo.get("unqualifiedQuantity");
                }
                if(examLearningStatisticsInfo.get("waiveQuantity") != null){
                    waiveQuantity += (Integer)examLearningStatisticsInfo.get("waiveQuantity");
                }
            }
        }

        String[] names = new String[]{"进行中", "已完成", "不合格", "已放弃"};
        Integer[] values = new Integer[]{inProgressQuantity, completeQuantity, unqualifiedQuantity, waiveQuantity};

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> statisticsInfo;
        for(int i = 0; i < names.length; i++){
            statisticsInfo = new HashMap<>();
            statisticsInfo.put("name", names[i]);
            statisticsInfo.put("value", values[i]);
            list.add(statisticsInfo);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        return result;
    }

    @Override
    public Option getChartOption(Map<String, Object> chartData){
        //圆圈图表
        return ReportChartOptionTemplateUtil.getPieChartOption(ReportTypeEnum.EXAM_LEARNING_STATISTICS.name(), chartData);
    }

    /**
     * 获取课程目录
     * 格式：XXX/AA、XX/BB | XXX
     * @param examLearningStatisticsInfo 考试统计信息
     * @param catalogList 所有目录集合
     */
    public void getAeItemCatalog(Map<String, Object> examLearningStatisticsInfo, List<Map<String, Object>> catalogList){
        if(!CollectionUtils.isEmpty(examLearningStatisticsInfo)){
            //课程ID
            Long itmId = ((Integer) examLearningStatisticsInfo.get("id")).longValue();
            //获取课程所在的目录信息，可多个
            List<Map<String, Object>> catalogs = examStatisticsMapper.queryCatalogByItmId(itmId);
            //课程目录信息不为空
            if(!CollectionUtils.isEmpty(catalogs)){
                //课程目录操作对象
                StringBuilder itemCatalog = new StringBuilder();
                //遍历课程所在的目录
                for (int i = 0; i < catalogs.size(); i++){
                    //目录对象
                    Map<String, Object> catalog = catalogs.get(i);
                    //该课程目录的全目录集合
                    List<String> itemCatalogList = new ArrayList<>();
                    //获取课程目录的全目录
                    getCatalogFullPath(itemCatalogList, catalogList, catalog);
                    //目录集合不为空
                    if (!CollectionUtils.isEmpty(itemCatalogList)){
                        StringBuilder catalogFullName = new StringBuilder();
                        for (int j = itemCatalogList.size() - 1; j >= 0; j--){
                            //目录名称
                            catalogFullName.append(itemCatalogList.get(j));
                            //斜杠
                            if (j != 0){
                                catalogFullName.append("/");
                            }
                        }
                        itemCatalog.append(catalogFullName.toString());

                        //顿号
                        if (i + 1 != catalogs.size()){
                            itemCatalog.append("、");
                        }
                    }
                }
                //put目录信息到考试学习统计信息中
                examLearningStatisticsInfo.put("catalog", itemCatalog.toString());
            }
        }
    }

    /**
     * 获取课程目录的全路径
     * @param itemCatalogList 所有相关的课程目录集合
     * @param catalogList 所有目录集合
     * @param itemCatalog 课程所在的相关目录
     */
    private List<String> getCatalogFullPath(List<String> itemCatalogList, List<Map<String, Object>> catalogList, Map<String, Object> itemCatalog){
        if(!CollectionUtils.isEmpty(catalogList)){
            //如果相关课程目录还有父目录
            if(itemCatalog != null && itemCatalog.get("parentId") != null){
                //把自己先加入进集合中
                itemCatalogList.add((String)itemCatalog.get("name"));
                for(Map<String, Object> catalog : catalogList){
                    //课程相关目录的父id等于目录id
                    if(catalog.get("id") != null && catalog.get("id").equals(itemCatalog.get("parentId"))){
                        //如果还有父级
                        if(catalog.get("parentId") != null){
                            getCatalogFullPath(itemCatalogList, catalogList, catalog);
                        }else{
                            itemCatalogList.add((String)catalog.get("name"));
                            break;
                        }
                    }
                }
            }else{
                itemCatalogList.add((String)itemCatalog.get("name"));
            }
        }
        return itemCatalogList;
    }
}
