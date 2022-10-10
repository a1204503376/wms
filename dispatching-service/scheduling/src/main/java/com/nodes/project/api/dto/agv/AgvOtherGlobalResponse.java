package com.nodes.project.api.dto.agv;

import com.nodes.common.utils.ExceptionUtil;
import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;

/**
 * 针对AGV other 返回对象是否存在异常的包装对象
 */
@Data
public class AgvOtherGlobalResponse {
    private Boolean flagException;

    private String msg;

    private AgvOtherResponse agvOtherResponse;

    public static AgvOtherGlobalResponse success(AgvOtherResponse agvResponse) {
        AgvOtherGlobalResponse agvGlobalResponse = new AgvOtherGlobalResponse();
        agvGlobalResponse.setAgvOtherResponse(agvResponse);
        agvGlobalResponse.setFlagException(Boolean.FALSE);
        return agvGlobalResponse;
    }

    public static AgvOtherGlobalResponse error(Exception e) {
        AgvOtherGlobalResponse agvGlobalResponse = new AgvOtherGlobalResponse();
        agvGlobalResponse.setFlagException(Boolean.TRUE);
        agvGlobalResponse.setMsg(ExceptionUtil.getRootErrorMessage(e));
        return agvGlobalResponse;
    }

    public boolean isException() {
        return BooleanUtils.isTrue(flagException);
    }
}
