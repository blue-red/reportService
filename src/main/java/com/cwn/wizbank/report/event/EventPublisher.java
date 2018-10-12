package com.cwn.wizbank.report.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * 时间发布器
 * @author Andrew.xiao 2018/5/16
 */
@Component
public class EventPublisher {
    @Autowired
    private ApplicationContext applicationContext;

    public void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }
}
