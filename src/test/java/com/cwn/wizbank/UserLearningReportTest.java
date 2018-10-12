package com.cwn.wizbank;

import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.vo.ReportTemplateVo;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门学员学习报表
 * @author jack 2018-05-09
 **/
public class UserLearningReportTest extends AbstractAppTest{

    /***
     *
     */
    @Test
    public void getDataByConditionId(){
        Map<String,Object> params = new HashMap<>();
        params.put("reportConditionId",6l);
        Map<String,Object> map = restTemplate.getForObject("/api/report/learning-situation/group?reportConditionId={reportConditionId}",Map.class,params);
    }

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

    /***
     * 立即生成测试用例
     */
    @Test
    public void backPostByFalse(){
        ReportCondition reportCondition = setReportCondition();
        //立即生成
        reportCondition.setIsBack(false);
        JSONObject jsonObject = JSONObject.fromObject(restTemplate.postForObject("/api/report/learning-situation/group", reportCondition,
                Map.class));
    }

    /***
     * 保存模板测试用例
     */
    @Test
    public void saveTemplate(){
        ReportTemplateVo reportTemplateVo = new ReportTemplateVo();
        ReportCondition reportCondition = setReportCondition();
        reportCondition.setIsBack(true);
        reportTemplateVo.setReportCondition(reportCondition);
        reportTemplateVo.setName("我是模板");

        Map<String,String> subscriber = new HashMap<>();
        subscriber.put("name1","12712312@qq.com");
        subscriber.put("name2","12712312@qq.com");
        reportTemplateVo.setSubscribers(subscriber);
        restTemplate.postForObject("/api/report/learning-situation/template", reportTemplateVo,Map.class);
    }

    /***
     * 设置查询条件
     * @return
     */
    private ReportCondition setReportCondition(){
        List<Long> testList = new ArrayList<>();
        testList.add(1l);
        testList.add(2l);
        testList.add(3l);
        testList.add(41l);
        testList.add(50l);

        List<String> statusList = new ArrayList<>();
        statusList.add("C");
        statusList.add("F");
        statusList.add("I");
        statusList.add("W");

        List<String> showFieldList = new ArrayList<>();
        showFieldList.add("groupName");
        showFieldList.add("groupCode");
        showFieldList.add("groupUserQuantity");
        showFieldList.add("learnUserQuantity");
        showFieldList.add("learnQuantity");
        showFieldList.add("learnDuration");
        showFieldList.add("examLearnDuration");
        showFieldList.add("averageLearnDuration");

        Map<String,Object> params = new HashMap<>();
        params.put("queryDate","MONTH");
        params.put("groupIds",testList);
        params.put("aeItemIds",testList);
        params.put("learnStatus",statusList);
        params.put("showFieldRequire",showFieldList);
        params.put("subscriptionTime","WEEK");

        ReportCondition reportCondition = new ReportCondition();
        reportCondition.setParams(params);
        return reportCondition;
    }

}
