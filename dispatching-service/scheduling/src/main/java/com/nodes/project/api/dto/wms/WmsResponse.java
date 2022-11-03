package com.nodes.project.api.dto.wms;

import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;

/**
 * WMS系统通用返回结果对象
 */
@Data
public class WmsResponse {

    private Integer code;
    private String msg;
    private Object data;
    private Boolean success;

    public boolean isFailed() {
        return BooleanUtils.isFalse(success);
    }
}
