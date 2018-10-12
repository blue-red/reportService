package com.cwn.wizbank.report.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cwn.wizbank.report.entity.ReportHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 我的历史查询mapper
 * @author jack 2018-05-21
 **/
@Component
public interface ReportHistoryMapper{

    /***
     * 获取我的历史查询
     * @param pagination
     * @param reportType 报表类型
     * @return
     */
    List<Map<String,Object>> getMyHistoryRecord (Pagination pagination, @Param("type") String reportType);
}
