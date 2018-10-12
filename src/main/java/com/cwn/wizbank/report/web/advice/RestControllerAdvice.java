package com.cwn.wizbank.report.web.advice;

import com.cwn.wizbank.report.web.error.ResponseError;
import com.cwn.wizbank.report.web.exception.ParamsException;
import com.cwn.wizbank.report.web.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Rest 控制器切面
 * @author Andrew.xiao 2018/4/24
 */
@ControllerAdvice
public class RestControllerAdvice {
    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseError resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        Long resourceId = ex.getResourceId();
        String resourceName = ex.getResourceName();
        String errorMesssage = resourceName + " ("+resourceId+") not found";
        return new ResponseError(4, errorMesssage);
    }

    /**
     * 报表参数异常处理器
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(ParamsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseError paramsExceptionHandler(ParamsException ex){
        return new ResponseError(ex.getErrorCode(), ex.getErrorMessage());
    }
}
