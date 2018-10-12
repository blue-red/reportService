package com.cwn.wizbank.report.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 学员测验结果报表
 * @author jack 2018-05-23
 **/
@Component
public interface UserLearningExamResultMapper extends SuperMapper{

    /***
     * 获取学员测验结果
     * @return
     */
    List<Map<String,Object>> getUserLearningExamResult(Map<String,Object> params);

    /***
     * 根据学员报名跟踪id和尝试次数和问测验id
     * @param params
     * @return
     */
    List<Map<String,Object>> getExamInfoByExamIdAndAttemptAndTkhId(Map<String,Object> params);

}
