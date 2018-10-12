package com.cwn.wizbank.report.entity;

import com.cwn.wizbank.report.repository.dialect.JsonbType;
import com.cwn.wizbank.report.vo.ReportTemplateVo;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Map;

/**
 * 我的报表模板
 * @author jack.wang 2018-04-26
 **/
@Table(name = "report_template")
@Entity
@TypeDef(name = "JsonbType", typeClass = JsonbType.class)
public class ReportTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ret_id")
    private Long id;

    /**
     * 模板名称
     */
    @Column(name = "ret_template_name")
    private String name;

    /**
     * 报表生成条件id
     */
    @Column(name = "ret_rec_id")
    private Long reportConditionId;

    /**
     * 拥有者id
     */
    @Column(name = "ret_owner_id")
    private Long ownerId;

    /**
     * 订阅时间
     * @see com.cwn.wizbank.report.enums.CycleTypeEnum
     */
    @Column(name = "ret_subscription_time")
    private String subscriptionTime;

    /**
     * 报表订阅对象
     * {name1:email, name2:email2.......}
     */
    @Column(name = "ret_subscribers")
    @Type(type = "JsonbType")
    private Map<String,String> subscribers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubscriptionTime() {
        return subscriptionTime;
    }

    public void setSubscriptionTime(String subscriptionTime) {
        this.subscriptionTime = subscriptionTime;
    }

    public Long getReportConditionId() {
        return reportConditionId;
    }

    public void setReportConditionId(Long reportConditionId) {
        this.reportConditionId = reportConditionId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Map<String, String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Map<String, String> subscribers) {
        this.subscribers = subscribers;
    }

    public static ReportTemplate build(ReportTemplateVo reportTemplateVo) {
        ReportTemplate reportTemplate = new ReportTemplate();
        BeanUtils.copyProperties(reportTemplateVo,reportTemplate);
        return reportTemplate;
    }
}
