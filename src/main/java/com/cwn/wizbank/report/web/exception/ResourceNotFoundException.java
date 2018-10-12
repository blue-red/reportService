package com.cwn.wizbank.report.web.exception;
/**
 * Rest资源找不到异常
 * @author Andrew.xiao 2018/4/24
 */
public class ResourceNotFoundException extends RuntimeException{
    private Long resourceId;
    private String resourceName;
    public ResourceNotFoundException(String resourceName, Long resourceId) {
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }
}
