package com.cwn.wizbank;

import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.enums.ReportTypeEnum;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学员学习统计报表测试用例
 * @author jack 2018-05-16
 **/
public class UserLearningStatisticsReportTest extends AbstractAppTest{

    /***
     * 后台生成测试用例
     */
    @Test
    public void backPostByTrue(){
        ReportCondition reportCondition = setReportCondition();
        //后台生成
        reportCondition.setIsBack(true);
        restTemplate.postForObject("/api/report/learning-situation/group", reportCondition,
                Map.class);
    }

    private ReportCondition setReportCondition() {
        ReportCondition reportCondition = new ReportCondition();
        reportCondition.setType(ReportTypeEnum.EXAM_LEARNING_STATISTICS);

        List<String> showFieldList = new ArrayList<>();
        showFieldList.add("userName");
        showFieldList.add("fullName");
        showFieldList.add("groupName");
        showFieldList.add("gradeName");
        showFieldList.add("positionName");
        showFieldList.add("learnQuantity");
        showFieldList.add("learnDuration");
        showFieldList.add("examLearnDuration");
        showFieldList.add("completedAeItemQuantity");
        showFieldList.add("inProgressAeItemQuantity");
        showFieldList.add("abandonAeItemQuantity");
        showFieldList.add("unqualifiedAeItemQuantity");
        showFieldList.add("attemptQuantity");
        showFieldList.add("onlineLearnDuration");
        showFieldList.add("offlineLearnDuration");
        showFieldList.add("averageMark");

        Map<String,Object> params = new HashMap<>();
        params.put("queryDate","MONTH");
        params.put("groupIds","ALL");
        params.put("aeItemIds","ALL");
        params.put("showFieldRequire",showFieldList);
        //params.put("subscriptionTime","WEEK");
        reportCondition.setParams(params);
        return reportCondition;
    }
}
