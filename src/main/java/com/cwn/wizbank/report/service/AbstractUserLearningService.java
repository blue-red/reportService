package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.entity.ReportCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学员学习抽象类
 * @author jack 2018-05-22
 **/
public abstract class AbstractUserLearningService extends AbstractReportService{

    /**
     * 获取要显示在eChart图表上的数据
     * @param data 源数据
     * @return
     */
    @Override
    public Map<String, Object> getChartData(List<Map<String, Object>> data, ReportCondition reportCondition) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        //进行中的课程和考试
        int inProgressAeItemQuantity = 0;
        //已完成的课程和考试
        int completedAeItemQuantity = 0;
        //不合格的课程和考试
        int unqualifiedAeItemQuantity = 0;
        //已放弃的课程和考试
        int abandonAeItemQuantity = 0;

        for (Map<String, Object> userLearningInfo : data) {
            inProgressAeItemQuantity = inProgressAeItemQuantity + (null == userLearningInfo.get("inProgressAeItemQuantity") ? 0 : Integer.valueOf(userLearningInfo.get("inProgressAeItemQuantity").toString()));
            completedAeItemQuantity = completedAeItemQuantity + (null == userLearningInfo.get("completedAeItemQuantity") ? 0 : Integer.valueOf(userLearningInfo.get("completedAeItemQuantity").toString()));
            unqualifiedAeItemQuantity = unqualifiedAeItemQuantity + (null == userLearningInfo.get("unqualifiedAeItemQuantity") ? 0 : Integer.valueOf(userLearningInfo.get("unqualifiedAeItemQuantity").toString()));
            abandonAeItemQuantity = abandonAeItemQuantity + (null == userLearningInfo.get("abandonAeItemQuantity") ? 0 : Integer.valueOf(userLearningInfo.get("abandonAeItemQuantity").toString()));
        }

        String[] names = new String[]{"进行中", "已完成", "不合格", "已放弃"};
        Integer[] values = new Integer[]{inProgressAeItemQuantity, completedAeItemQuantity, unqualifiedAeItemQuantity, abandonAeItemQuantity};
        Map<String, Object> map;
        for(int i = 0; i < names.length; i++){
            map = new HashMap<>();
            map.put("name", names[i]);
            map.put("value", values[i]);
            list.add(map);
        }
        hashMap.put("list", list);
        return hashMap;
    }


}
