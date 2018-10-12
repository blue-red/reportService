package com.cwn.wizbank.report.common;

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

import java.util.List;
import java.util.Map;

/**
 * 报表图表Option模板工具类
 * @author bill.lai 2018/5/23.
 */
public class ReportChartOptionTemplateUtil{

    /**
     * 圆圈饼状图模板
     * @param name
     * @param chartData 数据列表
     *             注：list中Map<String, Object> 需包含name，value
     * @return
     */
    public static Option getPieChartOption(String name, Map<String, Object> chartData){

        //得到数据列表
        List<Map<String, Object>> list = (List<Map<String, Object>>)chartData.get("list");

        //创建Option
        GsonOption option = new GsonOption();

        //设置背景图片
        option.backgroundColor("#fff");

        //设置legend列表
        option.legend().show(true).orient(Orient.vertical).x(X.right).y(Y.center).selectedMode(false).data(list.toArray());

        //饼图
        Pie pie = new Pie(name);

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

}
