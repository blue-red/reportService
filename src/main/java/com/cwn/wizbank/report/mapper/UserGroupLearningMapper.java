package com.cwn.wizbank.report.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 用户组mapper
 * @author jack.wang 2018-05-08
 **/
@Component
public interface UserGroupLearningMapper{

    /***
     * 获取用户组信息
     * @param groupId
     * @return
     */
    Map<String,Object> getGroupInfoById(@Param("groupId") Long groupId);

    /***
     * 根据用户组获取网上课程学习情况
     * @param params
     * @return
     */
    Map<String,Object> getOnlineItemLearningSituation(Map<String,Object> params);

    /***
     * 根据用户组获取面授课程学习情况
     * @param params
     * @return
     */
    Map<String,Object> getOfflineItemLearningSituation(Map<String,Object> params);

    /***
     * 根据用户组获取考试学习情况
     * @param params
     * @return
     */
    Map<String,Object> getExamLearningSituation(Map<String,Object> params);

    /***
     * 根据用户组id与课程id获取学习总数
     * @param groupIds
     * @param itmIds
     * @return
     */
    Integer getLearningQuantityByGroupIdAndItemId(@Param("groupIds") List<Long> groupIds, @Param("aeItemIds") List<Long> itmIds);
}
