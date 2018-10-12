package com.cwn.wizbank.report.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 模块信息mapper
 * @author jack 2018-05-17
 **/
@Component
public interface OnlineModuleLearningMapper {

    /***
     * 模块被学习情况
     * @param params
     * @return
     */
    List<Map<String,Object>> getOnlineModuleSituationByUser(Map<String,Object> params);
}
