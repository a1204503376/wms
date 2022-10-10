package com.nodes.project.api.events;

import com.nodes.project.api.dto.agv.AgvSyncOrderRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 目标库位已存在物品 事件类
 */
public class AlreadyStorageEvent extends ApplicationEvent {
    @Getter
    @Setter
    private AgvSyncOrderRequest agvSyncOrderRequest;

    public AlreadyStorageEvent(Object source, AgvSyncOrderRequest agvSyncOrderRequest) {
        super(source);
        this.agvSyncOrderRequest = agvSyncOrderRequest;
    }
}
