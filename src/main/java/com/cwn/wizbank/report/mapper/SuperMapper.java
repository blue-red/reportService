package com.cwn.wizbank.report.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface SuperMapper<T> extends BaseMapper<T> {
    //公共的方法

    /***
     * 获取用户基本信息
     * @param userId
     * @return
     */
    Map<String,Object> getUserInfoByUserId(@Param("userId") Long userId);
}
