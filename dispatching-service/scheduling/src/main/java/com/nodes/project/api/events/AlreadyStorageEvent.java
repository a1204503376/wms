package com.nodes.project.api.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 目标库位已存在物品 事件类
 */
public class AlreadyStorageEvent extends ApplicationEvent {
    @Getter
    @Setter
    private String jobId;

    public AlreadyStorageEvent(Object source, String jobId) {
        super(source);
        this.jobId = jobId;
    }
}
