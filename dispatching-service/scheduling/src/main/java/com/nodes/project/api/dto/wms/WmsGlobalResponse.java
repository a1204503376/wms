package com.nodes.project.api.dto.wms;

import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.Assert;

/**
 * WMS系统通用返回结果对象 异常封装对象
 */
@Data
public class WmsGlobalResponse {

    private Boolean flagException;

    private String msg;

    private WmsResponse wmsResponse;

    public static WmsGlobalResponse success(WmsResponse agvResponse) {
        WmsGlobalResponse wmsGlobalResponse = new WmsGlobalResponse();
        wmsGlobalResponse.setWmsResponse(agvResponse);
        wmsGlobalResponse.setFlagException(Boolean.FALSE);
        return wmsGlobalResponse;
    }

    public static WmsGlobalResponse error(Exception e) {
        WmsGlobalResponse agvGlobalResponse = new WmsGlobalResponse();
        agvGlobalResponse.setFlagException(Boolean.TRUE);
        agvGlobalResponse.setMsg(e.getMessage());
        return agvGlobalResponse;
    }

    public static boolean hasException(WmsGlobalResponse wmsGlobalResponse) {
        Assert.notNull(wmsGlobalResponse, "呼叫WMS异常对象为空");
        return BooleanUtils.isTrue(wmsGlobalResponse.getFlagException());
    }
}
