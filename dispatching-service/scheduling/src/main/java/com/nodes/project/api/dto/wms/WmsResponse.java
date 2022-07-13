package com.nodes.project.api.dto.wms;

import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.Assert;

/**
 * WMS系统通用返回结果对象
 */
@Data
public class WmsResponse {

    private Integer code;
    private String msg;
    private Object data;
    private Boolean success;

    public static boolean isFailed(WmsResponse wmsResponse) {
        Assert.notNull(wmsResponse, "WMS返回对象为空");
        return BooleanUtils.isFalse(wmsResponse.getSuccess());
    }
}
