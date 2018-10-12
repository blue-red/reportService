package com.cwn.wizbank.report.common;

import java.math.BigDecimal;

/**
 * 数字工具类
 * @author bill.lai 2018/5/24.
 */
public class NumberUtil{

    /**
     * 格式化数字
     * @param val 值
     * @param scale 多少位小数
     * @return
     */
    public static double formatDecimal(double val, int scale){
        BigDecimal bigDecimal = new BigDecimal(val);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
