package com.cwn.wizbank.report.config;

import com.cwn.wizbank.report.enums.DBTypeEnum;

/**
 * 当前数据库(数据源)持有者
 * @author Andrew.xiao 2018/5/7
 */
public class DbContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
    /**
     * 设置数据源
     * @param dbTypeEnum
     */
    public static void setDbType(DBTypeEnum dbTypeEnum) {
        contextHolder.set(dbTypeEnum.getValue());
    }

    /**
     * 取得当前数据源
     * @return
     */
    public static String getDbType() {
        return contextHolder.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType() {
        contextHolder.remove();
    }
}
