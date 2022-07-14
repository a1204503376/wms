package com.nodes.project.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nodes.framework.web.domain.AjaxResult;
import com.nodes.project.api.domain.AgvException;
import com.nodes.project.api.dto.agv.AgvSyncExceptionRequest;

/**
 * 针对表【agv_exception】的数据库操作Service
 */
public interface AgvExceptionService extends IService<AgvException> {

    AjaxResult syncException(AgvSyncExceptionRequest agvSyncExceptionRequest);
}
