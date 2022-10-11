package com.nodes.project.api.dto.wms;

import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;

/**
 * WMS系统通用返回结果对象 异常封装对象
 */
@Data
public class WmsGlobalResponse {

    private Boolean exceptionFlag;

    private String msg;

    private WmsResponse wmsResponse;

    public static WmsGlobalResponse success(WmsResponse agvResponse) {
        WmsGlobalResponse wmsGlobalResponse = new WmsGlobalResponse();
        wmsGlobalResponse.setWmsResponse(agvResponse);
        wmsGlobalResponse.setExceptionFlag(Boolean.FALSE);
        return wmsGlobalResponse;
    }

    public static WmsGlobalResponse error(Exception e) {
        WmsGlobalResponse agvGlobalResponse = new WmsGlobalResponse();
        agvGlobalResponse.setExceptionFlag(Boolean.TRUE);
        agvGlobalResponse.setMsg(e.getMessage());
        return agvGlobalResponse;
    }

    public boolean hasException() {
        return BooleanUtils.isTrue(exceptionFlag);
    }
}
