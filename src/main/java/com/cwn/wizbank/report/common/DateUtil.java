package com.cwn.wizbank.report.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间工具
 * @author bill.lai 2018/5/7.
 */
public class DateUtil{

    static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 构造方法
     */
    DateUtil(){}

    /**
     * 获取昨天的日期
     * @return
     */
    public static String getDateOfYesterday(){
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.DATE,-1);
        Date time = cale.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        return simpleDateFormat.format(time);
    }


    /**
     * 获取本月的第一天
     * @return
     */
    public static String getFirstDayOfMonth(){
        Calendar cale = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        //获取前月的第一天
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return simpleDateFormat.format(cale.getTime());
    }

    /**
     * 获取本月的最后一天
     * @return
     */
    public static String getLastDayOfMonth(){
        Calendar cale = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        //获取前月的最后一天
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        return simpleDateFormat.format(cale.getTime());
    }

    /**
     * 返回当前季度的第一天
     * @return
     */
    public static String getFirstDayOfQuarter() {
        Calendar calendar = Calendar.getInstance();
        //当前季度
        int currentQuarter = calendar.get(Calendar.MONTH) / 3 + 1;
        Integer month = 0;
        switch(currentQuarter){
            case 1:
                month = 0;
                break;
            case 2:
                month = 4 - 1;
                break;
            case 3:
                month = 7 - 1;
                break;
            case 4:
                month = 10 - 1;
                break;
            default:
                month = calendar.get(Calendar.MONTH);
        }
        //设置日期为当前季度的第一个月份的第一天
        calendar.set(calendar.get(Calendar.YEAR), month, 1);

        //格式化日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 返回当前季度的最后一天
     * @return
     */
    public static String getLastDayOfQuarter() {
        Calendar calendar = Calendar.getInstance();
        //当前季度
        int currentQuarter = calendar.get(Calendar.MONTH) / 3 + 1;
        Integer month = 0;
        switch(currentQuarter){
            case 1:
                month = 3 - 1;
                break;
            case 2:
                month = 6 - 1;
                break;
            case 3:
                month = 9 - 1;
                break;
            case 4:
                month = 12 - 1;
                break;
            default:
                month = calendar.get(Calendar.MONTH);
        }
        //设置日期为当前季度的最后一个月份的第一天
        calendar.set(calendar.get(Calendar.YEAR), month, 1);
        //指定月的最后一天
        calendar.roll(Calendar.DATE, -1);
        //格式化日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取当前年度第一天
     * @return
     */
    public static String getFirstDayOfYear(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        //格式化日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取当前年度最后一天
     * @return
     */
    public static String getLastDayOfYear(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        //格式化日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        return simpleDateFormat.format(calendar.getTime());
    }

}
