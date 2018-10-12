package com.cwn.wizbank.report.enums;

/**
 * 查询报表参数枚举
 * @author bill.lai 2018/05/22.
 */
public enum ReportParamEnum{
    //录取时间|报名时间
    QUERY_DATE("queryDate"),
    //用户组id
    GROUP_IDS("groupIds"),
    //课程/考试id
    AE_ITEM_IDS("aeItemIds"),
    //用户id
    USER_IDS("userIds"),
    //是否包含已删除用户
    DELETE_USER("deleteUser"),
    //学习状态，value=I,C,F,W
    LEARN_STATUS("learnStatus"),
    //模块id
    MODULE_IDS("moduleIds"),
    //必填项
    SHOW_FIELD_REQUIRE("showFieldRequire"),
    //选填项
    SHOW_FIELD_OPTIONAL("showFieldOptional")
    ;

    private String value;

    ReportParamEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
