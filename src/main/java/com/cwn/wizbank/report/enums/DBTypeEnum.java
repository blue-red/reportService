package com.cwn.wizbank.report.enums;
/**
 * 数据库类型枚举
 * @author Andrew.xiao 2018/5/7
 */
public enum DBTypeEnum {
    core("dataSource_core"),
    report("dataSource_report");
    private String value;

    DBTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
