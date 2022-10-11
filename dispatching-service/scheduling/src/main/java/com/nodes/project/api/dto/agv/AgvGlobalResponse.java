package com.nodes.project.api.dto.agv;

import com.nodes.common.utils.ExceptionUtil;
import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;

/**
 * 针对AGV返回对象是否存在异常的包装对象
 */
@Data
public class AgvGlobalResponse {

    private Boolean exceptionFlag;

    private String msg;

    private AgvResponse agvResponse;

    public static AgvGlobalResponse success(AgvResponse agvResponse) {
        AgvGlobalResponse agvGlobalResponse = new AgvGlobalResponse();
        agvGlobalResponse.setAgvResponse(agvResponse);
        agvGlobalResponse.setExceptionFlag(Boolean.FALSE);
        return agvGlobalResponse;
    }

    public static AgvGlobalResponse error(Exception e) {
        AgvGlobalResponse agvGlobalResponse = new AgvGlobalResponse();
        agvGlobalResponse.setExceptionFlag(Boolean.TRUE);
        agvGlobalResponse.setMsg(ExceptionUtil.getRootErrorMessage(e));
        return agvGlobalResponse;
    }

    public boolean hasException() {
        return BooleanUtils.isTrue(exceptionFlag);
    }
}
