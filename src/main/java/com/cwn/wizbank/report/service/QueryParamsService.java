package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.common.DateUtil;
import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.enums.CycleTypeEnum;
import com.cwn.wizbank.report.enums.ReportParamEnum;
import com.cwn.wizbank.report.web.exception.ParamsException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询参数处理服务
 * @author jack 2018-05-10
 **/
@Service
public class QueryParamsService {

    private static final String START_WITH = " 00:00:00";
    private static final String END_WITH = " 23:59:59";

    /**
     * 验证并拼装新的报表参数，预防前端恶意传入参数，导致系统不稳定
     */
    public void validationParams(ReportCondition reportCondition){
        //报表查询条件为null
        if(reportCondition == null){
            throw new ParamsException(404, "Parameter not found");
        }
        //前段传过来的参数
        Map<String, Object> params = reportCondition.getParams();
        //报表查询参数为null
        if(params == null || params.isEmpty()){
            throw new ParamsException(404, "Parameter params not found");
        }
        //新的报表参数字典
        Map<String, Object> reportParams = new HashMap<>();
        //循环所有可以有的参数，并进行相关的验证
        for(ReportParamEnum reportParamEnum : ReportParamEnum.values()){
            //参数名
            String paramKey = reportParamEnum.getValue();
            //参数值
            Object paramValue = params.get(paramKey);
            //如果参数值不为空
            if(paramValue != null){
                //验证参数值是否规范
                switch(reportParamEnum){
                    case QUERY_DATE:
                        //如果不是字符串类型
                        if(!(paramValue instanceof String)){
                            throw new ParamsException(400, "Parameter "+ paramKey +" type error");
                        }
                        reportParams.put(paramKey, paramValue);
                        break;
                    //用户组ID
                    case GROUP_IDS:
                    //课程ID
                    case AE_ITEM_IDS:
                    //用户ID
                    case USER_IDS:
                    //模块ID
                    case MODULE_IDS:
                        if(paramValue instanceof String){
                            if(!CycleTypeEnum.ALL.name().equalsIgnoreCase((String)paramValue)){
                                throw new ParamsException(400, "Parameter "+ paramKey +" type error");
                            }
                        }else if(!(paramValue instanceof ArrayList)){
                            throw new ParamsException(400, "Parameter "+ paramKey +" type error");
                        }
                        reportParams.put(paramKey, paramValue);
                        break;
                    //包含已删除用户
                    case DELETE_USER:
                        if(!(paramValue instanceof Boolean)){
                            throw new ParamsException(400, "Parameter "+ paramKey +" type error");
                        }
                        reportParams.put(paramKey, paramValue);
                        break;
                    //学习状态
                    case LEARN_STATUS:
                    //必须显示的字段
                    case SHOW_FIELD_REQUIRE:
                    //要显示的字段
                    case SHOW_FIELD_OPTIONAL:
                        if(!(paramValue instanceof ArrayList)){
                            throw new ParamsException(400, "Parameter "+ paramKey +" type error");
                        }
                        reportParams.put(paramKey, paramValue);
                        break;
                    default:
                        break;
                }
            }
        }
        //将验证后的参数设置进报表查询条件中
        reportCondition.setParams(reportParams);
    }

