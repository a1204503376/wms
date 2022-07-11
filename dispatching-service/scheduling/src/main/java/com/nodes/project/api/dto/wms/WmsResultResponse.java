package com.nodes.project.api.dto.wms;

import lombok.Data;

/**
 * WMS系统通用返回结果对象
 */
@Data
public class WmsResultResponse {

    private Integer code;
    private String msg;
    private Object data;
    private Boolean success;
}
