package com.cwn.wizbank.report.entity;

import com.cwn.wizbank.report.enums.ReportTypeEnum;
import com.cwn.wizbank.report.repository.dialect.JsonbType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * 报表查询条件
 * @author jack.wang 2018-04-26
 **/
@Table(name = "report_condition")
@Entity
@TypeDef(name = "JsonbType", typeClass = JsonbType.class)
public class ReportCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rec_id")
    private Long id;

    /**
     * 拥有者id
     */
    @Column(name = "rec_owner_id")
    private Long ownerId;

    /**
     * 报表查询参数
     */
    @Column(name = "rec_params")
    @Type(type = "JsonbType")
    private Map<String,Object> params;

    /**
     * 报表类型
     */
    @Column(name = "rec_type",nullable=false)
    @Enumerated(EnumType.STRING)
    private ReportTypeEnum type;

    /**
     * 是否后台生成
     */
    @Column(name = "rec_is_back")
    private Boolean isBack;

    @Column(name = "rec_create_time")
    private Date createTime;

    @Column(name = "rec_update_time")
    private Date updateTime;

    public ReportCondition() {
    }

    public ReportCondition(Map<String, Object> params) {
        this.params = params;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getIsBack() {
        return isBack;
    }

    public void setIsBack(Boolean back) {
        isBack = back;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public ReportTypeEnum getType() {
        return type;
    }

    public void setType(ReportTypeEnum type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
