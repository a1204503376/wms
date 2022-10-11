package com.nodes.project.api.service;

import com.nodes.project.api.dto.agv.AgvGlobalResponse;
import com.nodes.project.api.dto.agv.AgvOtherGlobalResponse;
import com.nodes.project.api.dto.wms.WmsGlobalResponse;

/**
 * 呼叫外部API
 */
public interface CallApiService {

    WmsGlobalResponse postWms(String url, Object request);

    AgvGlobalResponse postAgv(String url, Object request);

    AgvOtherGlobalResponse postOtherAgv(String url);
}
