package com.cwn.wizbank.report.web.error;
/**
 * 错误描述
 * @author Andrew.xiao 2018/4/24
 */
public class ResponseError {
    private int code;
    private String message;
    public ResponseError(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
