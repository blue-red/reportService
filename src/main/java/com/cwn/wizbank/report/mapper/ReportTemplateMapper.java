package com.cwn.wizbank.report.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 我的模板mapper
 * @author jack 2018-05-21
 **/
@Component
public interface ReportTemplateMapper{

    /***
     * 获取我的模板
     * @param pagination
     * @return
     */
    List<Map<String,Object>> getMyTemplatePage(Pagination pagination);
}
