package com.nodes.project.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nodes.framework.web.domain.AjaxResult;
import com.nodes.project.api.domain.AgvSync;
import com.nodes.project.api.dto.agv.AgvSyncOrderRequest;

public interface AgvSyncService extends IService<AgvSync> {

    AjaxResult syncOrder(AgvSyncOrderRequest agvSyncOrderRequest);

}
