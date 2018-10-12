package com.cwn.wizbank.report.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 用户信息mapper
 * @author jack 2018-05-15
 **/
@Component
public interface UserLearningDetailsMapper {

    /***
     * 获取学员学习统计
     * @param params
     * @return
     */
    List<Map<String,Object>> getUserLearningStatistics(Map<String,Object> params);

    /***
     * 获取学员网上课程和考试学习明细
     * @param params
     * @return
     */
    List<Map<String,Object>> getUserOnlineItemLearningDetails(Map<String,Object> params);

    /***
     * 获取学员面授课程学习明细
     * @param params
     * @return
     */
    List<Map<String,Object>> getUserOffItemLearningDetails(Map<String,Object> params);

}
