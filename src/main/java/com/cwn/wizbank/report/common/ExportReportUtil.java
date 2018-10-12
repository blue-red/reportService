package com.cwn.wizbank.report.common;

import com.cwn.wizbank.report.enums.CycleTypeEnum;
import com.cwn.wizbank.report.enums.ReportTypeEnum;
import com.github.abel533.echarts.Option;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * 导出报表工具
 * @author bill.lai 2018/5/2.
 */
public class ExportReportUtil{

    /**
     * 非公有构造方法
     */
    ExportReportUtil(){}

    /**
     * 导出Excel表格
     * @param reportTypeEnum 报表类型
     * @param conditions 报表查询条件
     *                   注：各个报表可能不同，请注意自行编写
     * @param cells 要导出的列
     * @param data 要显示的数据列表，数据根据下标与cell对应
     * @param option 报表统计图数据，没有统计图，传入null
     *                  注：各个报表的统计图各不相同，请在重写getChartOption方法获得对应的option数据
     * @return
     */
    public static String exportExcel(ReportTypeEnum reportTypeEnum, Map<String, Object> conditions, List<String> cells, List<Map<String, Object>> data, Option option){
        //创建工作表
        Workbook workbook = new HSSFWorkbook();
        //文档保存路径
        //报名表名称 TODO 需要国际化
        String reportName = reportTypeEnum.toString();
        //文件名称
        String filePath = "/"+ reportTypeEnum.toString() + System.currentTimeMillis() + ".xls";
        try{
            //创建sheet页
            Sheet sheet = workbook.createSheet(reportName);
            sheet.setDefaultRowHeight((short) 320);
            //row的下标
            int index = 0;
            //1.报表名称
            createReportName(sheet, index, reportName);

            //2.报表查询条件
            index = createCondition(sheet, index, conditions);

            //3.生成统计图表
            index = createCharts(sheet, index, option);

            //4.内容
            createContent(sheet, index, cells, data);

            //5.保存文档
            writeToExcel(workbook, Class.class.getResource("/").getPath() + "/static/" + filePath);

        }finally{
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    /**
     * 创建报表名称
     * @param sheet 页
     * @param index 行数下标
     * @param reportName 报表名称
     */
    public static void createReportName(Sheet sheet, int index, String reportName){
        //创建行
        Row row = sheet.createRow(index);
        //获取标题单元格样式
        HSSFCellStyle titleCellStyle = ExportReportUtil.getTitleCellStyle(sheet);
        //创建单元格
        Cell cell = row.createCell(0);
        //设置方格显示内容
        cell.setCellValue(reportName);
        //设置单元格样式
        cell.setCellStyle(titleCellStyle);
    }

    /**
     * 标题的单元格样式
     * @param sheet 页
     * @return
     */
    public static HSSFCellStyle getTitleCellStyle(Sheet sheet){
        HSSFWorkbook workbook = (HSSFWorkbook)sheet.getWorkbook();
        //设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short)18);
        //设置粗体
        font.setBold(true);

        //设置样式
        HSSFCellStyle style = workbook.createCellStyle();
        //在样式用应用设置的字体
        style.setFont(font);

        //设置自动换行
        style.setWrapText(false);
        //设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 创建条件信息row
     * @param sheet
     * @param index
     * @param conditions
     */
    public static int createCondition(Sheet sheet, int index, Map<String, Object> conditions){
        if(null != conditions && !conditions.isEmpty()){
            //空出一行
            index++;
            //循环写入条件信息
            for(String key : conditions.keySet()){
                if(!"showFieldRequire".equalsIgnoreCase(key) && !"showFieldOptional".equalsIgnoreCase(key)){
                    //创建行
                    Row row = sheet.createRow(++index);
                    //创建第一个单元格
                    Cell keyCell = row.createCell(0);
                    //设置值
                    keyCell.setCellValue(key);
                    //设置单元格样式
                    keyCell.setCellStyle(getConditionKeyCellStyle(sheet));
                    //创建第二个单元格
                    Cell valueCell = row.createCell(1);
                    //设置值
                    valueCell.setCellValue(getConditionCellValue(key, conditions));
                }
            }
        }
        return index;
    }

    /**
     * 条件Key的单元格样式
     * @param sheet 页
     * @return
     */
    public static HSSFCellStyle getConditionKeyCellStyle(Sheet sheet){
        HSSFWorkbook workbook = (HSSFWorkbook)sheet.getWorkbook();
        //设置字体
        HSSFFont font = workbook.createFont();
        //设置粗体
        font.setBold(true);

        //设置样式
        HSSFCellStyle style = workbook.createCellStyle();
        //在样式用应用设置的字体
        style.setFont(font);

        //设置自动换行
        style.setWrapText(false);
        //设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置水平对齐的样式为居中对齐
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    /**
     * 获取报表条件的值
     * @param key 条件的key
     * @param conditions 条件map
     * @return
     */
    public static String getConditionCellValue(String key, Map<String, Object> conditions){
        StringBuilder cellValue = new StringBuilder();
        String conditionValue = String.valueOf(conditions.get(key));
        CycleTypeEnum cycleTypeEnum = null;
        String startDate = null;
        String endDate = null;
        switch(key){
            //录取日期
            case "queryDate":
                if(!"ALL".equals(conditionValue)){
                    String queryDateType = conditionValue.substring(0,conditionValue.indexOf(":"));
                    startDate = conditionValue.substring(conditionValue.indexOf(":")+1,conditionValue.indexOf(","));
                    endDate = conditionValue.substring(conditionValue.indexOf(",")+1,conditionValue.length());
                    cycleTypeEnum = CycleTypeEnum.valueOf(queryDateType);
                }else{
                    cycleTypeEnum = CycleTypeEnum.valueOf(conditionValue);
                }
                //如果没有拿到相关的枚举，那么是自定义
                if(null == cycleTypeEnum){
                    cycleTypeEnum = CycleTypeEnum.DIY;
                }
                switch(cycleTypeEnum){
                    //昨天
                    case YESTERDAY:
                        cellValue.append("昨天").append("（").append(startDate).append("）");
                        break;
                    //本月
                    case MONTH:
                        cellValue.append("本月").append("（").append(startDate).append("至").append(endDate).append("）");
                        break;
                    //本季度
                    case QUARTER:
                        cellValue.append("本季度").append("（").append(startDate).append("至").append(endDate).append("）");
                        break;
                    //本年度
                    case YEAR:
                        cellValue.append("本年").append("（").append(startDate).append("至").append(endDate).append("）");
                        break;
                    case ALL:
                        cellValue.append(conditionValue);
                        break;
                    //自定义（2018-03-21 00:00:00）
                    default:
                        cellValue.append(startDate.isEmpty() ? "--" : startDate).append("至").append(endDate.isEmpty() ? "--" : endDate);
                        break;
                }
                break;
            //用户组
            case "groupIds":
                concatConditionCellValue(conditionValue, cellValue);
                cellValue.append("个用户组");
                break;
            //课程或考试
            case "aeItemIds":
                concatConditionCellValue(conditionValue, cellValue);
                cellValue.append("门课程和考试");
                break;
            //结训状态 learnStatus: ['C','F','I','W'] (已完成，不合格，进行中，已放弃)
            case "learnStatus":
                List<String> learnStatus = (List<String>) conditions.get(key);
                int learnStatusLength = learnStatus.size();
                for(int i = 0; i < learnStatusLength; i++){
                    //TODO 国际化
                    cellValue.append(learnStatus.get(i));
                    if(i+1 < learnStatusLength){
                        cellValue.append(",");
                    }
                }
                break;
            default:
                break;
        }
        return cellValue.toString();
    }

    /**
     * 拼装报表条件的值
     * @param conditionValue
     * @param cellValue
     */
    private static void concatConditionCellValue(String conditionValue, StringBuilder cellValue){
        String type = conditionValue.substring(0,conditionValue.indexOf(","));
        int quantity = Integer.parseInt(conditionValue.substring(conditionValue.indexOf(",")+1, conditionValue.length()));
        if("all".equals(type)){
            cellValue.append("全部");
        }else{
            cellValue.append("指定");
        }
        cellValue.append("【").append(quantity).append("】");
    }

    /**
     * 创建统计图表
     * @param sheet
     * @param index
     * @param option
     */
    public static int createCharts(Sheet sheet, int index, Option option){
        //如果option不为null
        if(null != option){
            //生成图片并保存图片，返回图片路径
            String chartPath = GenerateChartUtil.generateChart(option);

            //图片地址不为空
            if(chartPath != null){
                //空出两行
                index += 2;
                //创建图片标题行
                Row row = sheet.createRow(++index);
                Cell cell = row.createCell(0);
                cell.setCellValue("学习状态分布统计图");

                //创建图片行
                row = sheet.createRow(++index);
                Cell cells = row.createCell(1);
                //设置该单元格的类型为""
                cells.setCellType(CellType.BLANK);

                // 合并单元格// 起始行, 终止行, 起始列, 终止列
                CellRangeAddress cra =new CellRangeAddress(index, index + 15, 1, 6);
                sheet.addMergedRegion(cra);

                //图片输出流
                try(ByteArrayOutputStream outStream = new ByteArrayOutputStream()){
                    //获取BufferedImage对象
                    BufferedImage bufferImg = ImageIO.read(new File(chartPath));
                    //利用HSSFPatriarch将图片写入EXCEL
                    ImageIO.write(bufferImg,"PNG", outStream);
                    //Create the drawing patriarch.  This is the top level container for all shapes.
                    HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();
                    //add a picture
                    HSSFClientAnchor anchor = new HSSFClientAnchor(5,5, 5,5, (short)1, index, (short)7, index + 15);
                    //在patriarch容器显示刚才添加的图片
                    patriarch.createPicture(anchor, sheet.getWorkbook().addPicture(outStream.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));

                }catch(IOException e){
                    e.printStackTrace();
                }

                try{
                    //操作完后删除图片
                    Files.delete(Paths.get(chartPath));
                }catch(IOException e) {
                    e.printStackTrace();
                }

                //空出6行
                index += 15;
            }
        }
        return index;
    }

    /**
     * 创建报表详细内容
     * @param sheet 页
     * @param index 行数下标
     * @param cells 要显示的所有列名
     * @param data 要显示的数据列表，数据根据下标与cell对应
     */
    public static int createContent(Sheet sheet, int index, List<String> cells, List<Map<String, Object>> data){
        //要显示的列不为空
        if(!CollectionUtils.isEmpty(cells)){
            //1.创建内容头部
            //换行并且空出两行
            index += 3;
            //创建行
            Row row = sheet.createRow(index++);
            //获取内容头部单元格样式
            HSSFCellStyle cellStyle = ExportReportUtil.getContentCellStyle(sheet);
            //循环cells
            for(int i = 0; i < cells.size(); i++){
                //创建单元格
                Cell cell = row.createCell(i);
                //设置方格显示内容 TODO 需要国际化
                cell.setCellValue(cells.get(i));
                //设置单元格样式
                cell.setCellStyle(cellStyle);
                //设置cell宽度
                sheet.setColumnWidth(i, 20 * 256);
            }

            //2.创建内容
            if(!CollectionUtils.isEmpty(data)){
                //循环数据
                for(int i = 0; i < data.size(); i++){
                    Map<String, Object> rowInfo = data.get(i);
                    //创建行
                    row = sheet.createRow(index++);
                    //循环所有列
                    for(int j = 0; j < cells.size(); j++){
                        String cellKey = cells.get(j);
                        //创建单元格
                        Cell cell = row.createCell(j);
                        //设置方格显示内容
                        if(null != rowInfo.get(cellKey)){
                            cell.setCellValue(String.valueOf(rowInfo.get(cellKey)));
                        }
                    }
                }
            }
        }
        return index;
    }

    /**
     * 内容头部的单元格样式
     * @param sheet 页
     * @return
     */
    public static HSSFCellStyle getContentCellStyle(Sheet sheet){
        HSSFWorkbook workbook = (HSSFWorkbook)sheet.getWorkbook();
        //设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short)10);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式
        HSSFCellStyle style = workbook.createCellStyle();
        //在样式用应用设置的字体
        style.setFont(font);

        //设置背景填充颜色
        String color = "C0C0C0";
        //转为颜色转为RGB码，转为16进制
        int r = Integer.parseInt((color.substring(0,2)),16);
        int g = Integer.parseInt((color.substring(2,4)),16);
        int b = Integer.parseInt((color.substring(4,6)),16);
        //定义颜色是索引
        short colorIndex = 9;
        //自定义cell颜色
        HSSFPalette palette = workbook.getCustomPalette();
        palette.setColorAtIndex(colorIndex, (byte) r, (byte) g, (byte) b);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(colorIndex);

        //设置底边框
        style.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置左边框
        style.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色
        style.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置右边框
        style.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色
        style.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置顶边框
        style.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //设置自动换行
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 向硬盘写Excel表格
     * @param workbook 工作表
     * @param filePath 保存路径
     */
    public static void writeToExcel(Workbook workbook, String filePath){
        //创建文件流对象
        try(FileOutputStream fos = new FileOutputStream(filePath)){
            //将工作表写入输出流对象（保存文档到硬盘）
            workbook.write(fos);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
