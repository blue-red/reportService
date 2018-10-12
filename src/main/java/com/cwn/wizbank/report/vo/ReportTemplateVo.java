package com.cwn.wizbank.report.vo;

import com.cwn.wizbank.report.entity.ReportCondition;

import java.util.Map;

/**
 * 报表模板参数
 * @author Andrew.xiao 2018/5/9
 */
public class ReportTemplateVo {

    private Long id;

    /**
     * 模板名称
     */
    private String name;
    /**
     * 报表生成条件
     */
    private ReportCondition reportCondition;

    /**
     * 订阅时间
     * @see com.cwn.wizbank.report.enums.CycleTypeEnum
     */
    private String subscriptionTime;

    /**
     * 报表订阅对象
     * {name1:email, name2:email2.......}
     */
    private Map<String,String> subscribers;

    /**
     * 报表模板所属用户id
     */
    private Long ownerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReportCondition getReportCondition() {
        return reportCondition;
    }

    public void setReportCondition(ReportCondition reportCondition) {
        this.reportCondition = reportCondition;
    }

    public Map<String, String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Map<String, String> subscribers) {
        this.subscribers = subscribers;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getSubscriptionTime() {
        return subscriptionTime;
    }

    public void setSubscriptionTime(String subscriptionTime) {
        this.subscriptionTime = subscriptionTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
