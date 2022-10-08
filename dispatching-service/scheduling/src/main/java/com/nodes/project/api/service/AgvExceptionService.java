package com.nodes.project.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nodes.framework.web.domain.AjaxResult;
import com.nodes.project.api.domain.AgvException;
import com.nodes.project.api.dto.agv.AgvSyncExceptionRequest;

public interface AgvExceptionService extends IService<AgvException> {

    AjaxResult syncException(AgvSyncExceptionRequest agvSyncExceptionRequest);
}
