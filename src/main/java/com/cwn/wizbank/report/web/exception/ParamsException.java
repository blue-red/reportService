package com.cwn.wizbank.report.web.exception;
/**
 * Rest参数异常
 * @author bill.lai 2018/5/22.
 */
public class ParamsException extends RuntimeException{

    private int errorCode;
    private String errorMessage;

    public ParamsException(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode(){
        return errorCode;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
