package com.cwn.wizbank.report.event;

import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Map;

/**
 * 报表完成事件
 * @author Andrew.xiao 2018/5/16
 */
public class ReportExportFinishEvent extends ApplicationEvent {
    /**
     * 导出的文件地址
     */
    private String fileUrl;

    /**
     * 导出的结果数据
     */
    private List<Map<String,Object>> data;
    public ReportExportFinishEvent(String fileUrl) {
        super(fileUrl);
        this.fileUrl = fileUrl;
    }

    public ReportExportFinishEvent(List<Map<String,Object>> data) {
        super(data);
        this.data = data;
    }

    public ReportExportFinishEvent(String fileUrl, List<Map<String,Object>> data) {
        super(data);
        this.fileUrl = fileUrl;
        this.data = data;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
