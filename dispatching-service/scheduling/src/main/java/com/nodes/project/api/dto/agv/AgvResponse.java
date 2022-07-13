package com.nodes.project.api.dto.agv;

import com.nodes.common.constant.AgvConstants;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * AGV API 返回对象
 */
@Data
public class AgvResponse {

    /**
     * 结果
     * success：成功，failed：失败
     */
    private String result;
    /**
     * 失败原因
     */
    private String reason;

    public static boolean isSuccess(AgvResponse agvResponse) {
        Assert.notNull(agvResponse, "AGV返回对象为空");
        return Objects.equals(agvResponse.getResult(), AgvConstants.API_RESULT_SUCCESS);
    }

    public static boolean isFailed(AgvResponse agvResponse) {
        Assert.notNull(agvResponse, "AGV返回对象为空");
        return Objects.equals(agvResponse.getResult(), AgvConstants.API_RESULT_FAILED);
    }
}
