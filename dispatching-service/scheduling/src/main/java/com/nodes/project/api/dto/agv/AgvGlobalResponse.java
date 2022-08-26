package com.nodes.project.api.dto.agv;

import com.nodes.common.utils.ExceptionUtil;
import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.Assert;

/**
 * 针对AGV返回对象是否存在异常的包装对象
 */
@Data
public class AgvGlobalResponse {

    private Boolean flagException;

    private String msg;

    private AgvResponse agvResponse;

    public static AgvGlobalResponse success(AgvResponse agvResponse) {
        AgvGlobalResponse agvGlobalResponse = new AgvGlobalResponse();
        agvGlobalResponse.setAgvResponse(agvResponse);
        agvGlobalResponse.setFlagException(Boolean.FALSE);
        return agvGlobalResponse;
    }

    public static AgvGlobalResponse error(Exception e) {
        AgvGlobalResponse agvGlobalResponse = new AgvGlobalResponse();
        agvGlobalResponse.setFlagException(Boolean.TRUE);
        agvGlobalResponse.setMsg(ExceptionUtil.getRootErrorMessage(e));
        return agvGlobalResponse;
    }

    public static boolean isException(AgvGlobalResponse agvGlobalResponse) {
        Assert.notNull(agvGlobalResponse, "呼叫AGV异常对象为空");
        return BooleanUtils.isTrue(agvGlobalResponse.getFlagException());
    }
}
