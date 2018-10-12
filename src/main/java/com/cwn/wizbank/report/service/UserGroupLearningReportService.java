package com.cwn.wizbank.report.service;

import com.cwn.wizbank.report.entity.ReportCondition;
import com.cwn.wizbank.report.enums.ItemTypeEnum;
import com.cwn.wizbank.report.mapper.UserGroupLearningMapper;
import com.github.abel533.echarts.LabelLine;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.Position;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.code.Y;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.itemstyle.Normal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门学员学习报表
 * @author jack.wang 2018-04-26
 **/
@Service
public class UserGroupLearningReportService extends AbstractUserLearningService{

    @Autowired
    private UserGroupLearningMapper userGroupLearningMapper;

    @Override
    public List<Map<String,Object>> fetchData(ReportCondition reportCondition, Map<String,Object> paramMap) {
        //结果存放
        List<Map<String,Object>> result = new ArrayList<>();
        //获取要查询的用户组
        List<Long> groupIds = (List<Long>) paramMap.get("groupIds");
        //获取课程和考试的itmId
        List<Long> itmIds = (List)paramMap.get("aeItemIds");
        //查询，拼接结果map
        for(Long groupId : groupIds) {
            Map<String,Object> map = new HashMap<>();
            //获取用户组信息
            if(null != getGroupInfo(groupId)){
                mergeMap(map,getGroupInfo(groupId));
                //获取网上课程学习情况
                mergeMap(map,getLearningSituationByType(groupId,paramMap,ItemTypeEnum.SELFSTUDY));
                //获取面授课程学习情况
                mergeMap(map,getLearningSituationByType(groupId,paramMap,ItemTypeEnum.CLASSROOM));
                //获取考试学习情况
                mergeMap(map,getLearningSituationByType(groupId,paramMap,ItemTypeEnum.EXAM));
                //用户组总人数
                Integer groupUserQuantity = null == map.get("groupUserQuantity") ? 0 : Integer.valueOf(map.get("groupUserQuantity").toString());
                //人均学习时长
                map.put("averageLearnDuration",null == map.get("learnDuration") || 0 ==  groupUserQuantity ? 0D : Double.valueOf(map.get("learnDuration").toString())/groupUserQuantity);
                //人均考试时长
                map.put("averageExamDuration",null == map.get("examLearnDuration") || 0 ==  groupUserQuantity ? 0D : Double.valueOf(map.get("examLearnDuration").toString())/groupUserQuantity);
                //人均在线学习时长
                map.put("averageOnlineLearnDuration",null == map.get("onlineLearnDuration") || 0 ==  groupUserQuantity ? 0D : Double.valueOf(map.get("onlineLearnDuration").toString())/groupUserQuantity);
                //人均面授学习时长
                map.put("averageOfflineLearnDuration",null == map.get("offlineLearnDuration") || 0 ==  groupUserQuantity ? 0D : Double.valueOf(map.get("offlineLearnDuration").toString())/groupUserQuantity);
                //未参与学习人数
                map.put("notAttendQuantity",null == map.get("learnUserQuantity") ? 0 : groupUserQuantity - Integer.valueOf(map.get("learnUserQuantity").toString()));
                //学习总数
                map.put("learnQuantity",userGroupLearningMapper.getLearningQuantityByGroupIdAndItemId(groupIds,itmIds));
                result.add(map);
            }
        }
        return result;
    }

    /**
     * 获取生成部门学习统计报表统计图的数据结构
     * @return
     */
    @Override
    public Option getChartOption(Map<String, Object> chartData){
        //创建Option
        GsonOption option = new GsonOption();

        //设置背景图片
        option.backgroundColor("#fff");

        //得到数据列表
        List<Map<String, Object>> list = (List<Map<String, Object>>)chartData.get("list");
        //设置legend列表
        option.legend().show(true).orient(Orient.vertical).x(X.right).y(Y.center).selectedMode(false).data(list.toArray());

        //饼图
        Pie pie = new Pie("学习状态分布统计图");

        //饼图的半径，数组的第一项是内半径，第二项是外半径
        pie.radius(new String[]{"40%","50%"});

        //是否启用防止标签重叠策略
        pie.avoidLabelOverlap(false);

        Normal normal = new Normal();
        //饼图图形上的文本标签
        normal.show(true).position(Position.left).formatter("{b}\n{c}({d}%)").opacity(1d);
        //标签的视觉引导线样式
        normal.labelLine(new LabelLine().show(true)).opacity(1d);
        pie.label(new ItemStyle().normal(normal));

        //循环数据
        for(Map<String, Object> objectMap : list){
            //饼图数据
            pie.data(new PieData(objectMap.get("name").toString(), objectMap.get("value")));
        }

        //设置数据
        option.series(pie);
        //返回Option
        return option;
    }

    /***
     * 获取用户组信息
     * @param groupId 用户组id
     * @return
     */
    private Map<String,Object> getGroupInfo(Long groupId) {
        return userGroupLearningMapper.getGroupInfoById(groupId);
    }

    /***
     * 根据课程学习情况
     * @param groupId 要查询的用户组id
     * @param itemTypeEnum 课程类型
     * @return
     */
    private Map<String,Object> getLearningSituationByType(Long groupId, Map<String,Object> paramMap , ItemTypeEnum itemTypeEnum){
        Map<String,Object> result = new HashMap<>();
        paramMap.put("groupId",groupId);
        switch (itemTypeEnum){
            case SELFSTUDY://获取网上课程学习情况
                paramMap.put("isClass",false);
                paramMap.put("isExam",false);
                result = userGroupLearningMapper.getOnlineItemLearningSituation(paramMap);
                break;
            case CLASSROOM://获取面授课程和离线考试学习情况
                paramMap.put("isClass",true);
                result = userGroupLearningMapper.getOfflineItemLearningSituation(paramMap);
                break;
            case EXAM://获取考试学习情况
                paramMap.put("isClass",false);
                paramMap.put("isExam",true);
                result = userGroupLearningMapper.getExamLearningSituation(paramMap);
                break;
        }
        return result;
    }

    /***
     * 合并两个map
     * @param resultMap 结果map
     * @param targetMap 目标map
     * @return
     */
    private Map<String,Object> mergeMap(Map<String,Object> resultMap,Map<String,Object> targetMap){
        if (null != targetMap){
            for(String targetKey : targetMap.keySet()) {
                //相同的key且类型为Integer就合并value
                if(resultMap.containsKey(targetKey) && resultMap.get(targetKey) instanceof Double){
                    resultMap.put(targetKey,Double.valueOf(targetMap.get(targetKey).toString()) + Double.valueOf(resultMap.get(targetKey).toString()));
                }else if(resultMap.containsKey(targetKey) && (resultMap.get(targetKey) instanceof Integer)){
                    resultMap.put(targetKey,Integer.valueOf(targetMap.get(targetKey).toString()) + Integer.valueOf(resultMap.get(targetKey).toString()));
                }else if(resultMap.containsKey(targetKey) && (resultMap.get(targetKey) instanceof Long)){
                    resultMap.put(targetKey,Long.valueOf(targetMap.get(targetKey).toString()) + Long.valueOf(resultMap.get(targetKey).toString()));
                }else if(null == resultMap.get(targetKey)){
                    resultMap.put(targetKey,targetMap.get(targetKey));
                }
            }
        }
        return resultMap;
    }
}
