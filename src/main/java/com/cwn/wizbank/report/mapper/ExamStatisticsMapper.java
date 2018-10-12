package com.cwn.wizbank.report.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 考试统计MyBatis映射接口
 * @author bill.lai 2018/5/17.
 */
@Component
public interface ExamStatisticsMapper{

    /**
     * 获取考试学习统计信息
     * @param params
     * @return
     */
    List<Map<String, Object>> queryExamLearningStatisticsInfo(Map<String, Object> params);

    /**
     * 根据课程ID获取课程的目录信息
     * @param itmId
     * @return
     */
    List<Map<String, Object>> queryCatalogByItmId(Long itmId);

    /**
     * 获取所有的课程目录信息
     * @return
     */
    List<Map<String, Object>> queryCatalog();


    /**
     * 查询考试测试完成统计信息
     * @param params
     * @return
     */
    List<Map<String, Object>> queryExamCompletionStatisticsInfo(Map<String, Object> params);

    /**
     * 查询成绩级别统计
     * @param params
     * @return
     */
    Map<String, Object> queryAchievementsLevelStatisticsInfo(Map<String, Object> params);

}
