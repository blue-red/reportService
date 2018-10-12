package com.cwn.wizbank.common;

import com.cwn.wizbank.report.common.ExportReportUtil;
import com.cwn.wizbank.report.enums.CycleTypeEnum;
import com.cwn.wizbank.report.enums.ReportTypeEnum;
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
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * 报表导出工具单元测试类
 * @author bill.lai 2018/5/9.
 */
public class ExportReportUtilTest{

    @Test
    public void exportReport(){
        List<Map<String, Object>> data = new ArrayList();
        Map<String, Object> map = new HashMap<>();
        map.put("groupName","用户组1");
        map.put("groupCode","GW110");
        map.put("groupUserQuantity",23);
        map.put("learnUserQuantity",5);
        map.put("learnQuantity",20);
        map.put("learnDuration",60);
        map.put("examLearnDuration",40);
        map.put("averageLearnDuration",5);
        map.put("averageExamDuration",4);
        map.put("onlineLearnDuration",50);
        map.put("offlineLearnDuration",44);
        map.put("averageOnlineLearnDuration",5);
        map.put("averageOfflineLearnDuration",5);
        map.put("notAttendQuantity",3);
        map.put("inProgressAeItemQuantity",5);
        map.put("completedAeItemQuantity",4);
        map.put("abandonAeItemQuantity",1);
        map.put("unqualifiedAeItemQuantity",1);
        data.add(map);
        map = new HashMap<>();
        map.put("groupName","用户组2");
        map.put("groupCode","GW112");
        map.put("groupUserQuantity",230);
        map.put("learnUserQuantity",50);
        map.put("learnQuantity",203);
        map.put("learnDuration",620);
        map.put("examLearnDuration",403);
        map.put("averageLearnDuration",51);
        map.put("averageExamDuration",42);
        map.put("onlineLearnDuration",504);
        map.put("offlineLearnDuration",454);
        map.put("averageOnlineLearnDuration",65);
        map.put("averageOfflineLearnDuration",35);
        map.put("notAttendQuantity",34);
        map.put("inProgressAeItemQuantity",55);
        map.put("completedAeItemQuantity",44);
        map.put("abandonAeItemQuantity",16);
        map.put("unqualifiedAeItemQuantity",12);
        data.add(map);

        //要显示的列
        List<String> cells = Arrays.asList(new String[]{"groupName", "groupCode", "groupUserQuantity", "learnUserQuantity", "learnQuantity", "learnQuantity", "learnDuration", "examLearnDuration", "averageLearnDuration", "averageExamDuration"});

        //报表查询条件
        HashMap<String, Object> condition = new HashMap<>();
        //yesterday | month | quarter | year | '2018-03-21 00:00:00',//录取日期(string)
        condition.put("queryDate", CycleTypeEnum.YESTERDAY.toString() + ":2016-03-20,2016-03-23");
        condition.put("groupIds", "all:5");
        condition.put("aeItemIds", "all:5");
        //learnStatus: ['C','F','I','W'],//结训状态(已完成，不合格，进行中，已放弃)
        condition.put("learnStatus", Arrays.asList(new String[]{"C","F","I","W"}));

        //报表统计图表数据
        HashMap<String, Object> hashMap = new HashMap<>();
        List<Map<String, Object>> lists = new ArrayList<>();

        Map<String, Object> map2 = new HashMap<>();
        map2.put("name","学习中");
        map2.put("value",109);
        lists.add(map2);

        map2 = new HashMap<>();
        map2.put("name","已完成");
        map2.put("value",99);
        lists.add(map2);

        map2 = new HashMap<>();
        map2.put("name","已拒绝");
        map2.put("value",199);
        lists.add(map2);

        map2 = new HashMap<>();
        map2.put("name","已放弃");
        map2.put("value",159);
        lists.add(map2);

        hashMap.put("list", lists);

        Option option = getChartOption(hashMap);

        //导出报表
        String filePath = ExportReportUtil.exportExcel(ReportTypeEnum.LEARNING_GROUP , condition, cells, data, null);

        assertThat(filePath, endsWith(".xls"));

    }

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
        normal.show(true).position(Position.left).formatter("{b}\n{c}({d}%)").opacity(0.8d);
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

}
