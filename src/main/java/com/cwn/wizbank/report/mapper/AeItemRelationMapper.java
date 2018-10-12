package com.cwn.wizbank.report.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 面授课程与班级关系mapper
 * @author jack 2018-05-15
 **/
@Component
public interface AeItemRelationMapper{
    /***
     * 获取面授课程下的班级
     * @return
     */
    List<Long> getChildClassList(@Param("aeItemIds") List<Long> aeItemIds);
}