    /***
     * 参数处理
     * @param reportCondition
     * @return
     */
    public Map<String,Object> getParamMap(ReportCondition reportCondition){
        Map<String,Object> params = new HashMap<>();
        Map<String,Object> condition = reportCondition.getParams();

        //查询时间 | 录取时间
        if(null != condition.get("queryDate")){
            CycleTypeEnum cycleTypeEnum = CycleTypeEnum.valueOf(condition.get("queryDate").toString());
            if (null == cycleTypeEnum){
                cycleTypeEnum = CycleTypeEnum.DIY;
            }
            switch (cycleTypeEnum){
                case YESTERDAY:
                    params.put("startDate",DateUtil.getDateOfYesterday() + START_WITH);
                    params.put("endDate",DateUtil.getDateOfYesterday() + END_WITH);
                    break;
                case MONTH:
                    params.put("startDate",DateUtil.getFirstDayOfMonth() + START_WITH);
                    params.put("endDate",DateUtil.getLastDayOfMonth() + END_WITH);
                    break;
                case QUARTER:
                    params.put("startDate",DateUtil.getFirstDayOfQuarter() + START_WITH);
                    params.put("endDate",DateUtil.getLastDayOfQuarter() + END_WITH);
                    break;
                case YEAR:
                    params.put("startDate",DateUtil.getFirstDayOfYear() + START_WITH);
                    params.put("endDate",DateUtil.getLastDayOfYear() + END_WITH);
                    break;
                case ALL:
                    break;
                default:
                    String[] date = condition.get("queryDate").toString().split(",");
                    if(date.length == 1){
                        params.put("startDate",date[0] + START_WITH);
                    }else if(date.length == 2){
                        params.put("startDate",date[0] + START_WITH);
                        params.put("endDate",date[1] + END_WITH);
                    }
                    break;
            }
        }

        //查询的用户组
        if(null != condition.get("groupIds")){
            if(null != condition.get("groupIds") && condition.get("groupIds") instanceof String){
               // TODO 管理员所管理的全部用户组
                List<Long> list = new ArrayList<>();
                list.add(1l);
                list.add(2l);
                list.add(8l);
                list.add(10l);
                list.add(11l);
                params.put("groupIds",list);
            }else{
                params.put("groupIds",transformationType((List<Integer>) condition.get("groupIds")));
            }
        }

        //查询的课程和考试
        if(null != condition.get("aeItemIds")){
            if(condition.get("aeItemIds") != null && condition.get("aeItemIds") instanceof String){
                // TODO 管理员所管理的全部课程考试
            }else{
                params.put("aeItemIds",transformationType((List<Integer>) condition.get("aeItemIds")));
            }
        }

        //查询的用户
        if(null != condition.get("userIds")){
            if(null != condition.get("userIds") && condition.get("userIds") instanceof String){
                // TODO 管理员所管理的全部用户
            }else{
                params.put("userIds",transformationType((List<Integer>) condition.get("userIds")));
            }
        }

        //是否包含回收站用户 true | false
        if(null != condition.get("deleteUser")){
            params.put("deleteUser",condition.get("deleteUser"));
        }

        //结训状态
        if(null != condition.get("learnStatus")){
            params.put("learnStatus",condition.get("learnStatus"));
        }

        //指定模块
        if (null != condition.get("moduleIds")){
            params.put("moduleIds",transformationType((List<Integer>) condition.get("moduleIds")));
        }
        return params;
    }

    /**
     * 合并页面请求参数和sql查询参数并保存，用户查看历史报表的时候查看
     * @param requestParams 合并页面请求参数
     * @param queryParams sql查询参数
     * @return
     */
    public Map<String,Object> mergeQueryParam(Map<String, Object> requestParams, Map<String, Object> queryParams) {
        if (CollectionUtils.isEmpty(requestParams)) {
            return new HashMap<>();
        }
        /*
         * 查询时间
         */
        if (requestParams.get("queryDate") != null) {
            String mergeDate = requestParams.get("queryDate").toString();
            if(mergeDate.indexOf(",") == -1){
                if (queryParams.get("startDate") != null) {
                    mergeDate += ":"+queryParams.get("startDate");
                }
                if (queryParams.get("endDate") != null) {
                    mergeDate += ","+queryParams.get("endDate");
                }
            }
            requestParams.put("queryDate",mergeDate);
        }

        /**
         * 查询的用户组
         */
        if(requestParams.get("groupIds") != null){
            String mergeGroup;
            if (requestParams.get("groupIds") instanceof String) {
                mergeGroup = "all," +  ((List)queryParams.get("groupIds")).size();
            }else{
                mergeGroup = "appoint," +  ((List)queryParams.get("groupIds")).size();
            }
            requestParams.put("groupIds",mergeGroup);
        }

        /**
         * 查询的课程和考试
         */
        if(requestParams.get("aeItemIds") != null){
            String mergeGroup;
            if (requestParams.get("aeItemIds") instanceof String) {
                mergeGroup = "all,6";
            }else{
                mergeGroup = "appoint," +  ((List)queryParams.get("aeItemIds")).size();
            }
            requestParams.put("aeItemIds",mergeGroup);
        }

        /**
         * 查询的用户
         */
        if(requestParams.get("userIds") != null){
            String mergeGroup;
            if (requestParams.get("userIds") instanceof String) {
                mergeGroup = "all," +  (null != queryParams.get("userIds") ? ((List)queryParams.get("userIds")).size() : "0");
            }else{
                mergeGroup = "appoint," +  ((List)queryParams.get("userIds")).size();
            }
            requestParams.put("userIds",mergeGroup);
        }
        return requestParams;
    }

    private List<Long> transformationType(List<Integer> targetList){
        List<Long> result = new ArrayList<>();
        for (Integer id : targetList) {
            result.add(Long.valueOf(id));
        }
        return result;
    }
}
