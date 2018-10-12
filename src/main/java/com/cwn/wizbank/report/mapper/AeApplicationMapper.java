package com.cwn.wizbank.report.mapper;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 学员报名记录mapper
 * @author jack 2018-05-15
 **/
@Component
public interface AeApplicationMapper {

    /***
     * 根据用户id获取学习总数
     * @param params
     * @return
     */
    Integer getLearningQuantityByUserId(Map<String,Object> params);

}
