package com.cwn.wizbank.report.entity;

import com.cwn.wizbank.report.enums.GenerationTypeEnum;
import com.cwn.wizbank.report.enums.ReportTypeEnum;
import com.cwn.wizbank.report.repository.dialect.JsonbType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * 我的历史报表
 * @author jack.wang 2018-04-26
 **/
@Table(name = "report_history")
@Entity
@TypeDef(name = "JsonbType", typeClass = JsonbType.class)
public class ReportHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reh_id")
    private Long id;

    /**
     * 拥有者id
     */
    @Column(name = "reh_owner_id")
    private Long ownerId;

    /**
     * 报表类型
     */
    @Column(name = "reh_type",nullable=false)
    @Enumerated(EnumType.STRING)
    private ReportTypeEnum type;

    /**
     * 生成参数
     */
    @Column(name = "reh_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private GenerationTypeEnum status;

    @Column(name = "reh_create_time")
    private Date createTime;

    /**
     * 完成时间
     */
    @Column(name = "reh_complete_time")
    private Date completeTime;

    /**
     * 报表查询参数
     */
    @Column(name = "reh_params")
    @Type(type = "JsonbType")
    private Map<String,Object> params;

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

    public ReportTypeEnum getType() {
        return type;
    }

    public void setType(ReportTypeEnum type) {
        this.type = type;
    }

    public GenerationTypeEnum getStatus() {
        return status;
    }

    public void setStatus(GenerationTypeEnum status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
