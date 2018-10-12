package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.entity.ReportCondition;

import java.util.List;
import java.util.Map;

/**
 * 报表服务接口
 * @author Andrew.xiao 2018/5/2
 */
public interface IReportService {
    /**
     * 报表执行方法
     * @param reportCondition 查询参数
     * @param executeCallback 执行回调（针对后台执行的代码）
     * @return
     */
    List<Map<String,Object>> execute(ReportCondition reportCondition, ExecuteCallback executeCallback);

    interface ExecuteCallback{
        void onFinish(List<Map<String,Object>> data, String exportFileUrl);
    }

}
