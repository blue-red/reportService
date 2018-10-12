package com.cwn.wizbank.report.entity;

import com.cwn.wizbank.report.repository.dialect.JsonArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * 归档数据
 * @author jack.wang 2018-04-26
 **/
@Table(name = "report_archive")
@Entity
@TypeDef(name = "JsonArrayType", typeClass = JsonArrayType.class)
public class ReportArchive {

    @Id
    @Column(name = "rea_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联历史记录的id
     */
    @Column(name = "rea_reh_id")
    private Long reportHistoryId;

    /**
     * 归档数据
     */
    @Column(name = "rea_data")
    @Type(type = "JsonArrayType")
    private List<Map<String,Object>> data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportHistoryId() {
        return reportHistoryId;
    }

    public void setReportHistoryId(Long reportHistoryId) {
        this.reportHistoryId = reportHistoryId;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
