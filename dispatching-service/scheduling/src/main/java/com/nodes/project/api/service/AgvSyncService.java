package com.nodes.project.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nodes.framework.web.domain.AjaxResult;
import com.nodes.project.api.domain.AgvSync;
import com.nodes.project.api.dto.agv.AgvSyncOrderRequest;

/**
 * 针对表【agv_sync】的数据库操作Service
 */
public interface AgvSyncService extends IService<AgvSync> {

    AjaxResult syncOrder(AgvSyncOrderRequest agvSyncOrderRequest);

}
